package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.FacetRegistry;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.FacetRestriction;

public abstract class MinMaxRestriction extends FacetRestriction<Number> {

	protected Number value;
	protected boolean max;
	protected Class<? extends MinMaxRestriction> opposite;
	protected AbstractType requiredType;
	protected boolean intConstraint;

	public MinMaxRestriction(Number value, boolean max, Class<? extends MinMaxRestriction> opposite,
			AbstractType required, boolean intConstraint) {
		super();
		this.max = max;
		this.value = value;
		this.opposite = opposite;
		this.requiredType = required;
		this.intConstraint=intConstraint;
	}
	
	public boolean isIntConstraint(){
		return true;
	}

	@Override
	public final Status check(Object o) {
		o = extractValue(o);
		if (o instanceof Number) {
			if (isMax()) {
				if (doubleValue() < ((Number) o).doubleValue()) {
					return createError();
				}
			}
			else {
				if (doubleValue() > ((Number) o).doubleValue()) {
					return createError();
				}
			}
		}
		return Status.OK_STATUS;
	}

	@Override
	public Number value() {
		return this.value;
	}

	protected final boolean isMax() {
		return this.max;
	}

	protected abstract Object extractValue(Object o);

	protected final double doubleValue() {
		return this.value().doubleValue();
	}

	protected final String oppositeFacet() {
		return FacetRegistry.getFacetName(opposite);
	}

	protected Status createError() {
		return error(toString());
	}

	protected final double min(){
		if (intConstraint&&!max){
			return 0;
		}
		return Double.MIN_VALUE;
	}

	@Override
	protected String checkValue() {
		if (doubleValue() < min()) {
			return facetName() + " should be at least " + min();
		}
		return null;
	}

	@Override
	public final AbstractType requiredType() {
		return this.requiredType;
	}

	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction.facetName().equals(this.facetName())) {
			MinMaxRestriction mx = (MinMaxRestriction) restriction;
			if (mx.isMax() == this.isMax()) {
				if (isMax()) {
					if (this.doubleValue() < mx.doubleValue()) {
						return mx;
					} else {
						return this;
					}
				} else {
					if (this.doubleValue() > mx.doubleValue()) {
						return mx;
					} else {
						return this;
					}
				}
			}
		}
		if (restriction.facetName().equals(oppositeFacet())) {
			MinMaxRestriction mx = (MinMaxRestriction) restriction;
			if (this.isMax()) {
				if (mx.doubleValue() > this.doubleValue()) {
					return nothing(restriction);
				}
			} else {
				if (mx.doubleValue() < this.doubleValue()) {
					return nothing(restriction);
				}
			}
		}
		return null;
	}
	
	@Override
	public void setValue(Object vl) {
		if (vl instanceof String){
			try{
				if ((""+vl).indexOf(".")!=-1){
					vl=Double.parseDouble((String) vl);
				}
				else{
					vl=Long.parseLong(""+vl);
				}
			}catch(NumberFormatException e){
				vl=-1;
			}
		}
		this.value=(Number) vl;
	}

}
