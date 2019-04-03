package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>RecurentTypeRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RecurentTypeRestriction extends InternalRestriction {

	
	/**
	 * <p>Constructor for RecurentTypeRestriction.</p>
	 */
	public RecurentTypeRestriction() {
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		if (o == null) {
			return Status.OK_STATUS;
		}
		return new Status(Status.ERROR, Status.NOTHING_CAN_PASS, message(),o);
	}

	/** {@inheritDoc} */
	@Override
	public AbstractRestricton composeWith(AbstractRestricton restriction) {		
		return nothing(restriction, "Type "+this.ownerType.name()+" is recurently defining itself");
	}
	
	

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton innerOptimize() {
		return nothing(null, message());
	}

	private String message() {
		return "Type "+this.ownerType.name()+" is recurrent";
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "recurrent";
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return 31;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return new Status(Status.ERROR,0,message(),this);
	}
}
