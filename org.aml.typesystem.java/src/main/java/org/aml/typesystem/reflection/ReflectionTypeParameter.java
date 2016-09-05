package org.aml.typesystem.reflection;

import java.lang.reflect.TypeVariable;

import org.aml.typesystem.ITypeParameter;

public class ReflectionTypeParameter implements ITypeParameter {
	
	public ReflectionTypeParameter(TypeVariable<?> element) {
		super();
		this.element = element;
	}

	protected TypeVariable<?> element;
	
	@Override
	public String getName() {
		return element.getName();
	}

}
