package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.values.ObjectAccess;

public class MinProperties extends MinMaxRestriction implements ISimpleFacet {

	public MinProperties(Number value) {
		super(value,false,MaxProperties.class,BuiltIns.OBJECT,true);
	}

	@Override
	public String toString() {
		return "object should have at least " + doubleValue() + " properties";
	}

	@Override
	protected Object extractValue(Object o) {
		return ObjectAccess.properties(o).size();
	}
}