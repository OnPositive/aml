package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

/**
 * <p>IMemberFilter interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IMemberFilter extends IConfiguarionExtension{

	/**
	 * <p>accept.</p>
	 *
	 * @param member a {@link org.aml.typesystem.IMember} object.
	 * @return a boolean.
	 */
	boolean accept(IMember member);
}
