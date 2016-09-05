package org.aml.typesystem.values;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ObjectImpl implements IObject {

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

	public ObjectImpl() {
	}
	
	public Map<String,Object>getMap(){
		return values;
	}

	public ObjectImpl(Map<String, Object> value) {
		for (final String s : value.keySet()) {
			this.values.put(s, toRAML(value.get(s)));
		}
	}

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

	@Override
	public Object getProperty(String name) {
		return values.get(name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (values == null ? 0 : values.hashCode());
		return result;
	}

	@Override
	public Set<String> keys() {
		return values.keySet();
	}

	public void putProperty(String name, Object value) {
		this.values.put(name, value);
	}

	public void remove(String string) {
		values.remove(string);
	}

	@Override
	public String toString() {
		return values.toString();
	}
}