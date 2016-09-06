package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public class AllRequired implements OptionalityNullabilityChecker{

	@Override
	public boolean isOptional(IMember f) {
		return false;
	}

	@Override
	public boolean isNullable(IMember f) {
		return false;
	}
}