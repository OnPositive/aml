package org.aml.typesystem.ramlreader;

import org.aml.apimodel.impl.NamedParamImpl;
import org.aml.typesystem.AbstractType;

public class NamedParam extends NamedParamImpl{

	public NamedParam(AbstractType type, boolean required, boolean repeat) {
		super(type, required, repeat);
	}
	public NamedParam(String name,AbstractType type, boolean required, boolean repeat) {
		super(type, required, repeat);
	}
	
}
