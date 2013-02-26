package com.geekend.core.game.services.network.remote;

public interface RemoteEventHandler<T extends RemoteEvent> {

	void onEvent(T event);

}
