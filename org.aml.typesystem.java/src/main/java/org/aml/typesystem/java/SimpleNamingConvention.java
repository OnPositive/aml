package org.aml.typesystem.java;

import org.aml.typesystem.ITypeModel;

public class SimpleNamingConvention implements ITypeNamingConvention{

	@Override
	public String name(ITypeModel mdl) {
		return mdl.getName();
	}

}
