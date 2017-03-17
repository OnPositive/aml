package org.aml.server.core;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType=IdentityType.DATASTORE)
public class Product {
	
	@PrimaryKey()
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	protected Integer id;
	
	Set<Item> items=new HashSet<>();
}
