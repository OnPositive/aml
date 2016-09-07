package org.aml.typesystem.beans;

import java.util.List;

import org.aml.typesystem.INamedEntity;

/**
 * <p>IPropertyView interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IPropertyView extends INamedEntity {

	/**
	 * <p>properties.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<IProperty> properties();

	/**
	 * <p>allProperties.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<IProperty> allProperties();

	/**
	 * <p>facets.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<IProperty> facets();

	/**
	 * <p>allFacets.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<IProperty> allFacets();

}
