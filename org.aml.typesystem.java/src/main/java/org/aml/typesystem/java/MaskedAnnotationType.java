package org.aml.typesystem.java;

import java.util.List;

import org.aml.typesystem.IAnnotationModel;
import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMethodModel;
import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.ITypeParameter;

public class MaskedAnnotationType implements ITypeModel{

	
	private ITypeModel mdl;

	public MaskedAnnotationType(ITypeModel mdl) {
		super();
		this.mdl = mdl;
	}

	public List<ITypeParameter> getTypeParameters() {
		return mdl.getTypeParameters();
	}

	public IMethodModel[] getMethods() {
		return mdl.getMethods();
	}

	public String getName() {
		return mdl.getName();
	}

	public IFieldModel[] getFields() {
		return mdl.getFields();
	}

	public String getDocumentation() {
		return mdl.getDocumentation();
	}

	public String getFullyQualifiedName() {
		return mdl.getFullyQualifiedName();
	}

	public IAnnotationModel[] getAnnotations() {
		return mdl.getAnnotations();
	}

	public ITypeModel getSuperClass() {
		return mdl.getSuperClass();
	}

	public String getAnnotationValue(String annotation) {
		return mdl.getAnnotationValue(annotation);
	}

	public ITypeModel[] getImplementedInterfaces() {
		return mdl.getImplementedInterfaces();
	}

	public String[] getAnnotationValues(String annotation) {
		return mdl.getAnnotationValues(annotation);
	}

	public ITypeModel resolveClass(String qualifiedName) {
		return mdl.resolveClass(qualifiedName);
	}

	public boolean hasAnnotation(String name) {
		return mdl.hasAnnotation(name);
	}

	public boolean isCollection() {
		return mdl.isCollection();
	}

	public ITypeModel getComponentType() {
		return mdl.getComponentType();
	}

	public boolean isEnum() {
		return mdl.isEnum();
	}

	public boolean isAnnotation() {
		return false;
	}

	public IAnnotationModel getAnnotation(String name) {
		return mdl.getAnnotation(name);
	}

	public boolean hasAnnotationWithCanonicalName(String name) {
		return mdl.hasAnnotationWithCanonicalName(name);
	}

	public IAnnotationModel getAnnotationByCanonicalName(String name) {
		return mdl.getAnnotationByCanonicalName(name);
	}
}
