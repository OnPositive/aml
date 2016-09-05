package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

public class HasPropertyRestriction extends AbstractRestricton {

	private final String propertyName;

	public HasPropertyRestriction(String propertyName) {
		super();
		this.propertyName = propertyName;
	}

	@Override
	public Status check(Object o) {
		if (ObjectAccess.propertyValue(this.propertyName, o) != null) {
			return Status.OK_STATUS;
		}
		return new Status(Status.ERROR, 0, ",missing required property " + this.propertyName);
	}

	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof HasPropertyRestriction) {
			final HasPropertyRestriction pc = (HasPropertyRestriction) restriction;
			if (pc.propertyName.equals(this.propertyName)) {
				return this;
			}
		}
		return null;
	}

	@Override
	public String facetName() {
		return "hasProperty";
	}

	public String id() {
		return this.propertyName;
	}

	@Override
	public String toString() {
		return "should have property " + this.propertyName;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		if (!ownerType.allSuperTypes().contains(BuiltIns.OBJECT)) {
			return error("properties facet can only be used with objects");
		}
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
