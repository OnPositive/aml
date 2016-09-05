package org.aml.typesystem.java;

public class TypeBuilderConfig {

	private ITypeNamingConvention namingConvention=new SimpleNamingConvention();
	private IAnnotationFilter annotationsFilter;
	private OptionalityNullabilityChecker checkNullable;
	private IMemberFilter memberFilter;
	private IPropertyNameBuilder propertyNameBuilder;

	public IPropertyNameBuilder getPropertyNameBuilder() {
		return propertyNameBuilder;
	}

	public void setPropertyNameBuilder(IPropertyNameBuilder propertyNameBuilder) {
		this.propertyNameBuilder = propertyNameBuilder;
	}

	public IMemberFilter getMemberFilter() {
		return memberFilter;
	}

	public void setMemberFilter(IMemberFilter memberFilter) {
		this.memberFilter = memberFilter;
	}

	public OptionalityNullabilityChecker getCheckNullable() {
		return checkNullable;
	}

	public void setCheckNullable(OptionalityNullabilityChecker checkNullable) {
		this.checkNullable = checkNullable;
	}

	public IAnnotationFilter getAnnotationsFilter() {
		return annotationsFilter;
	}

	public void setAnnotationsFilter(IAnnotationFilter annotationsFilter) {
		this.annotationsFilter = annotationsFilter;
	}

	public ITypeNamingConvention getNamingConvention() {
		return namingConvention;
	}

	public void setNamingConvention(ITypeNamingConvention namingConvention) {
		this.namingConvention = namingConvention;
	}
}