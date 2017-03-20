package org.aml.raml2java;

import java.util.ArrayList;

import org.aml.typesystem.AbstractType;

import com.sun.codemodel.JDefinedClass;

public class ClassCustomizerParameters {

	protected final JavaWriter writer;	
	protected final JDefinedClass clazz;
	protected final AbstractType type;
	protected final ArrayList<PropertyCustomizerParameters>props;
	
	public ClassCustomizerParameters(JavaWriter writer, JDefinedClass clazz, AbstractType type,
			ArrayList<PropertyCustomizerParameters> props) {
		super();
		this.writer = writer;
		this.clazz = clazz;
		this.type = type;
		this.props = props;
	}
	
	public AbstractType getType(){
		return this.type;
	}
	
	public JavaWriter getWriter() {
		return writer;
	}
	public JDefinedClass getClazz() {
		return clazz;
	}
	public ArrayList<PropertyCustomizerParameters> getProps() {
		return props;
	}
}
