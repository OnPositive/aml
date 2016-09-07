package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public interface IPropertyNameBuilder extends IConfiguarionExtension{

	String buildName(IMember memb);
}
