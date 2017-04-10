package org.aml.graphmodel.values;

import org.aml.graphmodel.IResourceModel;
import org.aml.graphmodel.ValueKind;
import org.aml.typesystem.IType;

public class ReferenceValue extends ResourceRepresentation{

	protected String path;
	
	public ReferenceValue(String name, IType shape, boolean required,IResourceModel on,String propertyPath) {
		super(name, shape, required,on);
		this.path=propertyPath;
	}
	
	public String path(){
		return this.path;
	}

	@Override
	public ValueKind kind() {
		return ValueKind.REFERENCE;
	}
}
