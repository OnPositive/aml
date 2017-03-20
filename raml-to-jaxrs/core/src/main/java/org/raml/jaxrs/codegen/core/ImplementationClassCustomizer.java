package org.raml.jaxrs.codegen.core;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.aml.core.mappings.id;
import org.aml.core.mappings.parent;
import org.aml.core.mappings.visibleWhen;
import org.aml.persistance.jdo.VisibleWhen;
import org.aml.raml2java.ClassCustomizerParameters;
import org.aml.raml2java.IClassCustomizer;
import org.aml.typesystem.AbstractType;
import org.raml.jaxrs.codegen.core.ImplementationGenerator.ChildDefinition;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

public class ImplementationClassCustomizer implements IClassCustomizer {

	ImplementationGenerator generator;

	public ImplementationClassCustomizer(ImplementationGenerator implementationGenerator) {
		this.generator = implementationGenerator;
	}

	@Override
	public void customize(ClassCustomizerParameters parameters) {
		AbstractType type = parameters.getType();
		JDefinedClass clazz = parameters.getClazz();
		clazz.annotate(PersistenceCapable.class);
		type.toPropertiesView().allProperties().forEach(x -> {
			if (x.range().annotation(id.class, false) != null) {
				JFieldVar jFieldVar = clazz.fields().get(x.id());
				jFieldVar.annotate(PrimaryKey.class);
				jFieldVar.annotate(Persistent.class).param("valueStrategy", IdGeneratorStrategy.INCREMENT);
			}
			if (x.range().annotation(parent.class, false) != null) {
				generator.recordParent(type, x.id(), x.range(), clazz);
			}
			visibleWhen annotation = x.range().annotation(org.aml.core.mappings.visibleWhen.class, false);
			if (annotation != null) {
				JFieldVar jFieldVar = clazz.fields().get(x.id());
				String value = annotation.value();
				jFieldVar.annotate(VisibleWhen.class).param("value", value);
			}
		});
		ArrayList<ChildDefinition> arrayList = generator.children.get(type);
		if (arrayList != null) {
			arrayList.forEach(x -> {
				generator.processChildDefinition(clazz, x);
			});
		}
	}

}
