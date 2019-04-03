package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>HasPropertyRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class HasPropertyRestriction extends AbstractRestricton {

	private final String propertyName;

	/**
	 * <p>Constructor for HasPropertyRestriction.</p>
	 *
	 * @param propertyName a {@link java.lang.String} object.
	 */
	public HasPropertyRestriction(String propertyName) {
		super();
		this.propertyName = propertyName;
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		if (ObjectAccess.propertyValue(this.propertyName, o) != null) {
			return Status.OK_STATUS;
		}
		return new Status(Status.ERROR, 0, ",missing required property " + this.propertyName,o);
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "hasProperty";
	}

	/**
	 * <p>id.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String id() {
		return this.propertyName;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "should have property " + this.propertyName;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		if (!ownerType.allSuperTypes().contains(BuiltIns.OBJECT)) {
			Status error = error("properties facet can only be used with objects");
			return error;
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
