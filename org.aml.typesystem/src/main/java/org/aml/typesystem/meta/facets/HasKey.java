package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public class HasKey extends Facet<Boolean>{

	public HasKey(Boolean value) {
		super(value);
	}

	@Override
	public String facetName() {
		return "hasKey";
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
