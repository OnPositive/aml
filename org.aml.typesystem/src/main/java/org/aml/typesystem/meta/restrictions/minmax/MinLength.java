package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;

/**
 * <p>MinLength class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class MinLength extends MinMaxRestriction {


	/**
	 * <p>Constructor for MinLength.</p>
	 *
	 * @param minLength a {@link java.lang.Number} object.
	 */
	public MinLength(Number minLength) {
		super(minLength,false,MaxLength.class,BuiltIns.STRING,true);
	}


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "string length should be not more then " + value;
	}


	/** {@inheritDoc} */
	@Override
	protected Object extractValue(Object o) {
		if (o instanceof String){
			String str=(String) o;
			return str.length();
		}
		return null;
	}
	
}
