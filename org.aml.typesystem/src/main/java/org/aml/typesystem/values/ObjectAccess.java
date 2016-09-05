package org.aml.typesystem.values;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class ObjectAccess {

	public static Object item(Object o, int i) {
		if (o instanceof IArray) {
			return ((IArray) o).item(i);
		}
		return null;
	}

	public static int length(Object o) {
		if (o instanceof IArray) {
			return ((IArray) o).length();
		}
		return 0;
	}

	public static Set<String> properties(Object o) {
		if (o instanceof IObject) {
			return ((IObject) o).keys();
		}
		return Collections.emptySet();
	}

	public static Object propertyValue(String name, Object o) {
		// here we need to access property value
		if (o == null) {
			return null;
		}
		if (o instanceof IObject) {
			final IObject z = (IObject) o;
			return z.getProperty(name);
		}
		try {
			final Field f = o.getClass().getField(name);
			return f.get(o);
		} catch (final Exception e) { // NOSONAR
			try {
				final Method m = o.getClass().getMethod("get" + name);
				return m.invoke(o);
			} catch (final Exception e1) { // NOSONAR
				return null;
			}
		}
	}

	public static Object value(Object o) {
		return o;
	}

	private ObjectAccess() {
	}

}
