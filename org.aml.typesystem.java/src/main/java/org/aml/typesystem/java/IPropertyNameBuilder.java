package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

/**
 * <p>IPropertyNameBuilder interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IPropertyNameBuilder extends IConfiguarionExtension{

	/**
	 * <p>buildName.</p>
	 *
	 * @param memb a {@link org.aml.typesystem.IMember} object.
	 * @return a {@link java.lang.String} object.
	 */
	String buildName(IMember memb);
}
