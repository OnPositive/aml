package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>InstanceOfRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class InstanceOfRestriction extends InternalRestriction {

	protected final Class<?> clazz;

	/**
	 * <p>Constructor for InstanceOfRestriction.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 */
	public InstanceOfRestriction(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		if (clazz.isInstance(o)) {
			return Status.OK_STATUS;
		}
		return error();
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof InstanceOfRestriction) {
			final InstanceOfRestriction zz = (InstanceOfRestriction) restriction;
			if (zz.clazz.isAssignableFrom(this.clazz)) {
				return this;
			}
			if (this.clazz.isAssignableFrom(zz.clazz)) {
				return zz;
			}
			return nothing(restriction);
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "instanceOf";
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "should be instanceof " + this.clazz.getSimpleName().toLowerCase();
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
