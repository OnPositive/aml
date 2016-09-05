package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.TypeInformation;

public class FacetDeclaration extends TypeInformation {

	protected AbstractType facetType;
	protected String name;

	public FacetDeclaration(String name, AbstractType ts) {
		super(true);
		this.name = name;
		this.facetType = ts;
	}

	public String getName() {
		return name;
	}

	public AbstractType getType() {
		return facetType;
	}

	@Override
	public Status validate(ITypeRegistry registry) {
		if (ownerType.isAnonimous()) {
			return new Status(Status.ERROR, 0, "facet can not be declared in anonimous or final types");
		}
		if (facetType.isAnonimous()) {
			return facetType.validateMeta(null);
		}
		return Status.OK_STATUS;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
