package org.aml.typesystem.meta.restrictions;

public abstract class BooleanRestriction extends FacetRestriction<Boolean> {

	@Override
	public Boolean value() {
		return true;
	}
}
