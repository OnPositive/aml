package org.aml.typesystem.values;

import java.util.Set;

/**
 * <p>IObject interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IObject {

	/**
	 * <p>getProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	Object getProperty(String name);

	/**
	 * <p>keys.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<String> keys();
}
