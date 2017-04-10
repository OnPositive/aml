package org.aml.graphmodel;

import org.aml.typesystem.IType;

public interface Value {

	String name();
	
	boolean required();
	
	IType shape();
	
	ValueKind kind();
}
