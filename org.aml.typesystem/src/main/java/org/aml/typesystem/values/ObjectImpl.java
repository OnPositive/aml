package org.aml.typesystem.values;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>ObjectImpl class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public final class ObjectImpl implements IObject {

	/**
	 * <p>toRAML.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	@SuppressWarnings("unchecked")
	public static final Object toRAML(Object o) {
		if (o instanceof Map) {
			o = new ObjectImpl((Map<String, Object>) o);
		} else if (o instanceof List) {
			o = new ArrayImpl((List<?>) o);
		}
		return o;
	}

	protected LinkedHashMap<String, Object> values = new LinkedHashMap<>();

	/**
	 * <p>Constructor for ObjectImpl.</p>
	 */
	public ObjectImpl() {
	}
	
	/**
	 * <p>getMap.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String,Object>getMap(){
		return values;
	}

	/**
	 * <p>Constructor for ObjectImpl.</p>
	 *
	 * @param value a {@link java.util.Map} object.
	 */
	public ObjectImpl(Map<String, Object> value) {
		for (final String s : value.keySet()) {
			this.values.put(s, toRAML(value.get(s)));
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ObjectImpl other = (ObjectImpl) obj;
		if (values == null) {
			if (other.values != null) {
				return false;
			}
		} else if (!values.equals(other.values)) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return values.get(name);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (values == null ? 0 : values.hashCode());
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return values.keySet();
	}

	/**
	 * <p>putProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public void putProperty(String name, Object value) {
		this.values.put(name, value);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void remove(String string) {
		values.remove(string);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return values.toString();
	}
}
