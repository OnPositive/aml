package org.aml.raml2java;

import org.aml.typesystem.beans.IProperty;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;

public class PropertyCustomizerParameters {
	protected final JavaWriter writer;

	protected final IProperty prop;
	protected final JDefinedClass clazz;
	protected final JMethod getter;
	protected final JMethod setter;
	protected final JFieldVar field;

	public PropertyCustomizerParameters(JavaWriter writer, IProperty prop, JDefinedClass clazz, JMethod getter,
			JMethod setter, JFieldVar field) {
		this.writer = writer;
		this.prop = prop;
		this.clazz = clazz;
		this.getter = getter;
		this.setter = setter;
		this.field = field;
	}

	public JavaWriter getWriter() {
		return writer;
	}

	public IProperty getProp() {
		return prop;
	}

	public JDefinedClass getClazz() {
		return clazz;
	}

	public JMethod getGetter() {
		return getter;
	}

	public JMethod getSetter() {
		return setter;
	}

	public JFieldVar getField() {
		return field;
	}
}