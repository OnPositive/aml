package org.aml.typesystem.beans;

/**
 * <p>ISimpleFacet interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface ISimpleFacet {

	/**
	 * <p>facetName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String facetName();
		
	/**
	 * <p>value.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	Object value();
	
	/**
	 * <p>setValue.</p>
	 *
	 * @param vl a {@link java.lang.Object} object.
	 */
	void setValue(Object vl);
}
