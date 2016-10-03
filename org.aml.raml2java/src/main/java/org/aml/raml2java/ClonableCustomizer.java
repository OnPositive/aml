package org.aml.raml2java;

import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;

public class ClonableCustomizer implements IClassCustomizer{

	@Override
	public void customize(ClassCustomizerParameters parameters) {
		parameters.clazz._implements(Cloneable.class);
		parameters.clazz.method(JMod.PUBLIC, parameters.clazz, "clone").body().add(new JStatement() {
			
			@Override
			public void state(JFormatter f) {
				f.p("try {");
				f.nl();
				f.p("return ("+parameters.clazz.name()+")super.clone();");
				f.nl();
				f.p("} catch (CloneNotSupportedException e){ throw new IllegalStateException(e);}");
				f.nl();
			}
		});
		
	}

	
}
