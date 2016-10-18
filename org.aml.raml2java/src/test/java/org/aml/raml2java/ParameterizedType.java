package org.aml.raml2java;

import java.lang.reflect.Type;

public class ParameterizedType implements java.lang.reflect.ParameterizedType{

	final Class<?>raw;
	final Type[] arg;
	public ParameterizedType(Class<?> raw, Class<?> arg) {
		super();
		this.raw = raw;
		this.arg = new Type[]{arg};
	}

	
	
	@Override
	public Type[] getActualTypeArguments() {
		return arg;
	}

	@Override
	public Type getRawType() {
		return raw;
	}

	@Override
	public Type getOwnerType() {
		return null;
	}

}
