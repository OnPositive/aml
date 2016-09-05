package org.aml.typesystem.values;

import java.util.ArrayList;
import java.util.List;

public class ArrayImpl implements IArray {

	protected ArrayList<Object> values = new ArrayList<>();

	public ArrayImpl() {
	}

	public ArrayImpl(List<?> value) {
		for (Object o : value) {
			o = ObjectImpl.toRAML(o);
			this.values.add(o);
		}
	}

	public void add(Object vl) {
		values.add(vl);
	}

	public List<Object> asList() {
		return values;
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
		final ArrayImpl other = (ArrayImpl) obj;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (values == null ? 0 : values.hashCode());
		return result;
	}

	@Override
	public Object item(int position) {
		return values.get(position);
	}

	@Override
	public int length() {
		return values.size();
	}

	@Override
	public String toString() {
		return values.toString();
	}
}
