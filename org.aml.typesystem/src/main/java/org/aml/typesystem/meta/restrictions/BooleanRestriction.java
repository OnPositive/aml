package org.aml.typesystem.meta.restrictions;

/**
 * <p>Abstract BooleanRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class BooleanRestriction extends FacetRestriction<Boolean> {

	/** {@inheritDoc} */
	@Override
	public Boolean value() {
		return true;
	}
}
