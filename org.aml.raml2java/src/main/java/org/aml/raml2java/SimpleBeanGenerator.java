package org.aml.raml2java;

import java.util.Set;

import org.aml.raml2java.JavaGenerationConfig.MultipleInheritanceStrategy;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.IProperty;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;

public class SimpleBeanGenerator implements ITypeGenerator {

	private JavaWriter writer;

	public SimpleBeanGenerator(JavaWriter wr) {
		this.writer = wr;

	}

	@Override
	public JType define(AbstractType t) {
		JDefinedClass defineClass = writer.defineClass(t, ClassType.CLASS);
		AbstractType superType = t.superType();
		if (superType != null) {
			JType type = writer.getType(superType);
			defineClass._extends((JClass) type);
			t.toPropertiesView().properties().forEach(p -> {
				generateProperty(defineClass, p);
			});
		} else {
			if (this.writer.getConfig().multipleInheritance == MultipleInheritanceStrategy.MIX_IN) {
				AbstractType primarySuper=null;
				int pcount=0;
				for (AbstractType st:t.superTypes()){
				  if (primarySuper==null){
					  primarySuper=st;
				  }
				  if (st.propertySet().size()>pcount){
					  pcount=st.propertySet().size();
					  primarySuper=st;
				  }
				}
				Set<String>inheritedPropeties=primarySuper.propertySet();
				JType type = writer.getType(primarySuper);
				defineClass._extends((JClass) type);				
				t.toPropertiesView().allProperties().forEach(p -> {
					if(!inheritedPropeties.contains(p.id())){
						generateProperty(defineClass, p);
					}
				});	
			} else {
				t.toPropertiesView().allProperties().forEach(p -> {
					generateProperty(defineClass, p);
				});
			}
		}
		writer.annotate(defineClass, t);
		return defineClass;
	}

	private void generateProperty(JDefinedClass defineClass, IProperty p) {
		String name = writer.propNameGenerator.name(p);
		JType propType = writer.getType(p.range(), false, false, p);
		defineClass.field(JMod.PRIVATE, propType, name);
		JMethod get = defineClass.method(JMod.PUBLIC, propType,
				"get" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
		get.body()._return(JExpr.ref(name));
		writer.annotate(get, p.range());
		JMethod set = defineClass.method(JMod.PUBLIC, writer.getModel()._ref(void.class),
				(p.range().isBoolean() ? "is" : "set") + Character.toUpperCase(name.charAt(0)) + name.substring(1));
		set.param(propType, "value");
		set.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("this." + name + "=value;");
				f.nl();
			}
		});
	}

}
