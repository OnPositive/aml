package org.aml.raml2java;

import java.util.Set;

import org.aml.typesystem.AbstractType;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;

public class UnionTypeGenerator implements ITypeGenerator {

	private JavaWriter writer;

	public UnionTypeGenerator(JavaWriter wr) {
		this.writer = wr;

	}

	@Override
	public JType define(AbstractType t) {
		JDefinedClass defineClass = writer.defineClass(t, ClassType.CLASS);
		Set<AbstractType> typeFamily = t.typeFamily();
		for (AbstractType option:typeFamily){
			generateProperty(defineClass, option,typeFamily);
		}
		writer.annotate(defineClass, t);
		return defineClass;
	}

	private void generateProperty(JDefinedClass defineClass, AbstractType option,Set<AbstractType>allOptions) {
		String name = writer.escape(option.name().toLowerCase());
		JType propType = writer.getType(option, false, false, null);
		defineClass.field(JMod.PRIVATE, propType, name);
		JMethod get = defineClass.method(JMod.PUBLIC, propType,
				"get" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
		get.body()._return(JExpr.ref(name));
		writer.annotate(get, option);
		JMethod set = defineClass.method(JMod.PUBLIC, writer.getModel()._ref(void.class),
				(option.isBoolean() ? "is" : "set") + Character.toUpperCase(name.charAt(0)) + name.substring(1));
		set.param(propType, "value");
		set.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("this." + name + "=value;");
				f.nl();
				for(AbstractType other:allOptions){
					if (other!=option){
						String name = writer.escape(other.name().toLowerCase());
						f.p("this." + name + "=null;");
						f.nl();
					}
				}
			}
		});
	}

}
