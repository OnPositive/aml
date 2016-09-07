package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

/**
 * <p>MaxLength class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class MaxLength extends MinMaxRestriction {

	
	/**
	 * <p>Constructor for MaxLength.</p>
	 *
	 * @param value a {@link java.lang.Number} object.
	 */
	public MaxLength(Number value) {
		super(value, true, MinLength.class, BuiltIns.STRING, true);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "string length should be not more then " + value;
	}

	/** {@inheritDoc} */
	@Override
	protected Object extractValue(Object o) {
		if (o instanceof String){
			return ((String) o).length();
		}
		return null;
	}

}
