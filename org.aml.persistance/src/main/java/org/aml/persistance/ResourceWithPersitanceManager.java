package org.aml.persistance;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.aml.persistance.jdo.JDOPersistanceManager;

public class ResourceWithPersitanceManager {

	
	protected final PersistanceManager manager=createPersistanceManager();
	
	protected PersistanceManager createPersistanceManager(){
		PersistenceManagerFactory persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory("Tutorial");
		return new JDOPersistanceManager( persistenceManagerFactory.getPersistenceManager());
	}
}
