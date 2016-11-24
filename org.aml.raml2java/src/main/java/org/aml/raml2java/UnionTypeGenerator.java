package org.aml.raml2java;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.acbuilder.AcScheme;
import org.aml.typesystem.acbuilder.AcSchemeBuilder;
import org.aml.typesystem.beans.IProperty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.JsonAdapter;
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
		AcSchemeBuilder acSchemeBuilder = new AcSchemeBuilder();
		Set<AbstractType> typeFamily = acSchemeBuilder.extendedUnionTypeFamily(t);
		for (AbstractType option:typeFamily){
			generateProperty(defineClass, option,typeFamily);
		}
		
		writer.annotate(defineClass, t);
		
		if (writer.getConfig().isGsonSupport()){
			AcScheme build = acSchemeBuilder.build(t);
			String generateAdapter = new GsonAcElementWriter().generateAdapter(defineClass.getPackage().name(),build, defineClass.name(), writer);
			String adapterName = defineClass.fullName()+"Adapter";
			String qName = defineClass.name()+"Adapter.java";
			writer.getModel()._package(defineClass.getPackage().name()).addResourceFile(new StringResourceFile(qName, generateAdapter));
			defineClass.annotate(JsonAdapter.class).param("value", JExpr.dotclass(writer.getModel().directClass(adapterName)));
		}
		if (writer.getConfig().isJacksonSupport()){
			AcScheme build = acSchemeBuilder.build(t);
			String generateAdapter = new JacksonDeserializerWriter().generateAdapter(defineClass.getPackage().name(),build, defineClass.name(), writer);
			String adapterName = defineClass.fullName()+"Deserializer";
			String qName = defineClass.name()+"Deserializer.java";
			writer.getModel()._package(defineClass.getPackage().name()).addResourceFile(new StringResourceFile(qName, generateAdapter));
			defineClass.annotate(JsonDeserialize.class).param("using", JExpr.dotclass(writer.getModel().directClass(adapterName)));
			
			String writerCode = new JacksonSerializerWriter().generateAdapter(defineClass.getPackage().name(),build, defineClass.name(), writer);
			adapterName = defineClass.fullName()+"Serializer";
			qName = defineClass.name()+"Serializer.java";
			writer.getModel()._package(defineClass.getPackage().name()).addResourceFile(new StringResourceFile(qName,writerCode));
			defineClass.annotate(JsonSerialize.class).param("using", JExpr.dotclass(writer.getModel().directClass(adapterName)));
		}
		writer.runCustomizers(new ClassCustomizerParameters(writer, defineClass, t, new ArrayList<>()));
		return defineClass;
	}

	private void generateProperty(JDefinedClass defineClass, AbstractType option,Set<AbstractType>allOptions) {
		String oname = option.name();
		String gset=writer.escape(oname);
		String name = writer.escape(oname.toLowerCase());
		JType propType = writer.getType(option, false, false, null);
		if (propType.isPrimitive()){
			propType=propType.boxify();
		}
		defineClass.field(JMod.PRIVATE, propType, name);
		JMethod get = defineClass.method(JMod.PUBLIC, propType,
				"get" + Character.toUpperCase(gset.charAt(0)) + gset.substring(1));
		get.body()._return(JExpr.ref(name));
		writer.annotate(get, option);
		JMethod set = defineClass.method(JMod.PUBLIC, writer.getModel()._ref(void.class),
				(option.isBoolean() ? "is" : "set") + Character.toUpperCase(gset.charAt(0)) + gset.substring(1));
		set.param(propType, "value");
		set.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("this." + name + "=value;");
				f.nl();
				for(AbstractType other:allOptions){
					if (other!=option){
						String oname = other.name();						
						String name = writer.escape(oname.toLowerCase());
						f.p("this." + name + "=null;");
						f.nl();
					}
				}
			}
		});		
	}

}
