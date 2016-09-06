package org.aml.typesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.IPropertyView;
import org.aml.typesystem.beans.PropertyBean;
import org.aml.typesystem.beans.PropertyViewImpl;
import org.aml.typesystem.meta.BasicMeta;
import org.aml.typesystem.meta.IHasType;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Abstract;
import org.aml.typesystem.meta.facets.CustomFacet;
import org.aml.typesystem.meta.facets.Discriminator;
import org.aml.typesystem.meta.facets.DiscriminatorValue;
import org.aml.typesystem.meta.facets.Facet;
import org.aml.typesystem.meta.facets.FacetDeclaration;
import org.aml.typesystem.meta.facets.Polymorphic;
import org.aml.typesystem.meta.facets.internal.NothingRestriction;
import org.aml.typesystem.meta.facets.internal.NothingRestrictionWithLocation;
import org.aml.typesystem.meta.facets.internal.OriginalName;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.AdditionalProperties;
import org.aml.typesystem.meta.restrictions.HasPropertyRestriction;
import org.aml.typesystem.meta.restrictions.IMatchesProperty;
import org.aml.typesystem.meta.restrictions.KnownPropertyRestricton;
import org.aml.typesystem.meta.restrictions.MapPropertyIs;
import org.aml.typesystem.meta.restrictions.PropertyIs;
import org.aml.typesystem.meta.restrictions.RestrictionStackEntry;
import org.aml.typesystem.meta.restrictions.RestrictionsOptimizer;
import org.aml.typesystem.values.ObjectAccess;

public abstract class AbstractType implements IType {

	protected boolean computeConfluent;

	protected boolean locked = false;
	
	protected boolean nullable =false;

	public final Set<TypeInformation> metaInfo = new LinkedHashSet<>();

	protected final String name;

	protected final LinkedHashSet<AbstractType> subTypes = new LinkedHashSet<>();

	public AbstractType(String name) {
		super();
		this.name = name;
	}
	
	public final boolean isSubTypeOf(AbstractType t){
		return this.equals(t)||this.allSuperTypes().contains(t);
	}
	
	public final boolean isSuperType(AbstractType t){
		return this.equals(t)||this.allSubTypes().contains(t);
	}
	
	public IPropertyView toPropertiesView(){
		return new PropertyViewImpl(this);
	}

	/**
	 * performs automatic classification of object against type family
	 */
	@Override
	public final AbstractType ac(Object obj) {
		if (!this.isPolymorphic()&&!this.isUnion()) {
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

	public final void addMeta(TypeInformation m) {
		if (this.locked) {
			throw new IllegalArgumentException("type is locked for modification");
		}
		m.setOwnerType(this);
		this.metaInfo.add(m);
	}

	protected void addPotentialDependency(LinkedHashSet<AbstractType> ts, AbstractType range) {
		if (!range.declaredMeta().contains(BasicMeta.BUILTIN)) {
			if (range.isAnonimous()){
				range.fillDependencies(ts);
				return;
			}
			ts.add(range);
		}
	}

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
	public final Set<AbstractType> allSuperTypes() {
		final LinkedHashSet<AbstractType> results = new LinkedHashSet<>();
		fillSuperTypes(results);
		return results;
	}

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

	@Override
	public boolean canDoAC() {
		return calculateACStatus().isOk();
	}

	public Status checkConfluent() {
		return innerCheckConfluent();
	}

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

	public final AbstractType declareAdditionalProperty(AbstractType type) {
		if (type != null) {
			this.addMeta(new AdditionalProperties(type, this));
		}
		return type;
	}

	@Override
	public Set<TypeInformation> declaredMeta() {
		return new LinkedHashSet<>(this.metaInfo);
	}

	/**
	 * declares a pattern property on this type,
	 * note if type is not inherited from an object type this will move
	 * type to inconsistent state
	 * @param name - regexp 
	 * @param type - type of the property
	 * @return
	 */
	public final AbstractType declareMapProperty(String name, AbstractType type) {
		if (type != null) {
			this.addMeta(new MapPropertyIs(type, this, name));
		}
		return type;
	}

	/**
	 * adds new property declaration to this type, note if type is not inherited from an object type this will move
	 * type to inconsistent state
	 * @param name - name of the property
	 * @param type - type of the property
	 * @param optional true if property is optinal
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

	public Set<String> directPropertySet() {
		final LinkedHashSet<String> rs = new LinkedHashSet<>();
		for (final IMatchesProperty p : this.meta(PropertyIs.class)) {
			rs.add(p.id());
		}
		return rs;
	}

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
	 * 
	 * @return true if type is an inplace type and has no name
	 */
	public boolean isAnonimous() {
		return this.name == null || this.name.isEmpty();
	}

	/**
	 * 
	 * @return true if type is an array or extends from an array
	 */
	public boolean isArray() {
		return this.allSuperTypes().contains(BuiltIns.ARRAY) || this == BuiltIns.ARRAY;
	}

	/**
	 * 
	 * @return true if type is an boolean type or extends from boolean
	 */
	public boolean isBoolean() {
		return this.allSuperTypes().contains(BuiltIns.BOOLEAN) || this == BuiltIns.BOOLEAN;
	}

	/**
	 * 
	 * @return true if type is an built-in type
	 */
	public boolean isBuiltIn() {
		return declaredMeta().contains(BasicMeta.BUILTIN);
	}


	/**
	 * 
	 * @return true if type has no associated meta information of restrictions
	 */
	public boolean isEmpty() {
		return this.metaInfo.isEmpty();
	}

	/**
	 * 
	 * @return true if type is number or inherited from number
	 */
	public boolean isNumber() {
		return this.allSuperTypes().contains(BuiltIns.NUMBER) || this == BuiltIns.NUMBER;
	}

	/**
	 * 
	 * @return true if type is object or inherited from object
	 */
	public boolean isObject() {
		return this.allSuperTypes().contains(BuiltIns.OBJECT) || this == BuiltIns.OBJECT;
	}

	/**
	 * 
	 * @return true if type is scalar or inherited from scalar
	 */
	public boolean isScalar() {
		return this.allSuperTypes().contains(BuiltIns.SCALAR) || this == BuiltIns.SCALAR;
	}
	/**
	 * 
	 * @return true if type is string or inherited from string
	 */
	public boolean isString() {
		return this.allSuperTypes().contains(BuiltIns.STRING) || this == BuiltIns.STRING;
	}
	
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


	@Override
	public boolean isConfluent() {
		return !checkConfluent().isOk();
	}
	
	

	public void lock() {
		this.locked = true;
	}

	/**
	 * return all type information associated with type
	 */
	@Override
	public Set<TypeInformation> meta() {
		return new LinkedHashSet<>(this.metaInfo);
	}

	public final <T> Set<T> meta(Class<T> clazz) {
		final LinkedHashSet<T> result = new LinkedHashSet<>();
		for (final TypeInformation i : this.meta()) {
			if (clazz.isInstance(i)) {
				result.add(clazz.cast(i));
			}
		}
		return result;
	}

	public String name() {
		return this.name;
	}
	
	/**
	 * 
	 * @param clazz
	 * @return instance of meta information of particular class
	 */
	public final <T> T oneMeta(Class<T> clazz) {
		for (final TypeInformation i : this.meta()) {
			if (clazz.isInstance(i)) {
				return clazz.cast(i);
			}
		}
		return null;
	}
	public final boolean hasDirectMeta(Class<? extends TypeInformation> clazz) {
		for (final TypeInformation i : this.declaredMeta()) {
			if (clazz.isInstance(i)) {
				return true;
			}
		}
		return false;
	}

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

	public Set<String> propertySet() {
		final LinkedHashSet<String> rs = new LinkedHashSet<>();
		for (final IMatchesProperty p : this.meta(IMatchesProperty.class)) {
			rs.add(p.id());
		}
		return rs;
	}

	/**
	 * @return all restrictions associated with type
	 */
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
	 * direct sub types
	 */
	@Override
	public Set<AbstractType> subTypes() {
		return new LinkedHashSet<>(subTypes);
	}

	/**
	 * direct super types
	 */
	@Override
	public Set<AbstractType> superTypes() {
		return new LinkedHashSet<>();
	}

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

	@Override
	public final Status validate(Object obj) {
		return ac(obj).validateDirect(obj);
	}

	/**
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
			if (!p.isMap()&&!p.isAdditional()&&p.getDeclaredAt()!=this&&p.isRequired()) {
				PropertyBean propertyBean = propertyViewImpl.getDeclaredPropertiesMap().get(p.id());
				if (propertyBean!=null&&!propertyBean.isRequired()){
					result.addSubStatus(new Status(Status.ERROR, 0,
							"Property " + p.id() + " can not be declared as optional because it was declared as required in "+p.getDeclaredAt().name()));
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

	public abstract AbstractType noPolymorph() ;

	/**
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

	public void setNullable(boolean nullable) {
		this.nullable=nullable;
	}
}