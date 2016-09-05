package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

public class Description extends Facet<String> {

	public Description(String value) {
		super(value, false);
	}

	@Override
	public String facetName() {
		return "description";
	}


	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}
