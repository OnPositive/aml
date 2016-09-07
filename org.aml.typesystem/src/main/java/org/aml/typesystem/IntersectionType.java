package org.aml.typesystem;

import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.internal.ANDRestricton;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>IntersectionType class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public final class IntersectionType extends DerivedType {

	/**
	 * <p>Constructor for IntersectionType.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param options a {@link org.aml.typesystem.AbstractType} object.
	 */
	public IntersectionType(String name, AbstractType... options) {
		super(name, options);
	}

	/** {@inheritDoc} */
	@Override
	protected TypeInformation createRestricton(AbstractRestricton[] rs) {
		return new ANDRestricton(rs);
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType noPolymorph() {
		return this;
	}
}
