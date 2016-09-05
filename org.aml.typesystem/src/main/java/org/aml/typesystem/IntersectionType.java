package org.aml.typesystem;

import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.internal.ANDRestricton;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public final class IntersectionType extends DerivedType {

	public IntersectionType(String name, AbstractType... options) {
		super(name, options);
	}

	@Override
	protected TypeInformation createRestricton(AbstractRestricton[] rs) {
		return new ANDRestricton(rs);
	}

	@Override
	public AbstractType noPolymorph() {
		return this;
	}
}
