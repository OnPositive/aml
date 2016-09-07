package org.aml.typesystem.values;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>ArrayImpl class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ArrayImpl implements IArray {

	protected ArrayList<Object> values = new ArrayList<>();

	/**
	 * <p>Constructor for ArrayImpl.</p>
	 */
	public ArrayImpl() {
	}

	/**
	 * <p>Constructor for ArrayImpl.</p>
	 *
	 * @param value a {@link java.util.List} object.
	 */
	public ArrayImpl(List<?> value) {
		for (Object o : value) {
			o = ObjectImpl.toRAML(o);
			this.values.add(o);
		}
	}

	/**
	 * <p>add.</p>
	 *
	 * @param vl a {@link java.lang.Object} object.
	 */
	public void add(Object vl) {
		values.add(vl);
	}

	/**
	 * <p>asList.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Object> asList() {
		return values;
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
	public Object item(int position) {
		return values.get(position);
	}

	/** {@inheritDoc} */
	@Override
	public int length() {
		return values.size();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return values.toString();
	}
}
