package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.facets.internal.InstanceOfRestriction;
import org.aml.typesystem.meta.facets.internal.InternalRestriction;

public class IsObjectRestriction extends InternalRestriction {

	@Override
	public Status check(Object o) {
		if (o == null) {
			return Status.OK_STATUS;
		}
		if (o.getClass().isArray() || String.class.isInstance(o) || Number.class.isInstance(o)
				|| Boolean.class.isInstance(o)) {
			return error();
		}
		return Status.OK_STATUS;
	}

	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof IsObjectRestriction) {
			return this;
		}
		if (restriction instanceof IsArrayRestriction) {
			return nothing(restriction);
		}
		if (restriction instanceof InstanceOfRestriction) {
			return nothing(restriction);
		}
		return null;
	}

	@Override
	public String facetName() {
		return "isObject";
	}

	@Override
	public String toString() {
		return "should be object";
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
