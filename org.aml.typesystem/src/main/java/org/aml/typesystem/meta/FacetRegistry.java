package org.aml.typesystem.meta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.internal.annotations.Name;
import org.aml.typesystem.meta.facets.Abstract;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.Discriminator;
import org.aml.typesystem.meta.facets.DiscriminatorValue;
import org.aml.typesystem.meta.facets.DisplayName;
import org.aml.typesystem.meta.facets.Example;
import org.aml.typesystem.meta.facets.Format;
import org.aml.typesystem.meta.facets.Polymorphic;
import org.aml.typesystem.meta.facets.XMLFacet;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.AdditionalProperties;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.Enum;
import org.aml.typesystem.meta.restrictions.HasPropertyRestriction;
import org.aml.typesystem.meta.restrictions.KnownPropertyRestricton;
import org.aml.typesystem.meta.restrictions.MapPropertyIs;
import org.aml.typesystem.meta.restrictions.Pattern;
import org.aml.typesystem.meta.restrictions.PropertyIs;
import org.aml.typesystem.meta.restrictions.UniqueItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxLength;
import org.aml.typesystem.meta.restrictions.minmax.MaxProperties;
import org.aml.typesystem.meta.restrictions.minmax.Maximum;
import org.aml.typesystem.meta.restrictions.minmax.MinItems;
import org.aml.typesystem.meta.restrictions.minmax.MinLength;
import org.aml.typesystem.meta.restrictions.minmax.MinProperties;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;

public class FacetRegistry {

	private FacetRegistry() {
	}

	public static class FacetPrototype {

		protected Constructor<? extends TypeInformation> constructor;

		protected final Class<? extends TypeInformation> facetType;

		protected final TypeInformation emptyProto;

		@SuppressWarnings("unchecked")
		public FacetPrototype(Class<? extends TypeInformation> facetType) {
			super();
			this.facetType = facetType;
			Constructor<?>[] declaredConstructors = facetType.getConstructors();
			for (Constructor<?> c : declaredConstructors) {
				if (c.getParameterTypes().length <= 1) {
					constructor = (Constructor<? extends TypeInformation>) c;
				}
			}
			if (constructor == null) {
				throw new IllegalStateException(facetType.getName());
			}
			emptyProto = newInstance();
		}

		public boolean isInheritable() {
			return newInstance().isInheritable();
		}

		public boolean isConstraint() {
			return AbstractRestricton.class.isAssignableFrom(this.facetType);
		}

		public boolean isMeta() {
			return !isConstraint();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((facetType == null) ? 0 : facetType.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return name();
		}

		public Class<?>[] arguments() {
			return constructor.getParameterTypes();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FacetPrototype other = (FacetPrototype) obj;
			if (facetType == null) {
				if (other.facetType != null)
					return false;
			} else if (!facetType.equals(other.facetType))
				return false;
			return true;
		}

		public String name() {
			return getFacetName(facetType);
		}

		public TypeInformation newInstance() {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if (parameterTypes.length > 0) {
				Object val = "";
				if (parameterTypes[0] == Number.class) {
					val = 1;
				}
				if (parameterTypes[0] == Collection.class) {
					val = Arrays.asList("");
				}
				if (parameterTypes[0] == AbstractType.class) {
					val = BuiltIns.ANY;
				}
				if (parameterTypes[0] == boolean.class) {
					val = true;
				}
				try {
					return constructor.newInstance(val);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new IllegalStateException(facetType.getSimpleName());
				}
			}
			try {
				return constructor.newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new IllegalStateException();
			}
		}

		public boolean canBeAddedTo(AbstractType t) {
			return t.isSubTypeOf(this.emptyProto.requiredType());
		}

		public TypeInformation create(Object[] args) {
			try {
				return constructor.newInstance(args);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new IllegalStateException(e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	static Class<? extends TypeInformation>[] allRestrictions = new Class[] { MinLength.class, MaxLength.class,
			Minimum.class, Maximum.class, MinProperties.class, MaxProperties.class, MinItems.class, MaxItems.class,
			UniqueItems.class, Enum.class, Pattern.class,
			// type related restrictions
			ComponentShouldBeOfType.class, HasPropertyRestriction.class, PropertyIs.class, MapPropertyIs.class,
			AdditionalProperties.class, KnownPropertyRestricton.class };

	@SuppressWarnings("unchecked")
	static Class<? extends TypeInformation>[] metaInfo = new Class[] { Description.class,Format.class, DisplayName.class,
			Discriminator.class, DiscriminatorValue.class, Default.class, Example.class, XMLFacet.class, Abstract.class, Polymorphic.class };

	public static Collection<String> allRestrictionsNames() {
		Class<? extends TypeInformation>[] restr = allRestrictions;
		return names(restr);
	}

	public static Collection<String> allMetaNames() {
		Class<? extends TypeInformation>[] restr = metaInfo;
		return names(restr);
	}

	public static Collection<String> allFacetNames() {
		Class<? extends TypeInformation>[] restr = metaInfo;
		Collection<String> s = names(restr);
		s.addAll(names(allRestrictions));
		return s;
	}

	protected static Collection<String> names(Class<? extends TypeInformation>[] restr) {
		ArrayList<String> str = new ArrayList<>();
		for (Class<? extends TypeInformation> c : restr) {
			str.add(getFacetName(c));
		}
		return str;
	}

	/**
	 * 
	 * @param t0
	 * @return all facets potentially applyable to t0
	 */
	public static Collection<FacetPrototype> applyableTo(AbstractType t0) {
		ArrayList<FacetPrototype> rs = new ArrayList<>();
		for (FacetPrototype q : allPrototypes()) {
			if (q.canBeAddedTo(t0)) {
				rs.add(q);
			}
		}
		return rs;
	}
	
	/***
	 * 
	 * @return collection of all facet prototypes
	 */
	public static Collection<FacetPrototype> allPrototypes() {
		ArrayList<FacetPrototype> ps = new ArrayList<>();
		for (Class<? extends TypeInformation> t : allRestrictions) {
			ps.add(new FacetPrototype(t));
		}
		for (Class<? extends TypeInformation> t : metaInfo) {
			ps.add(new FacetPrototype(t));
		}
		return ps;
	}

	public static String getFacetName(Class<? extends TypeInformation> clazz) {
		final Name annotation = clazz.getAnnotation(Name.class);
		if (annotation == null) {
			final String nm = clazz.getSimpleName();
			return Character.toLowerCase(nm.charAt(0)) + nm.substring(1);
		}
		return annotation.value();
	}
}