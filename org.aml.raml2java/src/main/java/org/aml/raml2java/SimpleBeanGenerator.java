package org.aml.raml2java;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.aml.raml2java.JavaGenerationConfig.MultipleInheritanceStrategy;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.IXMLHints;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.XMLFacet;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.raml.v2.internal.utils.StreamUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JArray;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;

public class SimpleBeanGenerator implements ITypeGenerator {

	private JavaWriter writer;
	private boolean booleanAsIs = false;

	public SimpleBeanGenerator(JavaWriter wr) {
		this.writer = wr;

	}

	static class Extras {
		ArrayList<String> patterns = new ArrayList<>();
		ArrayList<String> fieldNames = new ArrayList<>();
		ArrayList<JType> types = new ArrayList<>();
		boolean needPropertyOverrides;

		ArrayList<PropertyCustomizerParameters> params = new ArrayList<>();
	}

	@Override
	public JType define(AbstractType t) {
		JDefinedClass defineClass = writer.defineClass(t, ClassType.CLASS);
		AbstractType superType = t.superType();
		boolean hasAdditionalOrMap = false;
		boolean hasMap = false;
		
		Extras ext = new Extras();
		if (superType != null) {
			JType type = writer.getType(superType);
			defineClass._extends((JClass) type);
			for (IProperty p : t.toPropertiesView().properties()) {
				generateProperty(defineClass, p, ext);
				if (p.isAdditional() || p.isMap()) {
					hasAdditionalOrMap = true;
					hasMap |= p.isMap();

				}
			}
			;
		} else {
			if (this.writer.getConfig().multipleInheritance == MultipleInheritanceStrategy.MIX_IN) {
				AbstractType primarySuper = null;
				int pcount = 0;
				for (AbstractType st : t.superTypes()) {
					if (primarySuper == null) {
						primarySuper = st;
					}
					if (st.propertySet().size() > pcount) {
						pcount = st.propertySet().size();
						primarySuper = st;
					}
				}
				Set<String> inheritedPropeties = primarySuper.propertySet();
				JType type = writer.getType(primarySuper);
				defineClass._extends((JClass) type);
				for (IProperty p : t.toPropertiesView().allProperties()) {
					if (!inheritedPropeties.contains(p.id())) {
						generateProperty(defineClass, p, ext);
						if (p.isAdditional() || p.isMap()) {
							hasAdditionalOrMap = true;
							hasMap |= p.isMap();
						}
					}
				}
				;
			} else {
				for (IProperty p : t.toPropertiesView().allProperties()) {
					generateProperty(defineClass, p, ext);
					if (p.isAdditional() || p.isMap()) {
						hasAdditionalOrMap = true;
						hasMap |= p.isMap();
					}
				}
				;
			}
		}
		writer.annotate(defineClass, t);
		addExtraInfoForPatternAndAdditionals(defineClass, hasAdditionalOrMap, ext, hasMap, t);
		
		return defineClass;
	}

	private void addExtraInfoForPatternAndAdditionals(JDefinedClass defineClass, boolean hasAdditionalOrMap, Extras ext,
			boolean hasMap, AbstractType type) {
		ClassCustomizerParameters cp = new ClassCustomizerParameters(writer, defineClass, type, ext.params);
		writer.runCustomizers(cp);
		if (writer.getConfig().isJaxbSupport()) {

			defineClass.annotate(XmlAccessorType.class).param("value", XmlAccessType.PROPERTY);

		}
		if (hasAdditionalOrMap || ext.needPropertyOverrides) {

			if (writer.getConfig().isGsonSupport()) {
				if (!defineClass.getPackage().hasResourceFile("PatternAndAdditionalTypeAdapterFactory.java")) {
					String string = StreamUtils.toString(SimpleBeanGenerator.class
							.getResourceAsStream("/PatternAndAdditionalTypeAdapterFactory.tpl"));
					string = string.replace("{packageName}", defineClass.getPackage().name());
					defineClass.getPackage().addResourceFile(
							new StringResourceFile("PatternAndAdditionalTypeAdapterFactory.java", string));
				}
				if (!defineClass.getPackage().hasResourceFile("PatternAndAdditionalTypeAdapter.java")) {
					String string = StreamUtils.toString(
							SimpleBeanGenerator.class.getResourceAsStream("/PatternAndAdditionalTypeAdapter.tpl"));
					string = string.replace("{packageName}", defineClass.getPackage().name());
					defineClass.getPackage()
							.addResourceFile(new StringResourceFile("PatternAndAdditionalTypeAdapter.java", string));
				}
				defineClass.annotate(JsonAdapter.class).param("value",
						JExpr.dotclass(writer.getModel().directClass("PatternAndAdditionalTypeAdapterFactory")));
				addPatternInfo(defineClass, ext);
			}
			if (writer.getConfig().isJacksonSupport() && hasAdditionalOrMap) {
				if (!defineClass.getPackage().hasResourceFile("MapAndAdditionalDeserializer.java")) {
					String string = StreamUtils.toString(
							SimpleBeanGenerator.class.getResourceAsStream("/MapAndAdditionalDeserializer.tpl"));
					string = string.replace("{packageName}", defineClass.getPackage().name());
					defineClass.getPackage()
							.addResourceFile(new StringResourceFile("MapAndAdditionalDeserializer.java", string));
				}
				if (!defineClass.getPackage().hasResourceFile("MapAndAdditionalSerializer.java")) {
					String string = StreamUtils
							.toString(SimpleBeanGenerator.class.getResourceAsStream("/MapAndAdditionalSerializer.tpl"));
					string = string.replace("{packageName}", defineClass.getPackage().name());
					defineClass.getPackage()
							.addResourceFile(new StringResourceFile("MapAndAdditionalSerializer.java", string));
				}
				try {
					JDefinedClass deserializer = defineClass.getPackage()._class(defineClass.name() + "Deserializer");
					deserializer._extends(writer.getModel()
							.directClass(defineClass.getPackage().name() + ".MapAndAdditionalDeserializer"));
					deserializer.constructor(JMod.PUBLIC).body().invoke("super").arg(JExpr.dotclass(defineClass));
					defineClass.annotate(JsonDeserialize.class).param("using", JExpr.dotclass(deserializer));
					addPatternInfo(deserializer, ext);
				} catch (JClassAlreadyExistsException e) {
					throw new IllegalStateException(e);
				}
				try {
					JDefinedClass deserializer = defineClass.getPackage()._class(defineClass.name() + "Serializer");
					deserializer._extends(writer.getModel()
							.directClass(defineClass.getPackage().name() + ".MapAndAdditionalSerializer"));
					deserializer.constructor(JMod.PUBLIC).body().invoke("super").arg(JExpr.dotclass(defineClass));
					defineClass.annotate(JsonSerialize.class).param("using", JExpr.dotclass(deserializer));
					addPatternInfo(deserializer, ext);
				} catch (JClassAlreadyExistsException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}

	private void addPatternInfo(JDefinedClass defineClass, Extras ext) {
		JArray newArray = JExpr.newArray(writer.getModel()._ref(String.class));
		defineClass.field(JMod.STATIC, String[].class, "$PATTERNS").init(newArray);
		for (String s : ext.patterns) {
			newArray.add(JExpr.lit(s));
		}
		newArray = JExpr.newArray(writer.getModel()._ref(String.class));
		defineClass.field(JMod.STATIC, String[].class, "$FIELDS").init(newArray);
		for (String s : ext.fieldNames) {
			newArray.add(JExpr.lit(s));
		}
		newArray = JExpr.newArray(writer.getModel()._ref(Class.class));
		defineClass.field(JMod.STATIC, Class[].class, "$CLASSES").init(newArray);
		for (JType ct : ext.types) {
			newArray.add(JExpr.direct(ct.fullName() + ".class"));
		}
	}

	private void generateProperty(JDefinedClass defineClass, IProperty p, Extras ext) {
		String name = writer.propNameGenerator.name(p);
		JType propType = writer.getType(p.range(), false, false, p);
		JExpression initExpr = null;
		if (p.isMap() || p.isAdditional()) {
			JType oType = propType;
			propType = ((JClass) writer.getModel()._ref(Map.class)).narrow(String.class).narrow(propType);
			initExpr = JExpr
					._new(((JClass) writer.getModel()._ref(LinkedHashMap.class)).narrow(String.class).narrow(oType));
			ext.fieldNames.add(name);
			ext.patterns.add(p.id().substring(1, p.id().length() - 1));
			ext.types.add(oType);

		}

		JClass _extends = defineClass._extends();
		boolean needField = true;
		JType ts = null;
		if (_extends != null && _extends instanceof JDefinedClass) {
			JDefinedClass ee = (JDefinedClass) _extends;
			JFieldVar jFieldVar = ee.fields().get(name);
			if (jFieldVar != null) {
				needField = false;
				jFieldVar.mods().setProtected();
				ts = jFieldVar.type();
			}

		}
		JFieldVar field = null;
		if (needField) {
			field = defineClass.field(JMod.PRIVATE, propType, name);
			if (p.range().isFile()&&writer.getConfig().isGsonSupport()){
				if (!defineClass.getPackage().hasResourceFile("ByteArrayToBase64TypeAdapter.java")) {
					String string = StreamUtils.toString(
							SimpleBeanGenerator.class.getResourceAsStream("/ByteArrayToBase64TypeAdapter.tpl"));
					string = string.replace("{packageName}", defineClass.getPackage().name());
					defineClass.getPackage()
							.addResourceFile(new StringResourceFile("ByteArrayToBase64TypeAdapter.java", string));
					field.annotate(JsonAdapter.class).param("value", writer.getModel().directClass(defineClass.getPackage().name()+".ByteArrayToBase64TypeAdapter"));
					
				}
			}
			if (writer.getConfig().isGsonSupport()) {
				if (p.isMap() || p.isAdditional()) {
					field.annotate(Expose.class).param("serialize", false).param("deserialize", false);
				}
			}
			if (writer.getConfig().isJacksonSupport()) {
				if (p.isMap() || p.isAdditional()) {
					field.annotate(JsonIgnore.class);
				}
			}
			if (initExpr != null) {
				field.init(initExpr);
			}
		}
		JMethod get = defineClass.method(JMod.PUBLIC, propType,
				"get" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
		if (p.range().hasDirectMeta(Description.class)){
			get.javadoc().add(p.range().oneMeta(Description.class).value());
		}
		JExpression ref = JExpr.ref(name);
		if (ts != null && !ts.equals(propType)) {
			ref = JExpr.cast(propType, ref);
			if (writer.getConfig().isGsonSupport()) {
				ext.needPropertyOverrides = true;
			}

		}
		get.body()._return(ref);
		writer.annotate(get, p.range());
		JMethod set = defineClass.method(JMod.PUBLIC, writer.getModel()._ref(void.class),
				((p.range().isBoolean() && this.booleanAsIs) ? "is" : "set") + Character.toUpperCase(name.charAt(0))
						+ name.substring(1));
		set.param(propType, "value");
		set.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("this." + name + "=value;");
				f.nl();
			}
		});
		PropertyCustomizerParameters propCustomizer = new PropertyCustomizerParameters(writer, p, defineClass, get, set,
				field);
		writer.runCustomizers(propCustomizer);
		ext.params.add(propCustomizer);
		if (writer.getConfig().isJaxbSupport() && p.getXMLHints() != null) {
			IXMLHints xmlHints = p.getXMLHints();
			if (xmlHints.isAttribute()) {
				JAnnotationUse annotate = get.annotate(XmlAttribute.class);
				if (xmlHints.localName() != null && !xmlHints.localName().equals(name)) {
					annotate.param("name", xmlHints.localName());
				}
			} else {
				if (xmlHints.wrapped()) {
					JAnnotationUse annotate = get.annotate(XmlElementWrapper.class);
					annotate.param("name", name);
					annotate = get.annotate(XmlElement.class);
					String mn = p.range().name();
					AbstractType range = p.range();
					ComponentShouldBeOfType oneMeta = range.oneMeta(ComponentShouldBeOfType.class);
					if (oneMeta!=null){
						range=oneMeta.range();
					}
					if (range.superTypes().size()>1){
						mn=name;
					}
					while (mn == null || mn.length() == 0) {

						
						mn = range.name();
						if (range.hasDirectMeta(XMLFacet.class)){
							XMLFacet meta = range.oneMeta(XMLFacet.class);
							if (meta.getName()!=null&&meta.getName().length()>0){
								mn=meta.getName();
							}
						}
						range = range.superType();
					}
					annotate.param("name", mn);
				} else if (xmlHints.localName() != null && !xmlHints.localName().equals(name)) {
					JAnnotationUse annotate = get.annotate(XmlElement.class);
					annotate.param("name", xmlHints.localName());
				}

			}
		}
	}

}
