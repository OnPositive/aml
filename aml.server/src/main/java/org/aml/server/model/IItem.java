package org.aml.server.model;

public interface IItem {
	
	String url();
	
	String shortId();
	
	int id();
	
	IItem[] depenedentItems();
	
	IItem[] dependencies();
	
	User owner();
	
	User[] editors();
	
	User[] readers();
	
	boolean isPublic();

}
