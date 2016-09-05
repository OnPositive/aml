package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public class InstanceOfRestriction extends InternalRestriction {

	protected final Class<?> clazz;

	public InstanceOfRestriction(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public Status check(Object o) {
		if (clazz.isInstance(o)) {
			return Status.OK_STATUS;
		}
		return error();
	}

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

	@Override
	public String facetName() {
		return "instanceOf";
	}

	@Override
	public String toString() {
		return "should be instanceof " + this.clazz.getSimpleName().toLowerCase();
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
