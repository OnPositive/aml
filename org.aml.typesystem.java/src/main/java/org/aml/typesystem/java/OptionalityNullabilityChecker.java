package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

/**
 * <p>OptionalityNullabilityChecker interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface OptionalityNullabilityChecker extends IConfiguarionExtension{

	/**
	 * <p>isOptional.</p>
	 *
	 * @param f a {@link org.aml.typesystem.IMember} object.
	 * @return a boolean.
	 */
	boolean isOptional(IMember f);

	/**
	 * <p>isNullable.</p>
	 *
	 * @param f a {@link org.aml.typesystem.IMember} object.
	 * @return a boolean.
	 */
	boolean isNullable(IMember f);

}
