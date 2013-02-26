package com.geekend.core.game.services.network.remote;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import playn.core.Connection;
import playn.core.Net.WebSocket;
import playn.core.Net.WebSocket.Listener;
import playn.core.PlayN;

public class WebSocketRemoteConnection implements RemoteConnection {

	private WebSocket webSocket;

	private final int port = 9999;

	private final String address = "192.168.1.108";

	private final Set<RemoteMessageListener> listeners = new HashSet<RemoteMessageListener>();

	private boolean connected;

	@Override
	public void connect() {
		if (webSocket != null) return;

		webSocket = PlayN.net().createWebSocket("ws://" + address + ":" + port, new Listener() {

			@Override
			public void onTextMessage(final String msg) {
				for (final RemoteMessageListener listener : listeners)
					listener.onMessage(msg);
			}

			@Override
			public void onOpen() {
				connected = true;
				log("onOpen");
			}

			@Override
			public void onError(final String reason) {
				connected = false;
				log("onError: reason=" + reason);
			}

			@Override
			public void onDataMessage(final ByteBuffer msg) {
				connected = true;
				log("onDataMessage: msg=" + msg);
			}

			@Override
			public void onClose() {
				connected = false;
				log("onClose");
			}

		});
	}

	@Override
	public Connection registerMessageListener(final RemoteMessageListener listener) {
		listeners.add(listener);
		return new Connection() {
			@Override
			public void disconnect() {
				listeners.remove(listener);
			}
		};
	}

	@Override
	public void disconnect() {
		if (webSocket == null) return;

		webSocket.close();
		webSocket = null;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void send(final String string) {
		webSocket.send(string);
	}

	private void log(final String msg) {
		PlayN.log().debug(msg);
	}

}
