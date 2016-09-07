package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

/**
 * <p>AllRequired class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class AllRequired implements OptionalityNullabilityChecker{

	/** {@inheritDoc} */
	@Override
	public boolean isOptional(IMember f) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isNullable(IMember f) {
		return false;
	}
}
