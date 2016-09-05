package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public abstract class InternalRestriction extends AbstractRestricton {

	
	@Override
	public final AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}
