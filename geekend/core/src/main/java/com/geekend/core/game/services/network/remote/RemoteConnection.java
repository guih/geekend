package com.geekend.core.game.services.network.remote;

import playn.core.Connection;

public interface RemoteConnection {

	void connect();

	Connection registerMessageListener(RemoteMessageListener listener);

	void disconnect();

	void send(String string);

	boolean isConnected();

}
