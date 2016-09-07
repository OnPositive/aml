package org.aml.typesystem.beans;

import java.util.List;

/**
 * <p>IXMLHints interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IXMLHints {

	/**
	 * <p>isAttribute.</p>
	 *
	 * @return a boolean.
	 */
	boolean isAttribute();
	/**
	 * <p>localName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String  localName();
	
	/**
	 * <p>order.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<String>order();
	/**
	 * <p>qualifiedName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String qualifiedName();
	/**
	 * <p>wrapped.</p>
	 *
	 * @return a boolean.
	 */
	boolean wrapped();
}
