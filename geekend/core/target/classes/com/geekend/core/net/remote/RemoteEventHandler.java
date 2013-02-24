package com.geekend.core.net.remote;

public interface RemoteEventHandler<T extends RemoteEvent> {

	void onEvent(T event);

}
