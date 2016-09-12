package org.aml.raml2java;

import org.aml.typesystem.AbstractType;

import com.sun.codemodel.JType;

public interface ITypeGenerator {

	public JType define(AbstractType t);
}