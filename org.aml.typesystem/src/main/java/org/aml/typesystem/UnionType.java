package org.aml.typesystem;

import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.internal.ORRestricton;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;

public final class UnionType extends DerivedType {

	protected UnionType(String name, AbstractType... options) {
		super(name, options);
	}

	@Override
	protected TypeInformation createRestricton(AbstractRestricton[] rs) {
		return new ORRestricton(rs);
	}

	@Override
	public final Set<AbstractType> typeFamily() {
		final Set<AbstractType> allOptions = this.allOptions();
		final LinkedHashSet<AbstractType> result = new LinkedHashSet<>();
		for (final AbstractType z : allOptions) {
			result.addAll(z.typeFamily());
		}
		return result;
	}

	@Override
	public AbstractType noPolymorph() {
		return this;
	}
}
