package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public class RecurentTypeRestriction extends InternalRestriction {

	
	public RecurentTypeRestriction() {
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
		return nothing(restriction, "Type "+this.ownerType.name()+" is recurently defining itself");
	}
	
	

	@Override
	protected AbstractRestricton innerOptimize() {
		return nothing(null, message());
	}

	private String message() {
		return "Type "+this.ownerType.name()+" is recurrent";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof RecurentTypeRestriction) {
			return true;
		}
		return false;
	}

	@Override
	public String facetName() {
		return "recurrent";
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return new Status(Status.ERROR,0,message());
	}
}
