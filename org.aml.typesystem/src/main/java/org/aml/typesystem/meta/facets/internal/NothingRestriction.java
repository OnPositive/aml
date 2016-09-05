package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public class NothingRestriction extends InternalRestriction {

	public static final NothingRestriction INSTANCE = new NothingRestriction();

	protected NothingRestriction() {
	}

	@Override
	public Status check(Object o) {
		if (o == null) {
			return Status.OK_STATUS;
		}
		return new Status(Status.ERROR, Status.NOTHING_CAN_PASS, "nothing can pass false except null");
	}

	@Override
	public AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof NothingRestrictionWithLocation) {
			return restriction;
		}
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof NothingRestriction) {
			return true;
		}
		return false;
	}

	@Override
	public String facetName() {
		return "nothing";
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
