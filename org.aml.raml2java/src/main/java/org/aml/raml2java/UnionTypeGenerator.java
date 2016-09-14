package org.aml.raml2java;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.acbuilder.AcScheme;
import org.aml.typesystem.acbuilder.AcSchemeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.JsonAdapter;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JResourceFile;
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
		if (writer.getConfig().isGsonSupport()){
			AcScheme build = new AcSchemeBuilder().build(t);
			String generateAdapter = new GsonAcElementWriter().generateAdapter(defineClass.getPackage().name(),build, defineClass.name(), writer);
			String adapterName = defineClass.fullName()+"Adapter";
			writer.getModel()._package(defineClass.getPackage().name()).addResourceFile(new JResourceFile(defineClass.name()+"Adapter.java") {
				
				@Override
				protected void build(OutputStream os) throws IOException {
					os.write(generateAdapter.getBytes("UTF-8"));
				}
			});
			defineClass.annotate(JsonAdapter.class).param("value", JExpr.dotclass(writer.getModel().directClass(adapterName)));
		}
		if (writer.getConfig().isJacksonSupport()){
			AcScheme build = new AcSchemeBuilder().build(t);
			String generateAdapter = new JacksonDeserializerWriter().generateAdapter(defineClass.getPackage().name(),build, defineClass.name(), writer);
			String adapterName = defineClass.fullName()+"Deserializer";
			writer.getModel()._package(defineClass.getPackage().name()).addResourceFile(new JResourceFile(defineClass.name()+"Deserializer.java") {
				
				@Override
				protected void build(OutputStream os) throws IOException {
					os.write(generateAdapter.getBytes("UTF-8"));
				}
			});
			defineClass.annotate(JsonDeserialize.class).param("using", JExpr.dotclass(writer.getModel().directClass(adapterName)));
			
			String writerCode = new JacksonSerializerWriter().generateAdapter(defineClass.getPackage().name(),build, defineClass.name(), writer);
			adapterName = defineClass.fullName()+"Serializer";
			writer.getModel()._package(defineClass.getPackage().name()).addResourceFile(new JResourceFile(defineClass.name()+"Serializer.java") {
				
				@Override
				protected void build(OutputStream os) throws IOException {
					os.write(writerCode.getBytes("UTF-8"));
				}
			});
			defineClass.annotate(JsonSerialize.class).param("using", JExpr.dotclass(writer.getModel().directClass(adapterName)));
		}
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
