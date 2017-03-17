package org.aml.examples;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("home")
public class Resource {

	
	@GET
	@Path("tools")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTools() {
		PersistenceManagerFactory persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory("Tutorial");
		PersistenceManager persistenceManager = persistenceManagerFactory.getPersistenceManager();
		
		List<Item> executeList = persistenceManager.newQuery(Item.class).filter("").executeList();
		System.out.println(executeList);
		Item pc = persistenceManager.newInstance(Item.class);
		Product ps=persistenceManager.newInstance(Product.class);
		pc.product=ps;
		//pc.related.add(executeList.get(0));
		//pc.id=1;
		pc.url="DD";
		persistenceManager.makePersistent(pc);
		return "Hello";
	}
}
