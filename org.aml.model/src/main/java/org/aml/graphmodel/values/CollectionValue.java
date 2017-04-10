package org.aml.graphmodel.values;

import org.aml.graphmodel.IResourceModel;
import org.aml.typesystem.IType;

public class CollectionValue extends ResourceRepresentation {

	protected final String path;

	public CollectionValue(String name, IType shape, boolean required, IResourceModel model, String path) {
		super(name, shape, required, model);
		this.path = path;
	}

	public String path() {
		return this.path;
	}

}
