package org.aml.server.core;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class T {

	public static void main(String[] args) {
		PersistenceManagerFactory persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory("Tutorial");
		PersistenceManager persistenceManager = persistenceManagerFactory.getPersistenceManager();
		
		List<Item> executeList = persistenceManager.newQuery(Item.class).filter("").executeList();
		System.out.println(executeList);
		Item pc = persistenceManager.newInstance(Item.class);
		//pc.related.add(executeList.get(0));
		//pc.id=1;
		Product ps=new Product();
		pc.product=ps;
		pc.url="DD";
		persistenceManager.makePersistent(pc);
		//persistenceManager.currentTransaction().commit();		
	}
}
