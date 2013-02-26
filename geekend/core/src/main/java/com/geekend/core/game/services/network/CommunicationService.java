package com.geekend.core.game.services.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import playn.core.Connection;
import playn.core.Json.Object;
import playn.core.json.JsonImpl;

import com.geekend.core.game.services.network.remote.RemoteConnection;
import com.geekend.core.game.services.network.remote.RemoteEvent;
import com.geekend.core.game.services.network.remote.RemoteEventFactory;
import com.geekend.core.game.services.network.remote.RemoteEventHandler;
import com.geekend.core.game.services.network.remote.RemoteEventType;
import com.geekend.core.game.services.network.remote.RemoteMessageListener;

public class CommunicationService implements RemoteMessageListener {

	private final RemoteConnection remote;
	private final Connection registration;
	private final Map<RemoteEventFactory<?>, Set<RemoteEventHandler<?>>> handlersMap;

	public CommunicationService(final RemoteConnection remoteConnection) {
		handlersMap = new HashMap<RemoteEventFactory<?>, Set<RemoteEventHandler<?>>>();
		remote = remoteConnection;
		registration = remote.registerMessageListener(this);
	}

	public <T extends RemoteEvent> Connection register(final RemoteEventType<T> type, final RemoteEventHandler<T> handler) {
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
	public void onMessage(final String msg) {
		final Object object = new JsonImpl().parse(msg);
		final String className = object.getString("class");
		for (final RemoteEventFactory factory : handlersMap.keySet())
			if (factory.supports(className)) {
				final RemoteEvent event = factory.fromJson(object);
				for (final RemoteEventHandler handler : handlersMap.get(factory))
					handler.onEvent(event);

			}
	}

	@Override
	protected void finalize() throws Throwable {
		registration.disconnect();
		super.finalize();
	}

	public <T extends RemoteEvent> void send(final T event) {
		if (event == null) throw new IllegalArgumentException("event should not be null");

		if (!remote.isConnected()) return;
		remote.send(event.getEventType().getEventFactory().toJson(event));
	}
}
