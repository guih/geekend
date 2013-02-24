package com.geekend.core.net;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import playn.core.Connection;
import playn.core.Json.Object;
import playn.core.json.JsonImpl;

import com.geekend.core.net.remote.RemoteConnection;
import com.geekend.core.net.remote.RemoteEvent;
import com.geekend.core.net.remote.RemoteEventFactory;
import com.geekend.core.net.remote.RemoteEventHandler;
import com.geekend.core.net.remote.RemoteEventType;
import com.geekend.core.net.remote.RemoteMessageListener;

public class CommunicationService implements RemoteMessageListener {

	private final RemoteConnection remote;
	private final Connection registration;
	private final Map<RemoteEventFactory<?>, Set<RemoteEventHandler<?>>> handlersMap;

	public CommunicationService(RemoteConnection remoteConnection) {
		this.handlersMap = new HashMap<RemoteEventFactory<?>, Set<RemoteEventHandler<?>>>();
		this.remote = remoteConnection;
		this.registration = remote.registerMessageListener(this);
	}

	public <T extends RemoteEvent> Connection register(final RemoteEventType<T> type,
			final RemoteEventHandler<T> handler) {
		final Set<RemoteEventHandler<?>> set = getHandlersSet(type);

		set.add(handler);
		return new Connection() {
			@Override
			public void disconnect() {
				set.remove(handler);
			}
		};
	}

	private Set<RemoteEventHandler<?>> getHandlersSet(final RemoteEventType<?> type) {
		final RemoteEventFactory<?> factory = type.getEventFactory();
		if (!handlersMap.containsKey(factory)) handlersMap.put(factory, new HashSet<RemoteEventHandler<?>>());
		return handlersMap.get(factory);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onMessage(String msg) {
		Object object = new JsonImpl().parse(msg);
		String className = object.getString("class");
		for (RemoteEventFactory factory : handlersMap.keySet()) {
			if (factory.supports(className)) {
				RemoteEvent event = factory.fromJson(object);
				for (RemoteEventHandler handler : handlersMap.get(factory)) {
					handler.onEvent(event);
				}

			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		registration.disconnect();
		super.finalize();
	}

	public <T extends RemoteEvent> void send(T event) {
		if (event == null) throw new IllegalArgumentException("event should not be null");

		remote.send(event.getEventType().getEventFactory().toJson(event));
	}
}
