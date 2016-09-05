package org.aml.typesystem.beans;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.INamedEntity;

public interface IProperty extends INamedEntity {

	String  id();
	
	public AbstractType getDeclaredAt();
	
	IXMLHints getXMLHints();
	/**
	 * true if this property is required to fill
	 */
	boolean isRequired();

	boolean isMap();
	
	boolean isAdditional();

	AbstractType range();
}
