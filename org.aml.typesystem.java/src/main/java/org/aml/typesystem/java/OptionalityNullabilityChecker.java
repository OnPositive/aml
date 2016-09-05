package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public interface OptionalityNullabilityChecker {

	boolean isOptional(IMember f);

	boolean isNullable(IMember f);

}
