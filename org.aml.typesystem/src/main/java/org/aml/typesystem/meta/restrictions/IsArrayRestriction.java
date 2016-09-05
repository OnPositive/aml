package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.facets.internal.InstanceOfRestriction;
import org.aml.typesystem.meta.facets.internal.InternalRestriction;
import org.aml.typesystem.values.IArray;

public class IsArrayRestriction extends InternalRestriction {

	public static final IsArrayRestriction INSTANCE = new IsArrayRestriction();

	private IsArrayRestriction() {
	}

	@Override
	public Status check(Object o) {
		if (o == null || o.getClass().isArray() || IArray.class.isInstance(o)) {
			return Status.OK_STATUS;
		}
		return error();
	}

	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof IsObjectRestriction) {
			return nothing(restriction);
		}

		if (restriction instanceof InstanceOfRestriction) {
			return nothing(restriction);
		}
		return null;
	}

	@Override
	public String facetName() {
		return "isArray";
	}

	@Override
	public String toString() {
		return "should be array";
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
