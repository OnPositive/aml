package org.aml.typesystem.meta.restrictions;

import java.util.HashSet;
import java.util.regex.PatternSyntaxException;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.RestrictionsConflict;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

public class MapPropertyIs extends IntersectRequires implements IMatchesProperty {

	private AbstractType baseType;
	private final String regexp;

	private AbstractType requirement;

	public MapPropertyIs(String regexp) {
		super();
		this.requirement = BuiltIns.ANY;
		this.regexp = regexp;
	}
	
	public MapPropertyIs(AbstractType requirement, String regexp) {
		super();
		this.requirement = requirement;
		this.regexp = regexp;
	}
	@Override
	public void setOwnerType(AbstractType ownerType) {
		this.baseType=ownerType;
		super.setOwnerType(ownerType);
	}
	
	public MapPropertyIs(AbstractType requirement, AbstractType base, String regexp) {
		super();
		this.requirement = requirement;
		this.regexp = regexp;
		this.baseType = base;
	}

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

	@Override
	public String facetName() {
		return "patternProperty";
	}

	public String getRegExp() {
		return this.regexp;
	}

	@Override
	public String id() {
		return "[" + this.regexp + "]";
	}

	@Override
	public boolean matches(String name) {
		return name.matches(this.regexp);
	}

	protected boolean psetCompatible(MapPropertyIs anotherType) {
		return this.baseType.propertySet().equals(anotherType.baseType.propertySet());
	}

	@Override
	public AbstractType range() {
		return requirement;
	}

	@Override
	public String toString() {
		return "pattern property " + this.regexp + " should be of type " + this.requirement;
	}

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

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}
}
