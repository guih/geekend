package com.geekend.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyBootstrap {

	private static final int PORT = 9999;

	public static void main(String[] args) throws Exception {
		Server server = new Server(PORT);
		ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", true, false);
		servletContextHandler.addServlet(GeekendWebsocketServlet.class, "/");
		server.start();
		server.join();
	}
}
