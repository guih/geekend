package com.geekend.core.net;

import java.nio.ByteBuffer;

import playn.core.Net.WebSocket;
import playn.core.Net.WebSocket.Listener;
import playn.core.PlayN;

import com.geekend.core.game.component.Console;

public class CommunicationService {

	private final WebSocket webSocket;

	public CommunicationService(final Console console, final int port) {
		webSocket = PlayN.net().createWebSocket("ws://localhost:" + port, new Listener() {

			@Override
			public void onTextMessage(String msg) {
				console.log("onTextMessage: msg=" + msg);
			}

			@Override
			public void onOpen() {
				console.log("onOpen");
			}

			@Override
			public void onError(String reason) {
				console.log("onError: reason=" + reason);
			}

			@Override
			public void onDataMessage(ByteBuffer msg) {
				console.log("onDataMessage: msg=" + msg);
			}

			@Override
			public void onClose() {
				console.log("onClose");
			}

		});
	}

	public void send(String string) {
		webSocket.send(string);
	}

}
