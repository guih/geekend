package com.geekend.core.net.remote;

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

	@Override
	public void connect() {
		if (webSocket != null) return;

		webSocket = PlayN.net().createWebSocket("ws://" + address + ":" + port, new Listener() {

			@Override
			public void onTextMessage(String msg) {
				for (RemoteMessageListener listener : listeners) {
					listener.onMessage(msg);
				}
			}

			@Override
			public void onOpen() {
				log("onOpen");
			}

			@Override
			public void onError(String reason) {
				log("onError: reason=" + reason);
			}

			@Override
			public void onDataMessage(ByteBuffer msg) {
				log("onDataMessage: msg=" + msg);
			}

			@Override
			public void onClose() {
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
	public void send(String string) {
		webSocket.send(string);
	}

	private void log(String msg) {
		PlayN.log().debug(msg);
	}

}
