package org.aml.raml2java;

import org.aml.typesystem.AbstractType;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public class InterfaceGenerator implements ITypeGenerator {

	private JavaWriter writer;

	public InterfaceGenerator(JavaWriter wr) {
		this.writer = wr;
	}

	@Override
	public JType define(AbstractType t) {
		JDefinedClass defineClass = writer.defineClass(t, ClassType.INTERFACE);
		for (AbstractType st:t.superTypes()){
			AbstractType superType = st;
			JType type = writer.getType(superType);
			defineClass._extends((JClass) type);
		}
		t.toPropertiesView().properties().forEach(p -> {
			String name = writer.propNameGenerator.name(p);
			JType propType = writer.getType(p.range(), false, false, p);
			//defineClass.field(JMod.PRIVATE, propType, name);
			defineClass.method(JMod.PUBLIC, propType,
					"get" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
			
			defineClass.method(JMod.PUBLIC, writer.getModel()._ref(void.class),
					(p.range().isBoolean() ? "is" : "set") + Character.toUpperCase(name.charAt(0)) + name.substring(1));
			
		});
		writer.annotate(defineClass, t);
		return defineClass;
	}

}
