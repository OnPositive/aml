package org.aml.typesystem.beans;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.INamedEntity;

/**
 * <p>IProperty interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IProperty extends INamedEntity {

	/**
	 * <p>id.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String  id();
	
	/**
	 * <p>getDeclaredAt.</p>
	 *
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public AbstractType getDeclaredAt();
	
	/**
	 * <p>getXMLHints.</p>
	 *
	 * @return a {@link org.aml.typesystem.beans.IXMLHints} object.
	 */
	IXMLHints getXMLHints();
	/**
	 * true if this property is required to fill
	 *
	 * @return a boolean.
	 */
	boolean isRequired();

	/**
	 * <p>isMap.</p>
	 *
	 * @return a boolean.
	 */
	boolean isMap();
	
	/**
	 * <p>isAdditional.</p>
	 *
	 * @return a boolean.
	 */
	boolean isAdditional();

	/**
	 * <p>range.</p>
	 *
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	AbstractType range();
}
