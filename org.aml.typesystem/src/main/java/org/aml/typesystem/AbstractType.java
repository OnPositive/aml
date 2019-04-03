package org.aml.typesystem;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.IPropertyView;
import org.aml.typesystem.beans.PropertyBean;
import org.aml.typesystem.beans.PropertyViewImpl;
import org.aml.typesystem.meta.BasicMeta;
import org.aml.typesystem.meta.IHasType;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Abstract;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.CustomFacet;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.facets.Discriminator;
import org.aml.typesystem.meta.facets.DiscriminatorValue;
import org.aml.typesystem.meta.facets.DisplayName;
import org.aml.typesystem.meta.facets.Facet;
import org.aml.typesystem.meta.facets.FacetDeclaration;
import org.aml.typesystem.meta.facets.Polymorphic;
import org.aml.typesystem.meta.facets.internal.NothingRestriction;
import org.aml.typesystem.meta.facets.internal.NothingRestrictionWithLocation;
import org.aml.typesystem.meta.facets.internal.OriginalName;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.AdditionalProperties;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.Enum;
import org.aml.typesystem.meta.restrictions.ExternalSchemaMeta;
import org.aml.typesystem.meta.restrictions.HasPropertyRestriction;
import org.aml.typesystem.meta.restrictions.IMatchesProperty;
import org.aml.typesystem.meta.restrictions.KnownPropertyRestricton;
import org.aml.typesystem.meta.restrictions.MapPropertyIs;
import org.aml.typesystem.meta.restrictions.PropertyIs;
import org.aml.typesystem.meta.restrictions.RestrictionStackEntry;
import org.aml.typesystem.meta.restrictions.RestrictionsOptimizer;
import org.aml.typesystem.values.IParseError;
import org.aml.typesystem.values.ITypedObject;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>
 * Abstract AbstractType class.
 * </p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class AbstractType implements IType {

	protected boolean computeConfluent;

	protected boolean locked = false;

	protected boolean nullable = false;

	protected boolean isAnnotation = false;

	protected ITypeLibrary source;

	public ITypeLibrary getSource() {
		return source;
	}

	public void setSource(ITypeLibrary source) {
		this.source = source;
	}

	public boolean isAnnotation() {
		return isAnnotation;
	}

	public void setAnnotation(boolean isAnnotation) {
		this.isAnnotation = isAnnotation;
	}

	public final Set<TypeInformation> metaInfo = new LinkedHashSet<>();

	protected final String name;

	protected final LinkedHashSet<AbstractType> subTypes = new LinkedHashSet<>();

	/**
	 * <p>
	 * Constructor for AbstractType.
	 * </p>
	 *
	 * @param name
	 *            a {@link java.lang.String} object.
	 */
	public AbstractType(String name) {
		super();
		this.name = name;
	}
	
	
	/**
	 * <p>
	 * isSubTypeOf.
	 * </p>
	 *
	 * @param t
	 *            a {@link org.aml.typesystem.AbstractType} object.
	 * @return a boolean.
	 */
	public final boolean isSubTypeOf(AbstractType t) {
		return this.equals(t) || this.allSuperTypes().contains(t);
	}

	/**
	 * <p>
	 * isSuperType.
	 * </p>
	 *
	 * @param t
	 *            a {@link org.aml.typesystem.AbstractType} object.
	 * @return a boolean.
	 */
	public final boolean isSuperType(AbstractType t) {
		return this.equals(t) || this.allSubTypes().contains(t);
	}

	/**
	 * <p>
	 * toPropertiesView.
	 * </p>
	 *
	 * @return a {@link org.aml.typesystem.beans.IPropertyView} object.
	 */
	public IPropertyView toPropertiesView() {
		propertyViewImpl = new PropertyViewImpl(this);
		return propertyViewImpl;
	}

	/**
	 * {@inheritDoc}
	 *
	 * performs automatic classification of object against type family
	 */
	@Override
	public final AbstractType ac(Object obj) {
		if (obj instanceof ITypedObject) {
			AbstractType type = ((ITypedObject) obj).getType();
			if (type!=null) {
				return type;
			}
		}
		if (this.isObject()) {
			if (obj instanceof Boolean) {
				return BuiltIns.BOOLEAN;
			}
			if (obj instanceof String) {
				return BuiltIns.STRING;
			}
			if (obj instanceof Number) {
				return BuiltIns.NUMBER;
			}
		}
		
		if (!this.isPolymorphic() && !this.isUnion()) {
			return this;
		}
		final Set<AbstractType> typeFamily = typeFamily();
		if (typeFamily.isEmpty()) {
			return BuiltIns.NOTHING;
		}
		if (this.isScalar()) {
			if (this.isNumber()) {
				if (obj instanceof Number) {
					return this;
				}
				return BuiltIns.NOTHING;
			}

			if (this.isString()) {
				if (obj instanceof String) {
					return this;
				}
				return BuiltIns.NOTHING;
			}

			if (this.isBoolean()) {
				if (obj instanceof Boolean) {
					return this;
				}
				return BuiltIns.NOTHING;
			}
			return this;
		}
		if (typeFamily.size() == 1) {
			return typeFamily.iterator().next();
		}
		LinkedHashSet<AbstractType> options = new LinkedHashSet<>();
		for (final AbstractType t1 : typeFamily) {
			final Status validateDirect = t1.validateDirect(obj);
			if (validateDirect.isOk()) {
				options.add(t1);
			}
		}
		// no we need recursively exclude options by calling discriminators
		options = discriminate(obj, options);
		if (options.isEmpty()) {
			return BuiltIns.NOTHING;
		}
		return options.iterator().next();

	}

	private void addIfNotInternal(LinkedHashSet<AbstractType> results, AbstractType t) {
		if (!t.hasDirectMeta(Abstract.class) && !t.metaInfo.contains(BasicMeta.INTERNAL)) {
			results.add(t);
		}
	}

	/**
	 * <p>
	 * addMeta.
	 * </p>
	 *
	 * @param m
	 *            a {@link org.aml.typesystem.meta.TypeInformation} object.
	 */
	public final void addMeta(TypeInformation m) {		
		if (this.locked) {
			throw new IllegalArgumentException("type is locked for modification");
		}
		this.propertyViewImpl=null;
		m.setOwnerType(this);
		this.metaInfo.add(m);
	}

	/**
	 * <p>
	 * addPotentialDependency.
	 * </p>
	 *
	 * @param ts
	 *            a {@link java.util.LinkedHashSet} object.
	 * @param range
	 *            a {@link org.aml.typesystem.AbstractType} object.
	 */
	protected void addPotentialDependency(LinkedHashSet<AbstractType> ts, AbstractType range) {
		if (!range.declaredMeta().contains(BasicMeta.BUILTIN)) {
			if (range.isAnonimous()) {
				range.fillDependencies(ts);
				return;
			}
			ts.add(range);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Set<AbstractType> allDependencies() {
		final LinkedHashSet<AbstractType> ts = new LinkedHashSet<>();
		fillAllDependencies(ts);
		ts.remove(this);
		return ts;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.raml.typesystem.IType#allSubTypes()
	 */
	/**
	 * <p>
	 * allSubTypes.
	 * </p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public final Set<AbstractType> allSubTypes() {
		final LinkedHashSet<AbstractType> results = new LinkedHashSet<>();
		fillSubTypes(results);
		return results;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.raml.typesystem.IType#allSuperTypes()
	 */
	/**
	 * <p>
	 * allSuperTypes.
	 * </p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public final Set<AbstractType> allSuperTypes() {
		final LinkedHashSet<AbstractType> results = new LinkedHashSet<>();
		fillSuperTypes(results);
		return results;
	}

	/**
	 * <p>
	 * calculateACStatus.
	 * </p>
	 *
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	public Status calculateACStatus() {

		final Set<AbstractType> typeFamily = this.typeFamily();
		final Status result = new Status(Status.OK, 0, "");
		for (final AbstractType t1 : typeFamily) {
			for (final AbstractType t2 : typeFamily) {
				if (t1 != t2) {
					final Status ok = emptyIntersectionOrDiscriminator(t1, t2, new RestrictionStackEntry());
					result.addSubStatus(ok);
				}
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean canDoAC() {
		return calculateACStatus().isOk();
	}

	/**
	 * <p>
	 * checkConfluent.
	 * </p>
	 *
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	public Status checkConfluent() {
		return innerCheckConfluent();
	}

	/**
	 * <p>
	 * checkDiscriminator.
	 * </p>
	 *
	 * @param t1
	 *            a {@link org.aml.typesystem.AbstractType} object.
	 * @param t2
	 *            a {@link org.aml.typesystem.AbstractType} object.
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	protected Status checkDiscriminator(final AbstractType t1, final AbstractType t2) {
		Status found = new Status(Status.ERROR, 0,
				"can not discriminate types " + t1.toString() + " and " + t2.toString() + " without discriminator");
		final Discriminator oneMeta = t1.oneMeta(Discriminator.class);
		final Discriminator anotherMeta = t2.oneMeta(Discriminator.class);
		if (oneMeta != null && anotherMeta != null && oneMeta.value().equals(anotherMeta.value())) {

			String d1 = t1.name();
			String d2 = t2.name();
			final DiscriminatorValue dv1 = t1.oneMeta(DiscriminatorValue.class);
			if (dv1 != null) {
				d1 = (String) dv1.value();
			}
			final DiscriminatorValue dv2 = t2.oneMeta(DiscriminatorValue.class);
			if (dv2 != null) {
				d2 = (String) dv2.value();
			}
			if (!d1.equals(d2)) {
				return Status.OK_STATUS;
			}
			found = new Status(Status.ERROR, 0,
					"types" + t1.name() + " and " + t2.name() + " have same discriminator value");
		}
		return found;
	}

	private boolean checkFound(Object obj, LinkedHashSet<AbstractType> newoptions, final AbstractType t1,
			final AbstractType t2) {
		boolean found = false;
		if (t1.isScalar() && t2.isScalar()) {
			if (t1.allSubTypes().contains(t2)) {
				newoptions.remove(t2);
				return true;
			}
			if (t2.allSubTypes().contains(t1)) {
				newoptions.remove(t1);
				return true;
			}
		}
		if (t1.passesDiscrimination(obj)) {
			newoptions.remove(t2);
			found = true;
		} else {
			if (t2.passesDiscrimination(obj)) {
				newoptions.remove(t1);
				found = true;
			} else {
				newoptions.clear();
				;
			}
		}
		return found;
	}

	/**
	 * make this type closed type (no unknown properties any more)
	 */
	public void closeUnknownProperties() {
		this.addMeta(new KnownPropertyRestricton());

	}

	/**
	 * <p>
	 * declareAdditionalProperty.
	 * </p>
	 *
	 * @param type
	 *            a {@link org.aml.typesystem.AbstractType} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public final AbstractType declareAdditionalProperty(AbstractType type) {
		if (type != null) {
			this.addMeta(new AdditionalProperties(type, this));
		}
		return type;
	}

	/** {@inheritDoc} */
	@Override
	public Set<TypeInformation> declaredMeta() {
		return new LinkedHashSet<>(this.metaInfo);
	}

	/**
	 * declares a pattern property on this type, note if type is not inherited
	 * from an object type this will move type to inconsistent state
	 *
	 * @param name
	 *            - regexp
	 * @param type
	 *            - type of the property
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public final AbstractType declareMapProperty(String name, AbstractType type) {
		if (type != null) {
			this.addMeta(new MapPropertyIs(type, this, name));
		}
		return type;
	}

	/**
	 * adds new property declaration to this type, note if type is not inherited
	 * from an object type this will move type to inconsistent state
	 *
	 * @param name
	 *            - name of the property
	 * @param type
	 *            - type of the property
	 * @param optional
	 *            true if property is optinal
	 * @return the type with property (this)
	 */
	public final AbstractType declareProperty(String name, AbstractType type, boolean optional) {
		if (!optional) {
			this.addMeta(new HasPropertyRestriction(name));
		}
		if (type != null) {
			this.addMeta(new PropertyIs(type, name));
		}
		return this;
	}
	
	public final AbstractType declareProperty(String name, AbstractType type, boolean optional,boolean positional,Object defaultValue) {
		if (defaultValue!=null) {
			type=TypeOps.derive("", type);
			type.addMeta(new Default(defaultValue));
		}
		if (!optional) {
			this.addMeta(new HasPropertyRestriction(name));
		}
		if (type != null) {
			this.addMeta(new PropertyIs(type, name,positional));
		}
		else if (positional) {
			this.addMeta(new PropertyIs(BuiltIns.ANY, name,positional));
		}
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public Set<AbstractType> dependencies() {
		final LinkedHashSet<AbstractType> ts = new LinkedHashSet<>();
		fillDependencies(ts);
		ts.remove(this);
		if (this.isPolymorphic()) {
			for (final AbstractType t : this.subTypes) {
				addPotentialDependency(ts, t);
			}
		}
		return ts;
	}

	/**
	 * <p>
	 * directPropertySet.
	 * </p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public Set<String> directPropertySet() {
		final LinkedHashSet<String> rs = new LinkedHashSet<>();
		for (final IMatchesProperty p : this.meta(PropertyIs.class)) {
			rs.add(p.id());
		}
		return rs;
	}

	/**
	 * <p>
	 * discriminate.
	 * </p>
	 *
	 * @param obj
	 *            a {@link java.lang.Object} object.
	 * @param opts
	 *            a {@link java.util.LinkedHashSet} object.
	 * @return a {@link java.util.LinkedHashSet} object.
	 */
	protected LinkedHashSet<AbstractType> discriminate(Object obj, LinkedHashSet<AbstractType> opts) {
		LinkedHashSet<AbstractType> options = opts;
		LinkedHashSet<AbstractType> newoptions = new LinkedHashSet<>(options);
		while (newoptions.size() > 1) {
			for (final AbstractType t1 : options) {
				boolean found = false;
				for (final AbstractType t2 : options) {
					if (t1 != t2) {
						found = checkFound(obj, newoptions, t1, t2);
					}
					if (found) {
						break;
					}
				}
				if (found) {
					break;
				}
			}
			options = newoptions;
			newoptions = new LinkedHashSet<>(options);
		}
		return options;
	}

	private Status emptyIntersectionOrDiscriminator(final AbstractType t1, final AbstractType t2,
			RestrictionStackEntry stack) {
		if (t1 == t2) {
			return Status.OK_STATUS;
		}
		final AbstractType intersect = TypeOps.intersect("", t1, t2);
		final Status innerCheckConfluent = intersect.innerCheckConfluent();
		if (innerCheckConfluent.isOk()) {
			return checkDiscriminator(t1, t2);

		}
		return Status.OK_STATUS;
	}

	/**
	 * <p>
	 * facetValue.
	 * </p>
	 *
	 * @param facet
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	@SuppressWarnings("unchecked")
	public final <T> T facetValue(Class<? extends Facet<T>> facet) {
		final Set<TypeInformation> meta = this.meta();
		ArrayList<TypeInformation> ti = new ArrayList<>(meta);
		Collections.reverse(ti);
		for (final TypeInformation t : ti) {
			if (facet.isInstance(t)) {
				final Facet<T> f = (Facet<T>) t;
				return f.value();
			}
		}
		return null;
	}

	private void fillAllDependencies(LinkedHashSet<AbstractType> ts) {
		if (!ts.contains(this)) {
			ts.add(this);
			for (final AbstractType t : this.dependencies()) {
				t.fillAllDependencies(ts);
			}
		}
	}

	/**
	 * <p>
	 * fillDependencies.
	 * </p>
	 *
	 * @param ts
	 *            a {@link java.util.LinkedHashSet} object.
	 */
	protected void fillDependencies(LinkedHashSet<AbstractType> ts) {

		for (final TypeInformation i : this.declaredMeta()) {
			if (i instanceof IHasType) {
				final AbstractType range = ((IHasType) i).range();
				addPotentialDependency(ts, range);
			}
		}
	}

	private void fillSubTypes(LinkedHashSet<AbstractType> results) {
		for (final AbstractType t : this.subTypes) {
			if (!results.contains(t)) {
				results.add(t);
				t.fillSubTypes(results);
			} else {
				results.add(BuiltIns.RECURRENT_TYPE);
			}
		}
	}

	private void fillSuperTypes(LinkedHashSet<AbstractType> results) {
		for (final AbstractType t : this.superTypes()) {
			if (!results.contains(t)) {
				results.add(t);
				t.fillSuperTypes(results);
			} else {
				results.add(BuiltIns.RECURRENT_TYPE);
			}
		}
	}

	/**
	 * <p>
	 * innerCheckConfluent.
	 * </p>
	 *
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	protected Status innerCheckConfluent() {
		if (computeConfluent) {
			return Status.OK_STATUS;
		}
		try {

			computeConfluent = true;
			final Set<AbstractRestricton> restrictions = this.restrictions();
			final LinkedHashSet<AbstractType> hashSet = new LinkedHashSet<>();
			hashSet.add(this);
			final String checkThatRequiredPropertiesAreNotCycling = checkThatRequiredPropertiesAreNotCycling(hashSet);
			if (checkThatRequiredPropertiesAreNotCycling != null) {
				return new Status(Status.ERROR, 0,
						"Cycle in required properties definition: " + checkThatRequiredPropertiesAreNotCycling);
			}
			final Set<AbstractRestricton> optimize = RestrictionsOptimizer.optimize(restrictions);
			if (optimize.contains(NothingRestriction.INSTANCE)) {
				RestrictionStackEntry lstack = null;
				AbstractRestricton another = null;
				for (final AbstractRestricton r : optimize) {
					if (r instanceof NothingRestrictionWithLocation) {
						final NothingRestrictionWithLocation nr = (NothingRestrictionWithLocation) r;
						lstack = nr.getStack();
						another = nr.another();
					}
				}
				final RestrictionsConflict status = new RestrictionsConflict(lstack, another);
				return status;
			}
			return Status.OK_STATUS;
		} finally {
			computeConfluent = false;
		}
	}

	private String checkThatRequiredPropertiesAreNotCycling(LinkedHashSet<AbstractType> ts) {
		for (IProperty q : new PropertyViewImpl(this).allProperties()) {
			if (q.isRequired()) {
				if (ts.contains(q.range())) {
					return q.id();
				}
				LinkedHashSet<AbstractType> mn = new LinkedHashSet<>(ts);
				mn.add(q.range());
				final String checkThatRequiredPropertiesAreNotCycling = q.range()
						.checkThatRequiredPropertiesAreNotCycling(mn);
				if (checkThatRequiredPropertiesAreNotCycling != null) {
					return q.id() + "/" + checkThatRequiredPropertiesAreNotCycling;
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * isAnonimous.
	 * </p>
	 *
	 * @return true if type is an inplace type and has no name
	 */
	public boolean isAnonimous() {
		return this.name == null || this.name.isEmpty();
	}

	/**
	 * <p>
	 * isArray.
	 * </p>
	 *
	 * @return true if type is an array or extends from an array
	 */
	public boolean isArray() {
		return this.allSuperTypes().contains(BuiltIns.ARRAY) || this == BuiltIns.ARRAY;
	}

	/**
	 * <p>
	 * isBoolean.
	 * </p>
	 *
	 * @return true if type is an boolean type or extends from boolean
	 */
	public boolean isBoolean() {
		return this.allSuperTypes().contains(BuiltIns.BOOLEAN) || this == BuiltIns.BOOLEAN;
	}

	/**
	 * <p>
	 * isBuiltIn.
	 * </p>
	 *
	 * @return true if type is an built-in type
	 */
	public boolean isBuiltIn() {
		return declaredMeta().contains(BasicMeta.BUILTIN);
	}

	/**
	 * <p>
	 * isEmpty.
	 * </p>
	 *
	 * @return true if type has no associated meta information of restrictions
	 */
	public boolean isEmpty() {
		return this.metaInfo.isEmpty();
	}

	/**
	 * <p>
	 * isNumber.
	 * </p>
	 *
	 * @return true if type is number or inherited from number
	 */
	public boolean isNumber() {
		return this.allSuperTypes().contains(BuiltIns.NUMBER) || this == BuiltIns.NUMBER;
	}

	/**
	 * <p>
	 * isObject.
	 * </p>
	 *
	 * @return true if type is object or inherited from object
	 */
	public boolean isObject() {
		return this.allSuperTypes().contains(BuiltIns.OBJECT) || this == BuiltIns.OBJECT;
	}

	/**
	 * <p>
	 * isScalar.
	 * </p>
	 *
	 * @return true if type is scalar or inherited from scalar
	 */
	public boolean isScalar() {
		return this.allSuperTypes().contains(BuiltIns.SCALAR) || this == BuiltIns.SCALAR;
	}

	/**
	 * <p>
	 * isString.
	 * </p>
	 *
	 * @return true if type is string or inherited from string
	 */
	public boolean isString() {
		return this.allSuperTypes().contains(BuiltIns.STRING) || this == BuiltIns.STRING;
	}

	/**
	 * <p>
	 * isUnion.
	 * </p>
	 *
	 * @return a boolean.
	 */
	public boolean isUnion() {
		if (this instanceof UnionType) {
			return true;
		}
		for (AbstractType t : this.allSuperTypes()) {
			if (t.isUnion()) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isConfluent() {
		return !checkConfluent().isOk();
	}

	/**
	 * <p>
	 * lock.
	 * </p>
	 */
	public void lock() {
		this.locked = true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * return all type information associated with type
	 */
	@Override
	public Set<TypeInformation> meta() {
		return new LinkedHashSet<>(this.metaInfo);
	}

	/**
	 * <p>
	 * meta.
	 * </p>
	 *
	 * @param clazz
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link java.util.Set} object.
	 */
	public final <T> Set<T> meta(Class<T> clazz) {
		final LinkedHashSet<T> result = new LinkedHashSet<>();
		for (final TypeInformation i : this.meta()) {
			if (clazz.isInstance(i)) {
				result.add(clazz.cast(i));
			}
		}
		return result;
	}

	/**
	 * <p>
	 * name.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String name() {
		return this.name;
	}

	/**
	 * <p>
	 * oneMeta.
	 * </p>
	 *
	 * @param clazz
	 *            a {@link java.lang.Class} object.
	 * @return instance of meta information of particular class
	 * @param <T>
	 *            a T object.
	 */
	public final <T> T oneMeta(Class<T> clazz) {
		for (final TypeInformation i : this.meta()) {
			if (clazz.isInstance(i)) {
				return clazz.cast(i);
			}
		}
		return null;
	}

	/**
	 * <p>
	 * hasDirectMeta.
	 * </p>
	 *
	 * @param clazz
	 *            a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	public final boolean hasDirectMeta(Class<? extends TypeInformation> clazz) {
		for (final TypeInformation i : this.declaredMeta()) {
			if (clazz.isInstance(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * passesDiscrimination.
	 * </p>
	 *
	 * @param o
	 *            a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public final boolean passesDiscrimination(Object o) {
		final String discriminator = this.facetValue(Discriminator.class);
		if (discriminator == null) {
			return false;
		}
		Object discriminatorValue = this.facetValue(DiscriminatorValue.class);
		if (discriminatorValue == null) {
			discriminatorValue = this.name;
		}
		final Object value = ObjectAccess.propertyValue(discriminator, o);
		return discriminatorValue.equals(value);

	}

	/**
	 * <p>
	 * propertySet.
	 * </p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public Set<String> propertySet() {
		final LinkedHashSet<String> rs = new LinkedHashSet<>();
		for (final IMatchesProperty p : this.meta(IMatchesProperty.class)) {
			rs.add(p.id());
		}
		return rs;
	}

	public Set<String> requiredPropertySet() {
		final LinkedHashSet<String> rs = new LinkedHashSet<>();
		for (final HasPropertyRestriction p : this.meta(HasPropertyRestriction.class)) {
			rs.add(p.id());
		}
		return rs;
	}

	/** {@inheritDoc} */
	@Override
	public final Set<AbstractRestricton> restrictions() {
		final LinkedHashSet<AbstractRestricton> r = new LinkedHashSet<>();
		for (final TypeInformation t : meta()) {
			if (t instanceof AbstractRestricton) {
				r.add((AbstractRestricton) t);
			}

		}
		return r;
	}

	/**
	 * {@inheritDoc}
	 *
	 * direct sub types
	 */
	@Override
	public Set<AbstractType> subTypes() {
		return new LinkedHashSet<>(subTypes);
	}

	/**
	 * {@inheritDoc}
	 *
	 * direct super types
	 */
	@Override
	public Set<AbstractType> superTypes() {
		return new LinkedHashSet<>();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		OriginalName oneMeta = this.oneMeta(OriginalName.class);
		if (oneMeta != null) {
			return oneMeta.value();
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.raml.typesystem.IType#typeFamily()
	 */
	/** {@inheritDoc} */
	@Override
	public Set<AbstractType> typeFamily() {
		if (this.isUnion()) {
			final Set<AbstractType> superTypes = this.superTypes();
			LinkedHashSet<AbstractType> ts = new LinkedHashSet<>();
			for (AbstractType t : superTypes) {
				if (t instanceof UnionType) {
					UnionType ut = (UnionType) t;
					Set<AbstractType> allOptions = ut.allOptions();
					for (AbstractType tm : allOptions) {
						ts.addAll(tm.typeFamily());
					}
				}
			}
			return ts;
		}
		final LinkedHashSet<AbstractType> results = new LinkedHashSet<>();
		for (final AbstractType t : this.allSubTypes()) {
			addIfNotInternal(results, t);
		}
		addIfNotInternal(results, this.noPolymorph());

		return results;
	}

	public Set<AbstractType> unionTypeFamily() {
		if (this.isUnion()) {
			final Set<AbstractType> superTypes = this.superTypes();
			LinkedHashSet<AbstractType> ts = new LinkedHashSet<>();
			for (AbstractType t : superTypes) {
				if (t instanceof UnionType) {
					UnionType ut = (UnionType) t;
					Set<AbstractType> allOptions = ut.allOptions();
					for (AbstractType tm : allOptions) {
						ts.add(tm);
					}
				}
			}
			
			return ts;
		}
		return Collections.emptySet();
	}

	/** {@inheritDoc} */
	@Override
	public final Status validate(Object obj) {
		if (obj instanceof IParseError) {
			return new Status(Status.ERROR, 0, ((IParseError) obj).getMessage(),obj);
		}
		AbstractType ac = ac(obj);
		
		if (!ac.isSubTypeOf(this)) {
			String name2 = this.name;
			if (this.isAnonimous())
			{
				name2=this.superType().name;
			}
			if (obj instanceof String) {
				
				return new Status(Status.ERROR, 0, "Expected "+name2+" but got "+obj,obj);
			}
			return new Status(Status.ERROR, 0, "Expected "+name2+" but got not compatible instance of "+ac.name,obj);
		}
		Status validateDirect = ac.validateDirect(obj);
		validateDirect.setSource(obj);
		return validateDirect;
	}

	/**
	 * {@inheritDoc}
	 *
	 * validates object against this type without performing AC
	 */
	@Override
	public final Status validateDirect(Object object) {
		final Status result = new Status(Status.OK, 0, "");
		for (final AbstractRestricton r : this.restrictions()) {
			Status check = r.check(object);
			result.addSubStatus(check);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Status validateMeta(ITypeRegistry annotationTypes) {
		final Status result = new Status(Status.OK, 0, "");
		LinkedHashSet<String> facets = new LinkedHashSet<>();
		for (final TypeInformation t : this.declaredMeta()) {
			result.addSubStatus(t.validate(annotationTypes));
			if (t instanceof FacetDeclaration) {
				FacetDeclaration dl = (FacetDeclaration) t;
				facets.add(dl.getName());
			}
		}
		HashMap<Object, Object> facetMap = new HashMap<>();
		for (final TypeInformation t : this.meta()) {
			result.addSubStatus(t.validate(annotationTypes));
			if (t instanceof CustomFacet) {
				CustomFacet cf = (CustomFacet) t;
				facetMap.put(cf.facetName(), cf.value());
			}
		}
		final PropertyViewImpl propertyViewImpl = new PropertyViewImpl(this);
		final List<IProperty> allProperties = propertyViewImpl.allProperties();
		HashMap<String, IProperty> propInXML = new HashMap<>();
		for (IProperty p : allProperties) {
			final String string = p.getXMLHints().toString();
			if (propInXML.containsKey(string)) {
				result.addSubStatus(new Status(Status.ERROR, 0, "XML representation of " + propInXML.get(string).id()
						+ " conflicts with xml representation of " + p.id()));
			} else {
				propInXML.put(string, p);
			}
		}
		for (IProperty p : allProperties) {
			if (!p.isMap()) {
				for (IProperty pa : allProperties) {
					if (pa.isMap()) {
						String reg = pa.id().substring(1, pa.id().length() - 1);
						if (p.id().matches(reg)) {
							result.addSubStatus(new Status(Status.ERROR, 0,
									"Property " + p.id() + " conflicts with pattern property: " + pa.id()));
						}
					}
				}
			}
		}
		for (IProperty p : propertyViewImpl.superProperties()) {
			if (!p.isMap() && !p.isAdditional() && p.getDeclaredAt() != this && p.isRequired()) {
				PropertyBean propertyBean = propertyViewImpl.getDeclaredPropertiesMap().get(p.id());
				if (propertyBean != null && !propertyBean.isRequired()) {
					result.addSubStatus(new Status(Status.ERROR, 0,
							"Property " + p.id()
									+ " can not be declared as optional because it was declared as required in "
									+ p.getDeclaredAt().name()));
				}

			}
		}
		for (IProperty q : propertyViewImpl.allFacets()) {
			if (q.isRequired()) {
				if (!facetMap.containsKey(q.id())) {
					result.addSubStatus(new Status(Status.ERROR, 0, "Required facet " + q.id() + " is not defined"));

				}
			}
		}
		return result;
	}

	/**
	 * <p>
	 * noPolymorph.
	 * </p>
	 *
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	public abstract AbstractType noPolymorph();

	/**
	 * <p>
	 * isPolymorphic.
	 * </p>
	 *
	 * @return true if this type is a polymorhpic
	 */
	public boolean isPolymorphic() {
		final Polymorphic oneMeta = this.oneMeta(Polymorphic.class);
		if (oneMeta != null) {
			if (oneMeta.value().equals(true)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Setter for the field <code>nullable</code>.
	 * </p>
	 *
	 * @param nullable
	 *            a boolean.
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * <p>
	 * isNullable.
	 * </p>
	 *
	 * @return a boolean.
	 */
	public boolean isNullable() {
		return this.nullable;
	}

	public AbstractType clone(String string) {
		AbstractType derive = TypeOps.derive(string,
				this.superTypes().toArray(new AbstractType[this.superTypes().size()]));
		derive.metaInfo.addAll(this.declaredMeta());
		return derive;
	}

	public boolean isNill() {
		return this.isSubTypeOf(BuiltIns.NIL);
	}

	public boolean isEnumType() {
		return this.oneMeta(Enum.class) != null;
	}

	public boolean isEffectivelyEmptyType() {
		if (this.isAnonimous() && this.superTypes().size() == 1 && !this.isUnion()) {
			if (this.declaredMeta().size() == 0) {
				return true;
			}
			for (TypeInformation t : this.declaredMeta()) {
				if (t instanceof DisplayName) {
					continue;
				}
				if (t instanceof Annotation) {
					continue;
				}
				if (t instanceof Default) {
					continue;
				}
				return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean hasOnlyDisplayName() {
		if (this.isAnonimous() && this.superTypes().size() == 1 && !this.isUnion()) {
			if (this.declaredMeta().size() == 0) {
				return true;
			}
			for (TypeInformation t : this.declaredMeta()) {
				if (t instanceof DisplayName) {
					continue;
				}
				if (t instanceof Default) {
					continue;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean hasSingleSuperType() {
		return this.superTypes().size() == 1;
	}

	public AbstractType superType() {
		if (this.superTypes().size() == 1) {
			return this.superTypes().iterator().next();
		}
		return null;
	}

	public boolean isInteger() {
		return this.isSubTypeOf(BuiltIns.INTEGER);
	}

	public boolean isAnnotationType() {
		return this.isAnnotation;
	}

	public String getNameSpaceId() {
		if (this.getSource() == null) {
			return "";
		}
		for (IAnnotation a : this.getSource().annotations()) {
			AbstractType annotationType = a.annotationType();
			if (annotationType != null) {
				if (annotationType.name().toLowerCase().equals("id")) {
					if (annotationType.isString()) {
						return "" + a.value();
					}
				}
			}
		}
		return "";
	}

	public String getExternalSchemaContent() {
		if (this.isExternal()) {
			ExternalSchemaMeta oneMeta = this.oneMeta(ExternalSchemaMeta.class);
			if (oneMeta!=null){
				return oneMeta.getContent();
			}
		}
		return null;
	}

	public boolean isExternal() {
		return this.isSubTypeOf(BuiltIns.EXTERNAL);
	}
	
	
	public <T> T annotation(Class<T> clazz,boolean lookInTopLevel){
		for (TypeInformation t:meta()){
			if (t instanceof Annotation){
				Annotation a=(Annotation) t;
				T r=tryConvert(clazz, a);
				if (r!=null){
					return r;
				}
			}
		}
		if (lookInTopLevel){
			ITypeLibrary source2 = this.getSource();
			if (source2!=null){
			for (IAnnotation a:source2.annotations()){
				T r=tryConvert(clazz, a);
				if (r!=null){
					return r;
				}
			}
			}
		}
		return null;		
	}

	public static <T> T tryConvert(Class<T> clazz, IAnnotation a) {
		AbstractType annotationType = a.annotationType();
		if (annotationType!=null){
			if ((annotationType.getNameSpaceId()+"."+annotationType.name()).toLowerCase().equals(clazz.getName().toLowerCase())){
				InvocationHandler h=new InvocationHandler() {
					
					@SuppressWarnings("unchecked")
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if (method.getName().equals("value")){
							return convert(method,a.value());	
						}
						Map<String,Object>os=(Map<String, Object>) a.value();
						Object object = os.get(method.getName());
						return convert(method, object);								
					}

					private Object convert(Method method, Object object) {
						if (method.getReturnType().isArray()){
							Collection<?>c=(Collection<?>) object;
							Object arr=Array.newInstance(method.getReturnType().getComponentType(), c.size());
							int num=0;
							for (Object o:c){
								Array.set(arr, num++, o);
							}
							return arr;
						}
						return object;
					}
				};
				return clazz.cast(Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h));
			}
		}
		return null;
	}

	public boolean isFile() {
		return this.isSubTypeOf(BuiltIns.FILE);
	}
	
	protected boolean optional;

	private PropertyViewImpl propertyViewImpl;

	public boolean isOptional(){
		return this.optional;
	}
	
	public void setOptional(boolean b) {
		this.optional=b;
	}

	public AbstractType componentType() {
		ComponentShouldBeOfType oneMeta = this.oneMeta(ComponentShouldBeOfType.class);
		if (oneMeta!=null){
			return oneMeta.range();
		}
		return null;
	}

	public void removeMeta(TypeInformation oneMeta) {
		this.metaInfo.remove(oneMeta);
	}

	public boolean isSubTypeOf(String string) {		
		return false;
	}
}
