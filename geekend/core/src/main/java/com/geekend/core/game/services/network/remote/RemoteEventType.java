package com.geekend.core.game.services.network.remote;

public class RemoteEventType<T extends RemoteEvent> {

	private RemoteEventFactory<T> factory;

	RemoteEventType() {}

	public RemoteEventType(RemoteEventFactory<T> factory) {
		this.factory = factory;
	}

	public RemoteEventFactory<T> getEventFactory() {
		return factory;
	}

}
