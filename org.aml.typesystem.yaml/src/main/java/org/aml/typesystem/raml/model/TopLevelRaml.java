package org.aml.typesystem.raml.model;

import java.util.Map;

import org.aml.typesystem.ITypeRegistry;

public interface TopLevelRaml extends Annotable{
	
	ITypeRegistry types();
	
	ITypeRegistry annotationTypes();

	Map<String,? extends Library>uses();
}
