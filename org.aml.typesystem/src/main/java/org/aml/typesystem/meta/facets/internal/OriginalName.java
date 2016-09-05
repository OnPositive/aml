package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.facets.Facet;

public class OriginalName extends Facet<String>{

	public OriginalName(String value) {
		super(value);
	}

	@Override
	public String facetName() {
		return "name";
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
