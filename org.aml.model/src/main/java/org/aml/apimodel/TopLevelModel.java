package org.aml.apimodel;

import java.util.List;
import java.util.Map;

import org.aml.typesystem.ITypeLibrary;
import org.aml.typesystem.ITypeRegistry;

public interface TopLevelModel extends Annotable, ITypeLibrary{

	String getVersion();

	ITypeRegistry types();
	
	ITypeRegistry annotationTypes();

	Map<String,? extends Library>uses();
	
	List<SecurityScheme>securityDefinitions();
}
