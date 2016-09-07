package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public interface OptionalityNullabilityChecker extends IConfiguarionExtension{

	boolean isOptional(IMember f);

	boolean isNullable(IMember f);

}
