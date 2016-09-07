package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

public final class AllObjectsAreOptional implements OptionalityNullabilityChecker {
	@Override
	public boolean isOptional(IMember f) {
		if (f.isCollection()){
			return true;
		}
		if (f.getType().getFullyQualifiedName().indexOf('.')!=-1){
			return true;
		}
		return false;
	}

	@Override
	public boolean isNullable(IMember f) {
		return false;
	}
}