package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>MaxItems class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class MaxItems extends MinMaxRestriction {

	/**
	 * <p>Constructor for MaxItems.</p>
	 *
	 * @param max a {@link java.lang.Number} object.
	 */
	public MaxItems(Number max) {
		super(max, true, MinItems.class, BuiltIns.ARRAY, true);
	}

	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "array should have not more then " + (int)doubleValue() + " items";
	}

	/** {@inheritDoc} */
	@Override
	protected Object extractValue(Object o) {
		return ObjectAccess.length(o);
	}
	
}
