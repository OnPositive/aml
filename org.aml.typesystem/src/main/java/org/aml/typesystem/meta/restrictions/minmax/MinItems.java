package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>MinItems class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class MinItems extends MinMaxRestriction {

	
	/**
	 * <p>Constructor for MinItems.</p>
	 *
	 * @param min a {@link java.lang.Number} object.
	 */
	public MinItems(Number min) {
		super(min, false, MaxItems.class, BuiltIns.ARRAY, true);

	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "array should have at least " + (int)doubleValue() + " items";
	}

	/** {@inheritDoc} */
	@Override
	protected Object extractValue(Object o) {
		return ObjectAccess.length(o);
	}


}
