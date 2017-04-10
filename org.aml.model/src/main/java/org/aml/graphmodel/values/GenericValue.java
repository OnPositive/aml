package org.aml.graphmodel.values;

import org.aml.graphmodel.Value;
import org.aml.graphmodel.ValueKind;
import org.aml.typesystem.IType;

public class GenericValue implements Value{

	public final String name;
	public final IType shape;
	public final boolean required;
	
	public GenericValue(String name, IType shape, boolean required) {
		super();
		this.name = name;
		this.shape = shape;
		this.required = required;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean required() {
		return required;
	}

	@Override
	public IType shape() {
		return shape;
	}

	@Override
	public ValueKind kind() {
		return ValueKind.GENERIC;
	}

}
