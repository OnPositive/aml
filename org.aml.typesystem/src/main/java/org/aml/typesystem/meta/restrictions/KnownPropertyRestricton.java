package org.aml.typesystem.meta.restrictions;

import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.ITransientMeta;
import org.aml.typesystem.meta.facets.internal.InternalRestriction;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>KnownPropertyRestricton class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class KnownPropertyRestricton extends InternalRestriction implements ITransientMeta {


	/**
	 * <p>Constructor for KnownPropertyRestricton.</p>
	 */
	public KnownPropertyRestricton() {
		super();
	}

	/**
	 * <p>base.</p>
	 *
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public AbstractType base() {
		return this.ownerType;
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		final Set<IMatchesProperty> meta = this.ownerType.meta(IMatchesProperty.class);
		final LinkedHashSet<String> propNames = new LinkedHashSet<>(ObjectAccess.properties(o));
		for (final String s : ObjectAccess.properties(o)) {
			for (final IMatchesProperty m : meta) {
				if (m.matches(s)) {
					propNames.remove(s);
					break;
				}
			}
		}
		if (!propNames.isEmpty()) {
			return new Status(Status.ERROR, 0, "unmatched properties:" + propNames.toString());
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof KnownPropertyRestricton) {
			final KnownPropertyRestricton mm = (KnownPropertyRestricton) restriction;
			if (this.ownerType.propertySet().equals(mm.ownerType.propertySet())) {
				return mm;
			}
		}
		if (restriction instanceof HasPropertyRestriction) {
			final HasPropertyRestriction ps = (HasPropertyRestriction) restriction;
			final String name = ps.id();
			final Set<String> allowedPropertySet = this.ownerType.directPropertySet();
			if (!allowedPropertySet.contains(name)) {
				return nothing(ps);
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "knownProperties";
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "should not have unmatched properties";
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
