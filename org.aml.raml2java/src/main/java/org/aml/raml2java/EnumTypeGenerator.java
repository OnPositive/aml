package org.aml.raml2java;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.restrictions.Enum;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JType;

public class EnumTypeGenerator implements ITypeGenerator{

	protected JavaWriter owner;

	public EnumTypeGenerator(JavaWriter owner) {
		super();
		this.owner = owner;
	}
	@Override
	public JType define(AbstractType t) {
		JDefinedClass defineClass = owner.defineClass(t, ClassType.ENUM);
		Enum oneMeta = t.oneMeta(Enum.class);
		oneMeta.value().forEach(x -> {
			defineClass.enumConstant(owner.escape(x));
		});
		return defineClass;		
	}

}
