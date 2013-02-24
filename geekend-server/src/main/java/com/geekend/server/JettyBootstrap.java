package com.geekend.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyBootstrap {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", true, false);
		servletContextHandler.addServlet(GeekendWebsocketServlet.class, "/");
		server.start();
		server.join();
	}
}
