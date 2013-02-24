package com.geekend.core.net.remote;

public class RemoteEventType<T extends RemoteEventHandler> {

	private Class<? extends RemoteEvent> eventClass;

	RemoteEventType() {}

	public RemoteEventType(Class<? extends RemoteEvent> eventClass) {
		this.eventClass = eventClass;
	}

	public Class<? extends RemoteEvent> getEventClass() {
		return eventClass;
	}

}
