package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.values.ObjectAccess;

public class MaxItems extends MinMaxRestriction {

	public MaxItems(Number max) {
		super(max, true, MinItems.class, BuiltIns.ARRAY, true);
	}

	
	@Override
	public String toString() {
		return "array should have not more then " + (int)doubleValue() + " items";
	}

	@Override
	protected Object extractValue(Object o) {
		return ObjectAccess.length(o);
	}
	
}
