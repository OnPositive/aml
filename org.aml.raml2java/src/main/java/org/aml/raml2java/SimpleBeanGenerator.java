package org.aml.raml2java;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.aml.raml2java.JavaGenerationConfig.MultipleInheritanceStrategy;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.IProperty;
import org.raml.v2.internal.utils.StreamUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JArray;
import com.sun.codemodel.JClass;
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
	private boolean booleanAsIs=false;

	public SimpleBeanGenerator(JavaWriter wr) {
		this.writer = wr;

	}
	static class Extras{
		ArrayList<String>patterns=new ArrayList<>();
		ArrayList<String>fieldNames=new ArrayList<>();
		ArrayList<JType>types=new ArrayList<>();
	}

	@Override
	public JType define(AbstractType t) {
		JDefinedClass defineClass = writer.defineClass(t, ClassType.CLASS);
		AbstractType superType = t.superType();
		boolean hasAdditionalOrMap=false;
		boolean hasMap=false;
		Extras ext=new Extras();
		if (superType != null) {
			JType type = writer.getType(superType);
			defineClass._extends((JClass) type);
			for (IProperty p:t.toPropertiesView().properties()){
				generateProperty(defineClass, p,ext);
				if(p.isAdditional()||p.isMap()){
					hasAdditionalOrMap=true;
					hasMap|=p.isMap();
					
				}
			};
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
				for (IProperty p:t.toPropertiesView().allProperties()){
					if(!inheritedPropeties.contains(p.id())){
						generateProperty(defineClass, p,ext);
						if(p.isAdditional()||p.isMap()){
							hasAdditionalOrMap=true;
							hasMap|=p.isMap();		
						}
					}
				};	
			} else {
				for (IProperty p:t.toPropertiesView().allProperties()){
					generateProperty(defineClass, p,ext);
					if(p.isAdditional()||p.isMap()){
						hasAdditionalOrMap=true;
						hasMap|=p.isMap();
					}
				};
			}
		}
		writer.annotate(defineClass, t);
		if (hasAdditionalOrMap){
			if (writer.getConfig().isGsonSupport()){
				if (!defineClass.getPackage().hasResourceFile("PatternAndAdditionalTypeAdapterFactory.java")){
					String string = StreamUtils.toString(SimpleBeanGenerator.class.getResourceAsStream("/PatternAndAdditionalTypeAdapterFactory.tpl"));
					string=string.replace("{packageName}", defineClass.getPackage().name());
					defineClass.getPackage().addResourceFile(new StringResourceFile("PatternAndAdditionalTypeAdapterFactory.java", string));
				}
				if (!defineClass.getPackage().hasResourceFile("PatternAndAdditionalTypeAdapter.java")){
					String string = StreamUtils.toString(SimpleBeanGenerator.class.getResourceAsStream("/PatternAndAdditionalTypeAdapter.tpl"));
					string=string.replace("{packageName}", defineClass.getPackage().name());
					defineClass.getPackage().addResourceFile(new StringResourceFile("PatternAndAdditionalTypeAdapter.java", string));
				}
				defineClass.annotate(JsonAdapter.class).param("value", JExpr.dotclass(writer.getModel().directClass("PatternAndAdditionalTypeAdapterFactory")));
				JArray newArray = JExpr.newArray(writer.getModel()._ref(String.class));
				defineClass.field(JMod.STATIC, String[].class, "$PATTERNS").init(newArray);
				for (String s:ext.patterns){
					newArray.add(JExpr.lit(s));
				}
				newArray = JExpr.newArray(writer.getModel()._ref(String.class));
				defineClass.field(JMod.STATIC, String[].class, "$FIELDS").init(newArray);
				for (String s:ext.fieldNames){
					newArray.add(JExpr.lit(s));
				}
				newArray = JExpr.newArray(writer.getModel()._ref(Class.class));
				defineClass.field(JMod.STATIC, Class[].class, "$CLASSES").init(newArray);
				for (JType ct:ext.types){
					newArray.add(JExpr.direct(ct.fullName()+".class"));
				}
			}
		}
		return defineClass;
	}

	private void generateProperty(JDefinedClass defineClass, IProperty p, Extras ext) {
		
		String name = writer.propNameGenerator.name(p);
		JType propType = writer.getType(p.range(), false, false, p);
		
		JExpression initExpr=null;
		if (p.isMap()||p.isAdditional()){
			JType oType=propType;
			propType=((JClass)writer.getModel()._ref(Map.class)).narrow(String.class).narrow(propType);
			initExpr=JExpr._new(((JClass)writer.getModel()._ref(LinkedHashMap.class)).narrow(String.class).narrow(oType));
			ext.fieldNames.add(name);
			ext.patterns.add(p.id().substring(1,p.id().length()-1));
			ext.types.add(oType);
			
		}
		JFieldVar field = defineClass.field(JMod.PRIVATE, propType, name);
		if (writer.getConfig().isGsonSupport()){
			if (p.isMap()||p.isAdditional()){
				field.annotate(Expose.class).param("serialize", false).param("deserialize", false);
			}
		}
		if (initExpr!=null){
			field.init(initExpr);
		}
		JMethod get = defineClass.method(JMod.PUBLIC, propType,
				"get" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
		get.body()._return(JExpr.ref(name));
		writer.annotate(get, p.range());
		JMethod set = defineClass.method(JMod.PUBLIC, writer.getModel()._ref(void.class),
				((p.range().isBoolean()&&this.booleanAsIs) ? "is" : "set") + Character.toUpperCase(name.charAt(0)) + name.substring(1));
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
