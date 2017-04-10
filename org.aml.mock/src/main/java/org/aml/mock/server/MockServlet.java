package org.aml.mock.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MockServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected MockDataBase db=new MockDataBase();

	{
		db.add("/mock/mock.raml");
	}
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException{
		db.handle(req,resp);
	}
	
}
