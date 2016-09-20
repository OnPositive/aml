package org.aml.raml2java;

import org.aml.typesystem.AbstractType;

public interface IAnnotationProcessingConfig {

	boolean skipDefinition(AbstractType t);
	
	boolean skipReference(AbstractType t);
	
	
}
