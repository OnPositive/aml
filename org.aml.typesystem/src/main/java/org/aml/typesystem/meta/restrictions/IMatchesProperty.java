package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.meta.IHasType;

/**
 * <p>IMatchesProperty interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IMatchesProperty extends IHasType {

	/**
	 * <p>id.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String id();

	/**
	 * <p>matches.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	boolean matches(String name);

}
