package org.aml.typesystem.java;

import java.util.List;

import org.aml.typesystem.IAnnotationModel;
import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMethodModel;
import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.ITypeParameter;

/**
 * <p>MaskedAnnotationType class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class MaskedAnnotationType implements ITypeModel{

	
	private ITypeModel mdl;

	/**
	 * <p>Constructor for MaskedAnnotationType.</p>
	 *
	 * @param mdl a {@link org.aml.typesystem.ITypeModel} object.
	 */
	public MaskedAnnotationType(ITypeModel mdl) {
		super();
		this.mdl = mdl;
	}

	/** {@inheritDoc} */
	@Override
	public List<ITypeParameter> getTypeParameters() {
		return mdl.getTypeParameters();
	}

	/** {@inheritDoc} */
	@Override
	public IMethodModel[] getMethods() {
		return mdl.getMethods();
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return mdl.getName();
	}

	/** {@inheritDoc} */
	@Override
	public IFieldModel[] getFields() {
		return mdl.getFields();
	}

	/** {@inheritDoc} */
	@Override
	public String getDocumentation() {
		return mdl.getDocumentation();
	}

	/** {@inheritDoc} */
	@Override
	public String getFullyQualifiedName() {
		return mdl.getFullyQualifiedName();
	}

	/** {@inheritDoc} */
	@Override
	public IAnnotationModel[] getAnnotations() {
		return mdl.getAnnotations();
	}

	/** {@inheritDoc} */
	@Override
	public ITypeModel getSuperClass() {
		return mdl.getSuperClass();
	}

	/** {@inheritDoc} */
	@Override
	public String getAnnotationValue(String annotation) {
		return mdl.getAnnotationValue(annotation);
	}

	/** {@inheritDoc} */
	@Override
	public ITypeModel[] getImplementedInterfaces() {
		return mdl.getImplementedInterfaces();
	}

	/** {@inheritDoc} */
	@Override
	public String[] getAnnotationValues(String annotation) {
		return mdl.getAnnotationValues(annotation);
	}

	/** {@inheritDoc} */
	@Override
	public ITypeModel resolveClass(String qualifiedName) {
		return mdl.resolveClass(qualifiedName);
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAnnotation(String name) {
		return mdl.hasAnnotation(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCollection() {
		return mdl.isCollection();
	}

	/** {@inheritDoc} */
	@Override
	public ITypeModel getComponentType() {
		return mdl.getComponentType();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEnum() {
		return mdl.isEnum();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAnnotation() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public IAnnotationModel getAnnotation(String name) {
		return mdl.getAnnotation(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAnnotationWithCanonicalName(String name) {
		return mdl.hasAnnotationWithCanonicalName(name);
	}

	/** {@inheritDoc} */
	@Override
	public IAnnotationModel getAnnotationByCanonicalName(String name) {
		return mdl.getAnnotationByCanonicalName(name);
	}

	/** {@inheritDoc} */
	@Override
	public String getPackageName() {
		return mdl.getPackageName();
	}
}
