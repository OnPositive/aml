package org.aml.server.model;

import org.aml.server.core.Item;

public interface User {

	int id();
	
	String login();
	
	String password();
	
	String firstName();
	
	String lastName();
	
	Item[] ownedItems();
	
}
