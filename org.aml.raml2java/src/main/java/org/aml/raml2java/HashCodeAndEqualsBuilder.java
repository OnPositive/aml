package org.aml.raml2java;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class HashCodeAndEqualsBuilder implements IClassCustomizer{

	@Override
	public void customize(ClassCustomizerParameters parameters) {
		JMethod hash = parameters.clazz.method(JMod.PUBLIC, int.class, "hashCode");
		JExpression newBuilder=JExpr._new(parameters.writer.getModel()._ref(HashCodeBuilder.class));
		for (String s:parameters.clazz.fields().keySet()){
			if ((parameters.clazz.fields().get(s).mods().getValue()&JMod.STATIC)==0){
				newBuilder=newBuilder.invoke("append").arg(JExpr.ref(s));
			}
		}
		hash.body()._return(newBuilder.invoke("build"));		
		JMethod equals = parameters.clazz.method(JMod.PUBLIC, boolean.class, "equals");
		equals.param(Object.class, "obj");
		JBlock bl=equals.body()._if(JExpr.direct("obj instanceof "+parameters.clazz.name()))._then();
		bl.decl(parameters.clazz, "castedObj", JExpr.cast(parameters.clazz, JExpr.ref("obj")));
		newBuilder=JExpr._new(parameters.writer.getModel()._ref(EqualsBuilder.class));
		
		for (String s:parameters.clazz.fields().keySet()){
			if ((parameters.clazz.fields().get(s).mods().getValue()&JMod.STATIC)==0){
				newBuilder=newBuilder.invoke("append").arg(JExpr.ref(s)).arg(JExpr.direct("castedObj."+s));
			}
		}
		bl._return(newBuilder.invoke("build"));
		equals.body()._return(JExpr.lit(false));
	}

}
