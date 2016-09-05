package org.aml.typesystem.java;

import org.aml.typesystem.ITypeModel;

public class QualifiedNamingConvention implements ITypeNamingConvention{

	@Override
	public String name(ITypeModel mdl) {
		return mdl.getFullyQualifiedName();
	}
	
}
