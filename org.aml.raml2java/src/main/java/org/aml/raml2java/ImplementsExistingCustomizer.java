package org.aml.raml2java;

import org.aml.java.mapping.implementsExisting;

public class ImplementsExistingCustomizer implements IClassCustomizer{

	@Override
	public void customize(ClassCustomizerParameters parameters) {
		implementsExisting annotation = parameters.type.annotation(implementsExisting.class, true);
		for (String vl:annotation.value()){
			parameters.clazz._implements(parameters.writer.getModel().directClass(vl));	
		}		
	}

	
}
