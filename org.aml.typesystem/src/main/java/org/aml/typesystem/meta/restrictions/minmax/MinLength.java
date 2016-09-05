package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

public class MinLength extends MinMaxRestriction {


	public MinLength(Number minLength) {
		super(minLength,false,MaxLength.class,BuiltIns.STRING,true);
	}


	@Override
	public String toString() {
		return "string length should be not more then " + value;
	}


	@Override
	protected Object extractValue(Object o) {
		if (o instanceof String){
			String str=(String) o;
			return str.length();
		}
		return null;
	}
	
}
