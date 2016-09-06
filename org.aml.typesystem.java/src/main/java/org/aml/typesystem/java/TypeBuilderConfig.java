package org.aml.typesystem.java;

public class TypeBuilderConfig {

	private ITypeNamingConvention namingConvention=new SimpleNamingConvention();
	private IAnnotationFilter annotationsFilter=new SkipAnnotationsFilter();
	private OptionalityNullabilityChecker checkNullable=new AllRequired();
	private IMemberFilter memberFilter=new FieldMemberFilter();
	private IPropertyNameBuilder propertyNameBuilder=new OneTwoOneNameBuilder();
	private AnnotationsProcessingConfig annotationsProcessingConfig=new AnnotationsProcessingConfig();
	
	public AnnotationsProcessingConfig getAnnotationsProcessingConfig() {
		return annotationsProcessingConfig;
	}

	public TypeBuilderConfig() {
		annotationsProcessingConfig.append(TypeBuilderConfig.class.getResourceAsStream("/jaxb.xml"));
		annotationsProcessingConfig.append(TypeBuilderConfig.class.getResourceAsStream("/lang.xml"));
		annotationsProcessingConfig.append(TypeBuilderConfig.class.getResourceAsStream("/javax.validation.xml"));
	}
	
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
