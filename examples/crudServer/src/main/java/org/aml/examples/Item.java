package org.aml.examples;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.DATASTORE)
public class Item {
	
	@PrimaryKey()
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	protected Integer id;
	
	protected String url;
	
	protected Product product;
	
	Set<Item>related=new HashSet<>();
	
	@Override
	public String toString() {
		
		return id+related.toString();
	}
	
}
