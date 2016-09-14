package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.RestrictionsConflict;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>AdditionalProperties class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class AdditionalProperties extends IntersectRequires implements IMatchesProperty,IRangeRestriction {

	private  AbstractType baseType;
	private  AbstractType requirement;

	/**
	 * <p>Constructor for AdditionalProperties.</p>
	 */
	public AdditionalProperties() {
		super();
		this.requirement = BuiltIns.ANY;
	}
	
	/**
	 * <p>Constructor for AdditionalProperties.</p>
	 *
	 * @param requirement a {@link org.aml.typesystem.AbstractType} object.
	 * @param base a {@link org.aml.typesystem.AbstractType} object.
	 */
	public AdditionalProperties(AbstractType requirement, AbstractType base) {
		super();
		this.requirement = requirement;
		this.baseType = base;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setOwnerType(AbstractType ownerType) {
		super.setOwnerType(ownerType);
		this.baseType=ownerType;
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		final Status status = new Status(Status.OK, 0, "");
		for (final String s : ObjectAccess.properties(o)) {
			boolean alreadyMatched = false;
			for (final IMatchesProperty p : this.baseType.meta(IMatchesProperty.class)) {
				if (!(p instanceof AdditionalProperties) && p.matches(s)) {
					alreadyMatched = true;
					break;
				}
			}
			if (!alreadyMatched) {
				status.addSubStatus(this.requirement.validate(ObjectAccess.propertyValue(s, o)));
			}
		}
		return status;
	}

	private AbstractRestricton composeAdditionalPropertyIs(AdditionalProperties anotherType) {
		if (this.baseType.propertySet().equals(anotherType.baseType.propertySet())) {
			if (this.requirement.typeFamily().contains(anotherType.requirement)) {
				return anotherType;
			}
			if (anotherType.requirement.typeFamily().contains(this.requirement)) {
				return this;
			}
			final AbstractType intersect = intersect(this.requirement, anotherType.requirement);
			try {
				final Status innerCheckConfluent = intersect.checkConfluent();
				if (!innerCheckConfluent.isOk()) {
					final RestrictionsConflict restrictionsConflict = (RestrictionsConflict) innerCheckConfluent;
					return restrictionsConflict.toRestriction();
				}
				return new AdditionalProperties(intersect, this.baseType);
			} finally {
				release(intersect);
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof AdditionalProperties) {
			final AdditionalProperties anotherType = (AdditionalProperties) restriction;
			return composeAdditionalPropertyIs(anotherType);
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "additionalProperties";
	}

	/** {@inheritDoc} */
	@Override
	public String id() {
		return "//";
	}

	/** {@inheritDoc} */
	@Override
	public boolean matches(String name) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType range() {
		return requirement;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		if (!ownerType.allSuperTypes().contains(BuiltIns.OBJECT)) {
			return error("map properties can only be used with object");
		}
		if (this.requirement.isAnonimous()) {
			return this.requirement.validateMeta(null);
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
