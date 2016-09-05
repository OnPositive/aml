package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public class ORRestricton extends InternalRestriction {

	protected AbstractRestricton[] options;

	public ORRestricton(AbstractRestricton... options) {
		super();
		this.options = options;
	}

	@Override
	public Status check(Object o) {
		for (final AbstractRestricton r : this.options) {
			if (r.check(o).isOk()) {
				return Status.OK_STATUS;
			}
		}
		return new Status(Status.ERROR, 0, "All options failed");
	}

	@Override
	/**
	 * if restrictions of this or already include this restriction
	 */
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		for (final AbstractRestricton option : options) {
			AbstractRestricton r = option.tryCompose(restriction);
			if (r == option) {
				return r;
			}
			if (r == restriction) {
				return restriction;
			}
			r = restriction.tryCompose(option);
			if (r == option) {
				return r;
			}
			if (r == restriction) {
				return r;
			}
		}
		return null;
	}

	@Override
	public String facetName() {
		return "or";
	}

	@Override
	public void setOwnerType(AbstractType ownerType) {
		super.setOwnerType(ownerType);
		for (final AbstractRestricton r : options) {
			r.setOwnerType(ownerType);
		}
	}

	@Override
	public String toString() {
		final StringBuilder bld = new StringBuilder();
		bld.append("(");
		for (int a = 0; a < options.length; a++) {
			bld.append(options[a]);
			if (a != options.length - 1) {
				bld.append(" | ");
			}
		}
		bld.append(")");
		return bld.toString();
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		final Status okStatus = new Status(Status.OK, 0, "");
		for (final AbstractRestricton r : this.options) {
			okStatus.addSubStatus(r.validate(registry));
		}
		return okStatus;
	}

	

}
