package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

/**
 * <p>Minimum class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Minimum extends MinMaxRestriction {

	/**
	 * <p>Constructor for Minimum.</p>
	 *
	 * @param min a {@link java.lang.Number} object.
	 */
	public Minimum(Number min) {
		super(min,false,Maximum.class,BuiltIns.NUMBER,false);
	}


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "value should be at least: " + doubleValue();
	}


	/** {@inheritDoc} */
	@Override
	protected Object extractValue(Object o) {
		return o;
	}

	
}
