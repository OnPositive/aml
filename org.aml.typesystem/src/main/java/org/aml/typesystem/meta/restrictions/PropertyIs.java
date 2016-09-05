package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.RestrictionsConflict;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

public class PropertyIs extends IntersectRequires implements IMatchesProperty {

	private final String name;

	private final AbstractType requirement;

	public PropertyIs(String name) {
		super();
		this.requirement = BuiltIns.ANY;
		this.name = name;
	}
	
	public PropertyIs(AbstractType requirement, String name) {
		super();
		this.requirement = requirement;
		this.name = name;
	}

	@Override
	public Status check(Object o) {
		final Object propertyValue = ObjectAccess.propertyValue(name, o);
		if (propertyValue == null) {
			return Status.OK_STATUS;
		}
		final Status validate = this.requirement.validate(propertyValue);
		if (!validate.isOk()){
			return new Status(Status.ERROR, 0, "value of property "+this.name+" "+validate.getMessage());
		}
		return validate;
	}

	private AbstractRestricton composePropertyIs(PropertyIs anotherType) {
		if (this.name.equals(anotherType.name)) {
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
				return new PropertyIs(intersect, name);
			} finally {
				release(intersect);
			}
		}
		return null;
	}

	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof PropertyIs) {
			final PropertyIs anotherType = (PropertyIs) restriction;
			return composePropertyIs(anotherType);
		}
		return null;
	}

	@Override
	public String facetName() {
		return this.name;
	}

	@Override
	public String id() {
		return this.name;
	}

	@Override
	public boolean matches(String name) {
		return this.name.equals(name);
	}

	@Override
	public AbstractType range() {
		return requirement;
	}

	@Override
	public String toString() {
		return this.name + ":" + this.requirement.name();
	}

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

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
