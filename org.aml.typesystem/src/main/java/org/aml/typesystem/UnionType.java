package org.aml.typesystem;

import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.internal.ORRestricton;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

/**
 * <p>UnionType class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public final class UnionType extends DerivedType {

	/**
	 * <p>Constructor for UnionType.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param options a {@link org.aml.typesystem.AbstractType} object.
	 */
	protected UnionType(String name, AbstractType... options) {
		super(name, options);
	}

	/** {@inheritDoc} */
	@Override
	protected TypeInformation createRestricton(AbstractRestricton[] rs) {
		return new ORRestricton(rs);
	}

	/** {@inheritDoc} */
	@Override
	public final Set<AbstractType> typeFamily() {
		final Set<AbstractType> allOptions = this.allOptions();
		final LinkedHashSet<AbstractType> result = new LinkedHashSet<>();
		for (final AbstractType z : allOptions) {
			result.addAll(z.typeFamily());
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType noPolymorph() {
		return this;
	}
	public Set<AbstractType> unionTypeFamily() {
		Set<AbstractType> unionTypeFamily = super.unionTypeFamily();
		unionTypeFamily.addAll(this.options());
		return unionTypeFamily;
	}
}
