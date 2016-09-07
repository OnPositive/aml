package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

/**
 * <p>Maximum class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Maximum extends MinMaxRestriction {

	
	/**
	 * <p>Constructor for Maximum.</p>
	 *
	 * @param max a {@link java.lang.Number} object.
	 */
	public Maximum(Number max) {
		super(max,true,Minimum.class,BuiltIns.NUMBER,false);
	}


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "value should be not more then " + this.doubleValue();
	}


	/** {@inheritDoc} */
	@Override
	protected Object extractValue(Object o) {
		return o;
	}

	
}
