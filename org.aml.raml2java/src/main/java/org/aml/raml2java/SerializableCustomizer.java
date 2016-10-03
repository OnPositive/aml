package org.aml.raml2java;

import java.io.Serializable;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMod;

public class SerializableCustomizer implements IClassCustomizer{

	@Override
	public void customize(ClassCustomizerParameters parameters) {
		parameters.clazz._implements(Serializable.class);
		parameters.clazz.field(JMod.FINAL|JMod.STATIC|JMod.PRIVATE, long.class, "serialVersionUID").init(JExpr.lit(1L));
	}

	
}
