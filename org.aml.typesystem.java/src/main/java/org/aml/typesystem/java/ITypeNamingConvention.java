package org.aml.typesystem.java;

import org.aml.typesystem.ITypeModel;

public interface ITypeNamingConvention  extends IConfiguarionExtension{

	String name(ITypeModel mdl);
}
