package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.facets.Facet;

public class DefaultPropertyMeta extends Facet<String>{

	public DefaultPropertyMeta(String value) {
		super(value);
	}

	@Override
	public String facetName() {
		return "defaultProperty";
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
