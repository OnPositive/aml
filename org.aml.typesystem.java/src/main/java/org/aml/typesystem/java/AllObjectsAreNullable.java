package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

/**
 * <p>AllObjectsAreNullable class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public final class AllObjectsAreNullable implements OptionalityNullabilityChecker {
	/** {@inheritDoc} */
	@Override
	public boolean isOptional(IMember f) {
		return false;
	}

	/** {@inheritDoc} */
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
