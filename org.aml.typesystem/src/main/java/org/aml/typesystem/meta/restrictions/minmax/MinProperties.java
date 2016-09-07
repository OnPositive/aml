package org.aml.typesystem.meta.restrictions.minmax;

import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.values.ObjectAccess;

/**
 * <p>MinProperties class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class MinProperties extends MinMaxRestriction implements ISimpleFacet {

	/**
	 * <p>Constructor for MinProperties.</p>
	 *
	 * @param value a {@link java.lang.Number} object.
	 */
	public MinProperties(Number value) {
		super(value,false,MaxProperties.class,BuiltIns.OBJECT,true);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "object should have at least " + doubleValue() + " properties";
	}

	/** {@inheritDoc} */
	@Override
	protected Object extractValue(Object o) {
		return ObjectAccess.properties(o).size();
	}
}
