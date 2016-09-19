package org.aml.typesystem;

import java.util.List;

public interface ITypeLibrary {

	ITypeRegistry types();
	ITypeRegistry annotationTypes();
	
	List<? extends IAnnotation> annotations();
}
