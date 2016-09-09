package org.aml.typesystem.meta.restrictions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.aml.typesystem.meta.restrictions.minmax.MaxItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxLength;
import org.aml.typesystem.meta.restrictions.minmax.MaxProperties;
import org.aml.typesystem.meta.restrictions.minmax.Maximum;
import org.aml.typesystem.meta.restrictions.minmax.MinItems;
import org.aml.typesystem.meta.restrictions.minmax.MinLength;
import org.aml.typesystem.meta.restrictions.minmax.MinProperties;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;

/**
 * <p>RestrictionsList class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RestrictionsList {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Class<FacetRestriction>[] allRestrictions = new Class[] { UniqueItems.class, Maximum.class,
			MaxItems.class, MaxLength.class, MaxProperties.class, Minimum.class, MinItems.class, MinLength.class,
			MinProperties.class, Pattern.class, Enum.class, };

	/**
	 * <p>build.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link org.aml.typesystem.meta.restrictions.FacetRestriction} object.
	 */
	@SuppressWarnings("rawtypes")
	public static FacetRestriction build(String name, Object value) {

		final String className = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		Class<FacetRestriction> fr = null;
		for (final Class<FacetRestriction> c : allRestrictions) {
			if (c.getSimpleName().equals(className)) { // NOSONAR
				fr = c;
				break;
			}
		}
		if (fr == null) {
			return null;
		}
		final Constructor<?> constructor = fr.getDeclaredConstructors()[0];
		final Class<?> arg = constructor.getParameterTypes()[0];
		final Object trvalue = fixArg(value, arg);
		try {
			return (FacetRestriction) constructor.newInstance(trvalue);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	private static Object fixArg(Object value, Class<?> arg) {
		if (arg == int.class) {
			value = Integer.parseInt((String) value); // NOSONAR
		}
		if (arg == boolean.class) {
			value = Boolean.parseBoolean((String) value); // NOSONAR
		}
		if (arg == double.class) {
			value = Double.parseDouble((String) value); // NOSONAR
		}
		if (arg == Number.class) {
			try{
			value = Double.parseDouble((String) value.toString()); // NOSONAR
			} catch (NumberFormatException e){
				return 0;
			}
		}
		return value;
	}

	/**
	 * <p>getArrayRestrictions.</p>
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	public static Class<?>[] getArrayRestrictions() {
		return new Class[] { MinItems.class, MaxItems.class, UniqueItems.class };
	}

	/**
	 * <p>getNumberRestrictions.</p>
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	public static Class<?>[] getNumberRestrictions() {
		return new Class[] { Maximum.class, Minimum.class, Enum.class };
	}

	/**
	 * <p>getObjectRestrictions.</p>
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	public static Class<?>[] getObjectRestrictions() {
		return new Class[] { MinProperties.class, MaxProperties.class };
	}

	/**
	 * <p>getStringRestrictions.</p>
	 *
	 * @return an array of {@link java.lang.Class} objects.
	 */
	public static Class<?>[] getStringRestrictions() {
		return new Class[] { MinLength.class, MaxLength.class, Enum.class, Pattern.class };
	}

	private RestrictionsList() {
	}
}
