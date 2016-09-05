package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.values.ObjectAccess;

public class MinItems extends MinMaxRestriction {

	
	public MinItems(Number min) {
		super(min, false, MaxItems.class, BuiltIns.ARRAY, true);

	}

	@Override
	public String toString() {
		return "array should have at least " + (int)doubleValue() + " items";
	}

	@Override
	protected Object extractValue(Object o) {
		return ObjectAccess.length(o);
	}


}
