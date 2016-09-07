package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

/**
 * <p>AllObjectsAreOptional class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public final class AllObjectsAreOptional implements OptionalityNullabilityChecker {
	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public boolean isNullable(IMember f) {
		return false;
	}
}
