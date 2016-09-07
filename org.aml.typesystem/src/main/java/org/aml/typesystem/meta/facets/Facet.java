package org.aml.typesystem.meta.facets;

import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;

/**
 * <p>Abstract Facet class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class Facet<T> extends TypeInformation implements ISimpleFacet {

	protected T value;

	/**
	 * <p>Constructor for Facet.</p>
	 *
	 * @param value a T object.
	 */
	public Facet(T value) {
		super(true);
		this.value = value;
	}

	/**
	 * <p>Constructor for Facet.</p>
	 *
	 * @param value a T object.
	 * @param inheritable a boolean.
	 */
	public Facet(T value, boolean inheritable) {
		super(inheritable);
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public T value() {
		return value;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object vl) {
		this.value=(T) vl;
	}
}
