package org.aml.persistance.jdo;

public class ReferenceInfo extends CanInit{

	protected String clazz;
	protected String property;

	
	@Override
	void init(Object value) {
		this.clazz=value.toString().substring(0,value.toString().indexOf('.'));
		this.property=value.toString().substring(value.toString().indexOf('.')+1);
	}
}
