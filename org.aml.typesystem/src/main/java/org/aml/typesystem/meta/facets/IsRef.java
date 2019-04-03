package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public class IsRef extends Facet<Boolean>{

	public IsRef(Boolean value) {
		super(value);
	}

	@Override
	public String facetName() {
		return "isRef";
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
