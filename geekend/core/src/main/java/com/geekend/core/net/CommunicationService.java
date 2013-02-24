package com.geekend.core.net;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import playn.core.Connection;
import playn.core.Json.Object;
import playn.core.Json.Writer;
import playn.core.PlayN;
import playn.core.json.JsonImpl;

import com.geekend.core.net.remote.RemoteConnection;
import com.geekend.core.net.remote.RemoteEvent;
import com.geekend.core.net.remote.RemoteEventHandler;
import com.geekend.core.net.remote.RemoteEventType;
import com.geekend.core.net.remote.RemoteMessageListener;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class CommunicationService implements RemoteMessageListener {

	private final RemoteConnection remote;
	private final Connection registration;
	private final SetMultimap<RemoteEventType<?>, RemoteEventHandler<?>> handlersMap;

	public CommunicationService(RemoteConnection remoteConnection) {
		this.handlersMap = HashMultimap.create();
		this.remote = remoteConnection;
		registration = remote.registerMessageListener(this);
	}

	public <T extends RemoteEventHandler<?>> Connection register(final RemoteEventType<T> type, final T handler) {
		handlersMap.put(type, handler);
		return new Connection() {
			@Override
			public void disconnect() {
				handlersMap.remove(type, handler);
			}
		};
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onMessage(String msg) {
		Object object = new JsonImpl().parse(msg);
		String className = object.getString("class");
		for (RemoteEventType<?> type : handlersMap.keySet()) {
			Class eventClass = type.getEventClass();
			if (eventClass.getName().equals(className)) try {
				Constructor constructor = eventClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				RemoteEvent instance = (RemoteEvent) constructor.newInstance();
				for (String key : object.keys()) {
					if (key.equals("class")) continue;
					Field field = eventClass.getDeclaredField(key);
					field.setAccessible(true);
					if (object.isNull(key))
						field.set(instance, null);
					else if (object.isString(key))
						field.set(instance, object.getString(key, ""));
					else if (object.isBoolean(key))
						field.set(instance, object.getBoolean(key, false));
					else if (object.isNumber(key)) field.set(instance, object.getNumber(key, 0F));
				}

				for (RemoteEventHandler handler : handlersMap.get(type)) {
					handler.onEvent(instance);
				}

				return;
			} catch (Exception e) {
				e.printStackTrace();
				PlayN.log().error("Unable to create event from message", e);
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		registration.disconnect();
		super.finalize();
	}

	@SuppressWarnings("rawtypes")
	public void send(RemoteEvent event) {
		if (event == null) throw new IllegalArgumentException("event should not be null");

		try {
			Writer writer = new JsonImpl().newWriter().object();
			Class eventClass = event.getClass();
			writer.value("class", eventClass.getName());
			for (Field f : eventClass.getDeclaredFields()) {
				if (Modifier.isStatic(f.getModifiers())) continue;
				f.setAccessible(true);
				String name = f.getName();
				Class<?> fieldType = f.getType();

				if (f.get(event) == null)
					writer.nul(name);
				else if (String.class.isAssignableFrom(fieldType))
					writer.value(name, (String) f.get(event));
				else if (Boolean.class.isAssignableFrom(fieldType))
					writer.value(name, f.getBoolean(event));
				else if (Number.class.isAssignableFrom(fieldType) || fieldType == float.class || fieldType == int.class)
					writer.value(name, f.getFloat(event));
				else
					throw new RuntimeException("The attributes of an event should be String, Boolean, Number or null");
			}
			remote.send(writer.end().write());
		} catch (Exception e) {
			e.printStackTrace();
			PlayN.log().error("Unable to send event", e);
		}
	}

}
