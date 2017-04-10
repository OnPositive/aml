package org.aml.graphmodel.values;

import org.aml.graphmodel.IResourceModel;
import org.aml.graphmodel.ValueKind;
import org.aml.typesystem.IType;

public class ResourceRepresentation extends GenericValue {

	protected IResourceModel resource;

	public ResourceRepresentation(String name, IType shape, boolean required,IResourceModel model) {
		super(name, shape, required);
		this.resource=model;
	}

	public IResourceModel resource() {
		return this.resource;
	}

	@Override
	public ValueKind kind() {
		return ValueKind.REPRESENTATION;
	}
}