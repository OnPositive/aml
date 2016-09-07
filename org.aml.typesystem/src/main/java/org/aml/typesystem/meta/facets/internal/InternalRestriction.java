package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>Abstract InternalRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class InternalRestriction extends AbstractRestricton {

	
	/** {@inheritDoc} */
	@Override
	public final AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}
