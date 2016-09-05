package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

public class Minimum extends MinMaxRestriction {

	public Minimum(Number min) {
		super(min,false,Maximum.class,BuiltIns.NUMBER,false);
	}


	@Override
	public String toString() {
		return "value should be at least: " + doubleValue();
	}


	@Override
	protected Object extractValue(Object o) {
		return o;
	}

	
}
