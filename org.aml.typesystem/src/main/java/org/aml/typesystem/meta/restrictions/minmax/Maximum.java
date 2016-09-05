package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

public class Maximum extends MinMaxRestriction {

	
	public Maximum(Number max) {
		super(max,true,Minimum.class,BuiltIns.NUMBER,false);
	}


	@Override
	public String toString() {
		return "value should be not more then " + this.doubleValue();
	}


	@Override
	protected Object extractValue(Object o) {
		return o;
	}

	
}