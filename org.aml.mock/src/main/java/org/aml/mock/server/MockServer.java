package org.aml.mock.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
public class MockServer {

	public static void main(String[] args) {
		Server server = new Server(8080);		
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(MockServlet.class, "/*");
		server.setHandler(handler);
		// Start things up!
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
