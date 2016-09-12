package org.aml.raml2java;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.meta.facets.Default;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpressionImpl;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public class AnnotationTypeGenerator implements ITypeGenerator {

	protected JavaWriter owner;

	public AnnotationTypeGenerator(JavaWriter owner) {
		super();
		this.owner = owner;
	}

	public JType define(AbstractType t) {
		if (t.isAnonimous()) {
			throw new IllegalStateException("Annotation types can not be anonimous");
		}
		if (!t.isObject()) {
			AbstractType wrapped=TypeOps.derive(t.name(), BuiltIns.OBJECT);
			if (!t.isNill()){
			wrapped.declareProperty("value", t.clone(""),false);			
			}
			return define(wrapped);
		}
		if (t.isObject()) {
			JDefinedClass defineClass = owner.defineClass(t, ClassType.ANNOTATION_TYPE_DECL);
			JAnnotationUse annotate = defineClass.annotate(Retention.class);
			annotate.param("value", RetentionPolicy.RUNTIME);
			t.toPropertiesView().allProperties().forEach(x -> {
				//note not primitive annotation members should become annotations
				JType type = owner.getType(x.range(),false,true,x);
				JMethod method = defineClass.method(JMod.PUBLIC, type,owner.propNameGenerator.name(x));
				owner.annotate(method,x.range());
				Default oneMeta = x.range().oneMeta(Default.class);
				if (oneMeta!=null){
					Object value = oneMeta.value();
					if (x.range().isEnumType()){
						method.declareDefaultValue(new JExpressionImpl() {
							
							@Override
							public void generate(JFormatter f) {
								f.p(type.name()+"."+value);
								
							}
						});
					}
					else{
						method.declareDefaultValue(owner.toExpr(value));
					}
				}
				else if (!x.isRequired()){
					Object value=owner.getDefault(x.range());
					method.declareDefaultValue(owner.toExpr(value));
				}
			});
			owner.annotate(defineClass, t);
			return defineClass;
		}
		return null;
	}
}
