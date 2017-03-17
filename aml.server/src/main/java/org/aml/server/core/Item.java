package org.aml.server.core;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Sequence;
import javax.jdo.annotations.SequenceStrategy;

@PersistenceCapable(identityType=IdentityType.DATASTORE)
@Sequence(name="a",strategy=SequenceStrategy.CONTIGUOUS)
public class Item {
	
	@PrimaryKey()
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	protected Integer id;
	
	protected String url;
	
	@PrimaryKey
	protected Product product;
	
	Set<Item>related=new HashSet<>();
	
	@Override
	public String toString() {
		
		return id+related.toString();
	}
	
}
