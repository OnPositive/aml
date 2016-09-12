package org.aml.typesystem.meta.restrictions;

import java.util.HashSet;
import java.util.regex.PatternSyntaxException;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.RestrictionsConflict;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>MapPropertyIs class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class MapPropertyIs extends IntersectRequires implements IMatchesProperty ,IRangeRestriction{

	private AbstractType baseType;
	private final String regexp;

	private AbstractType requirement;

	/**
	 * <p>Constructor for MapPropertyIs.</p>
	 *
	 * @param regexp a {@link java.lang.String} object.
	 */
	public MapPropertyIs(String regexp) {
		super();
		this.requirement = BuiltIns.ANY;
		this.regexp = regexp;
	}
	
	/**
	 * <p>Constructor for MapPropertyIs.</p>
	 *
	 * @param requirement a {@link org.aml.typesystem.AbstractType} object.
	 * @param regexp a {@link java.lang.String} object.
	 */
	public MapPropertyIs(AbstractType requirement, String regexp) {
		super();
		this.requirement = requirement;
		this.regexp = regexp;
	}
	/** {@inheritDoc} */
	@Override
	public void setOwnerType(AbstractType ownerType) {
		this.baseType=ownerType;
		super.setOwnerType(ownerType);
	}
	
	/**
	 * <p>Constructor for MapPropertyIs.</p>
	 *
	 * @param requirement a {@link org.aml.typesystem.AbstractType} object.
	 * @param base a {@link org.aml.typesystem.AbstractType} object.
	 * @param regexp a {@link java.lang.String} object.
	 */
	public MapPropertyIs(AbstractType requirement, AbstractType base, String regexp) {
		super();
		this.requirement = requirement;
		this.regexp = regexp;
		this.baseType = base;
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		final Status status = new Status(Status.OK, 0, "");
		final HashSet<String> knownProps = new HashSet<>();
		for (final PropertyIs p : this.baseType.meta(PropertyIs.class)) {
			knownProps.add(p.facetName());
		}
		for (final String s : ObjectAccess.properties(o)) {
			if (knownProps.contains(s)) {
				continue;
			}
			if (this.matches(s)) {
				status.addSubStatus(this.requirement.validate(ObjectAccess.propertyValue(s, o)));
			}
		}
		return status;
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof MapPropertyIs) {
			return composeWithMap((MapPropertyIs) restriction);
		}
		return null;
	}

	private AbstractRestricton composeWithMap(MapPropertyIs anotherType) {
		final boolean re = this.regexp.equals(anotherType.regexp);
		if (re && psetCompatible(anotherType)) {
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
				return new MapPropertyIs(intersect, this.baseType, regexp);
			} finally {
				release(intersect);
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "patternProperty";
	}

	/**
	 * <p>getRegExp.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRegExp() {
		return this.regexp;
	}

	/** {@inheritDoc} */
	@Override
	public String id() {
		return "[" + this.regexp + "]";
	}

	/** {@inheritDoc} */
	@Override
	public boolean matches(String name) {
		return name.matches(this.regexp);
	}

	/**
	 * <p>psetCompatible.</p>
	 *
	 * @param anotherType a {@link org.aml.typesystem.meta.restrictions.MapPropertyIs} object.
	 * @return a boolean.
	 */
	protected boolean psetCompatible(MapPropertyIs anotherType) {
		return this.baseType.propertySet().equals(anotherType.baseType.propertySet());
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType range() {
		return requirement;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "pattern property " + this.regexp + " should be of type " + this.requirement;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		try {
			if (!ownerType.allSuperTypes().contains(BuiltIns.OBJECT)) {
				return error("map properties can only be used with object");
			}
			java.util.regex.Pattern.compile(this.regexp);
			if (this.baseType.isAnonimous()) {
				return this.baseType.validateMeta(null);
			}
			return Status.OK_STATUS;
		} catch (final PatternSyntaxException e) {
			return new Status(Status.ERROR, 1, e.getMessage());
		}
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}
}
