package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.RestrictionsConflict;
import org.aml.typesystem.Status;
import org.aml.typesystem.internal.annotations.Name;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>ComponentShouldBeOfType class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
@Name("items")
public class ComponentShouldBeOfType extends IntersectRequires implements IRangeRestriction  {

	protected final AbstractType type;

	/**
	 * <p>Constructor for ComponentShouldBeOfType.</p>
	 *
	 * @param type a {@link org.aml.typesystem.AbstractType} object.
	 */
	public ComponentShouldBeOfType(AbstractType type) {
		super();
		this.type = type;
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		final int length = ObjectAccess.length(o);
		final Status result = new Status(Status.OK, 0, "",o);
		for (int i = 0; i < length; i++) {
			result.addSubStatus(type.validate(ObjectAccess.item(o, i)));
		}
		return result;
	}

	private AbstractRestricton composeShouldBeOfType(ComponentShouldBeOfType cp) {
		if (this.type == null) {
			return cp;
		}
		boolean pl=this.type.isPolymorphic();
		if (pl&&this.type.typeFamily().contains(cp.type)) {
			return cp;
		}
		boolean pl1=cp.type.isPolymorphic();
		if (pl1&&cp.type.typeFamily().contains(this.type)) {
			return this;
		}
		if (cp.type.equals(this.type)){
			return this;
		}
		final AbstractType intersect = intersect(cp.type, this.type);
		final Status innerCheckConfluent = intersect.checkConfluent();
		try {
			if (!innerCheckConfluent.isOk()) {
				final RestrictionsConflict restrictionsConflict = (RestrictionsConflict) innerCheckConfluent;
				return restrictionsConflict.toRestriction();
			}
			return new ComponentShouldBeOfType(intersect);
		} finally {
			release(intersect);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof ComponentShouldBeOfType) {
			final ComponentShouldBeOfType cp = (ComponentShouldBeOfType) restriction;
			return composeShouldBeOfType(cp);
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "items";
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType range() {
		return this.type;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "items should be of type " + this.type;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		if (!ownerType.allSuperTypes().contains(BuiltIns.ARRAY)) {
			return error("items facet can only be used with arrays");
		}
		if (this.type.isAnonimous()) {
			return this.type.validateMeta(null);
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ARRAY;
	}

}
