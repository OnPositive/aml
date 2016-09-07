package org.aml.java2raml;

import org.aml.typesystem.IMember;
import org.aml.typesystem.java.OptionalityNullabilityChecker;

final class AllObjectsAreNullable implements OptionalityNullabilityChecker {
	@Override
	public boolean isOptional(IMember f) {
		return false;
	}

	@Override
	public boolean isNullable(IMember f) {
		if (f.isCollection()){
			return true;
		}
		if (f.getType().getFullyQualifiedName().indexOf('.')!=-1){
			return true;
		}
		return false;
	}
}