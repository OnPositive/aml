package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

public class MaxLength extends MinMaxRestriction {

	
	public MaxLength(Number value) {
		super(value, true, MinLength.class, BuiltIns.STRING, true);
	}

	@Override
	public String toString() {
		return "string length should be not more then " + value;
	}

	@Override
	protected Object extractValue(Object o) {
		if (o instanceof String){
			return ((String) o).length();
		}
		return null;
	}

}
