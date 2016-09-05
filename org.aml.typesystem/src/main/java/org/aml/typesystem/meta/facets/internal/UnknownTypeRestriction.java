package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public class UnknownTypeRestriction extends InternalRestriction {

	
	public UnknownTypeRestriction() {
	}

	@Override
	public Status check(Object o) {
		if (o == null) {
			return Status.OK_STATUS;
		}
		return new Status(Status.ERROR, Status.NOTHING_CAN_PASS, message());
	}

	@Override
	public AbstractRestricton composeWith(AbstractRestricton restriction) {		
		return nothing(restriction, message());
	}
	
	

	@Override
	protected AbstractRestricton innerOptimize() {
		return nothing(null, message());
	}

	private String message() {
		return "Type "+this.ownerType.name()+" is unknown";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof UnknownTypeRestriction) {
			return true;
		}
		return false;
	}

	@Override
	public String facetName() {
		return "unknown";
	}

	@Override
	public int hashCode() {
		return 31;
	}
	@Override
	public String toString() {
		return message();
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return new Status(Status.ERROR, 0, message());
	}

}