package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.values.ObjectAccess;

public class MaxProperties extends MinMaxRestriction {

	public MaxProperties(Number max) {
		super(max,true,MinProperties.class,BuiltIns.OBJECT,true);
	}

	@Override
	public String toString() {
		return "object should have not more then " + doubleValue() + " properties";
	}

	@Override
	protected Object extractValue(Object o) {
		return ObjectAccess.properties(o).size();
	}

}
