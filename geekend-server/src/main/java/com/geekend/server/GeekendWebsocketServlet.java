package com.geekend.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class GeekendWebsocketServlet extends WebSocketServlet {

	private static final long serialVersionUID = 5603312865118498559L;

	private final Set<SampleWebSocket> connectedClients = new CopyOnWriteArraySet<SampleWebSocket>();

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {}

	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		return new SampleWebSocket();
	}

	public class SampleWebSocket implements WebSocket.OnTextMessage {

		private volatile Connection connection;

		public void onOpen(Connection connection) {
			this.connection = connection;
			System.out.println("onOpen: connection=" + connection);
			connectedClients.add(this);
		}

		public void onClose(int closeCode, String message) {
			System.out.println("onClose: closeCode=" + closeCode + ", message=" + message);
			connectedClients.remove(this);
		}

		public void onMessage(String data) {
			System.out.println("onMessage: data=" + data);
			for (final SampleWebSocket client : connectedClients) {
				if (client == this) continue;

				try {
					client.connection.sendMessage(data);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
