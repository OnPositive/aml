package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>NothingRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class NothingRestriction extends InternalRestriction {

	/** Constant <code>INSTANCE</code> */
	public static final NothingRestriction INSTANCE = new NothingRestriction();

	/**
	 * <p>Constructor for NothingRestriction.</p>
	 */
	protected NothingRestriction() {
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		if (o == null) {
			return Status.OK_STATUS;
		}
		return new Status(Status.ERROR, Status.NOTHING_CAN_PASS, "nothing can pass false except null");
	}

	/** {@inheritDoc} */
	@Override
	public AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof NothingRestrictionWithLocation) {
			return restriction;
		}
		return this;
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
		if (obj instanceof NothingRestriction) {
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "nothing";
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return 31;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
