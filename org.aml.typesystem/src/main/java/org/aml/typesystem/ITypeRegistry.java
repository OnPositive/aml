package org.aml.typesystem;

import java.util.Collection;

/**
 * <p>ITypeRegistry interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface ITypeRegistry {

	/**
	 * <p>getType.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	AbstractType getType(String type);

	/**
	 * <p>types.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	Collection<AbstractType> types();
}
