package org.aml.typesystem.meta.facets.internal;

import java.util.Arrays;
import java.util.LinkedHashSet;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>ANDRestricton class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ANDRestricton extends InternalRestriction {

	protected AbstractRestricton[] options;

	/**
	 * <p>Constructor for ANDRestricton.</p>
	 *
	 * @param options a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	public ANDRestricton(AbstractRestricton... options) {
		super();
		this.options = options;
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		for (final AbstractRestricton r : this.options) {
			final Status passes = r.check(o);
			if (!passes.isOk()) {
				return passes;
			}
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof ANDRestricton) {
			final LinkedHashSet<AbstractRestricton> rs = new LinkedHashSet<>(Arrays.asList(this.options));
			rs.addAll(Arrays.asList(((ANDRestricton) restriction).options));
			if (rs.size() == 1) {
				return rs.iterator().next();
			}
			return new ANDRestricton(rs.toArray(new AbstractRestricton[rs.size()]));
		}
		for (int a = 0; a < options.length; a++) {
			final AbstractRestricton rr = options[a];
			final AbstractRestricton composeWith = rr.tryCompose(restriction);
			if (composeWith != null) {
				final AbstractRestricton[] newOpts = options.clone();
				newOpts[a] = composeWith;
				return new ANDRestricton(newOpts);
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "&";
	}

	/** {@inheritDoc} */
	@Override
	public void setOwnerType(AbstractType ownerType) {
		super.setOwnerType(ownerType);
		for (final AbstractRestricton r : options) {
			r.setOwnerType(ownerType);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder bld = new StringBuilder();
		bld.append("(");
		for (int a = 0; a < options.length; a++) {
			bld.append(options[a]);
			if (a != options.length - 1) {
				bld.append(" & ");
			}
		}
		bld.append(")");
		return bld.toString();
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		final Status okStatus = new Status(Status.OK, 0, "",this);
		for (final AbstractRestricton r : this.options) {
			okStatus.addSubStatus(r.validate(registry));
		}
		return okStatus;
	}

	/**
	 * <p>options.</p>
	 *
	 * @return an array of {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} objects.
	 */
	public AbstractRestricton[] options() {
		return this.options;
	}

}
