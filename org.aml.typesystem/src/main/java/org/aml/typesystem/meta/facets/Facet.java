package org.aml.typesystem.meta.facets;

import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;

public abstract class Facet<T> extends TypeInformation implements ISimpleFacet {

	protected T value;

	public Facet(T value) {
		super(true);
		this.value = value;
	}

	public Facet(T value, boolean inheritable) {
		super(inheritable);
		this.value = value;
	}

	@Override
	public T value() {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object vl) {
		this.value=(T) vl;
	}
}
