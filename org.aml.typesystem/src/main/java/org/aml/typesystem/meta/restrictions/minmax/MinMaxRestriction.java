package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.FacetRegistry;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.FacetRestriction;

/**
 * <p>Abstract MinMaxRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class MinMaxRestriction extends FacetRestriction<Number> {

	protected Number value;
	protected boolean max;
	protected Class<? extends MinMaxRestriction> opposite;
	protected AbstractType requiredType;
	protected boolean intConstraint;

	/**
	 * <p>Constructor for MinMaxRestriction.</p>
	 *
	 * @param value a {@link java.lang.Number} object.
	 * @param max a boolean.
	 * @param opposite a {@link java.lang.Class} object.
	 * @param required a {@link org.aml.typesystem.AbstractType} object.
	 * @param intConstraint a boolean.
	 */
	public MinMaxRestriction(Number value, boolean max, Class<? extends MinMaxRestriction> opposite,
			AbstractType required, boolean intConstraint) {
		super();
		this.max = max;
		this.value = value;
		this.opposite = opposite;
		this.requiredType = required;
		this.intConstraint=intConstraint;
	}
	
	/**
	 * <p>isIntConstraint.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isIntConstraint(){
		return true;
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public Number value() {
		return this.value;
	}

	/**
	 * <p>isMax.</p>
	 *
	 * @return a boolean.
	 */
	protected final boolean isMax() {
		return this.max;
	}

	/**
	 * <p>extractValue.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 */
	protected abstract Object extractValue(Object o);

	/**
	 * <p>doubleValue.</p>
	 *
	 * @return a double.
	 */
	protected final double doubleValue() {
		return this.value().doubleValue();
	}

	/**
	 * <p>oppositeFacet.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected final String oppositeFacet() {
		return FacetRegistry.getFacetName(opposite);
	}

	/**
	 * <p>createError.</p>
	 *
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	protected Status createError() {
		return error(toString());
	}

	/**
	 * <p>min.</p>
	 *
	 * @return a double.
	 */
	protected final double min(){
		if (intConstraint&&!max){
			return 0;
		}
		return Double.MIN_VALUE;
	}

	/** {@inheritDoc} */
	@Override
	protected String checkValue() {
		if (doubleValue() < min()) {
			return facetName() + " should be at least " + min();
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public final AbstractType requiredType() {
		return this.requiredType;
	}

	/** {@inheritDoc} */
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
	
	/** {@inheritDoc} */
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
