package org.aml.raml2java;

import org.aml.java.mapping.comparable;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class ComparableCustomizer implements IClassCustomizer{

	@Override
	public void customize(ClassCustomizerParameters parameters) {
		parameters.clazz._implements(parameters.writer.getModel().ref(Comparable.class).narrow(parameters.clazz));
		JMethod method = parameters.clazz.method(JMod.PUBLIC, int.class, "compareTo");
		method.param(parameters.clazz, "another");
		method.body()._return(createCompare(parameters));
		
		
	}

	private JExpression createCompare(ClassCustomizerParameters parameters) {
		parameters.clazz.fields().get("value");
		String value=parameters.type.annotation(comparable.class, true).value();
		return JExpr.direct("this."+value+"compareTo(another."+value+")");
	}

	
}
