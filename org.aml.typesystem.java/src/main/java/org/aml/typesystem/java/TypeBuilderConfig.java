package org.aml.typesystem.java;

/**
 * <p>TypeBuilderConfig class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class TypeBuilderConfig {

	private ITypeNamingConvention namingConvention=new SimpleNamingConvention();
	private IAnnotationFilter annotationsFilter=new SkipAnnotationsFilter();
	private OptionalityNullabilityChecker checkNullable=new AllRequired();
	private IMemberFilter memberFilter=new FieldMemberFilter();
	private IPropertyNameBuilder propertyNameBuilder=new OneTwoOneNameBuilder();
	private AnnotationsProcessingConfig annotationsProcessingConfig=new AnnotationsProcessingConfig();
	
	/**
	 * <p>Getter for the field <code>annotationsProcessingConfig</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.java.AnnotationsProcessingConfig} object.
	 */
	public AnnotationsProcessingConfig getAnnotationsProcessingConfig() {
		return annotationsProcessingConfig;
	}

	/**
	 * <p>Constructor for TypeBuilderConfig.</p>
	 */
	public TypeBuilderConfig() {
		annotationsProcessingConfig.append(TypeBuilderConfig.class.getResourceAsStream("/jaxb.xml"));
		annotationsProcessingConfig.append(TypeBuilderConfig.class.getResourceAsStream("/lang.xml"));
		annotationsProcessingConfig.append(TypeBuilderConfig.class.getResourceAsStream("/javax.validation.xml"));
	}
	
	/**
	 * <p>Getter for the field <code>propertyNameBuilder</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.java.IPropertyNameBuilder} object.
	 */
	public IPropertyNameBuilder getPropertyNameBuilder() {
		return propertyNameBuilder;
	}

	/**
	 * <p>Setter for the field <code>propertyNameBuilder</code>.</p>
	 *
	 * @param propertyNameBuilder a {@link org.aml.typesystem.java.IPropertyNameBuilder} object.
	 */
	public void setPropertyNameBuilder(IPropertyNameBuilder propertyNameBuilder) {
		this.propertyNameBuilder = propertyNameBuilder;
	}

	/**
	 * <p>Getter for the field <code>memberFilter</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.java.IMemberFilter} object.
	 */
	public IMemberFilter getMemberFilter() {
		return memberFilter;
	}

	/**
	 * <p>Setter for the field <code>memberFilter</code>.</p>
	 *
	 * @param memberFilter a {@link org.aml.typesystem.java.IMemberFilter} object.
	 */
	public void setMemberFilter(IMemberFilter memberFilter) {
		this.memberFilter = memberFilter;
	}

	/**
	 * <p>Getter for the field <code>checkNullable</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.java.OptionalityNullabilityChecker} object.
	 */
	public OptionalityNullabilityChecker getCheckNullable() {
		return checkNullable;
	}

	/**
	 * <p>Setter for the field <code>checkNullable</code>.</p>
	 *
	 * @param checkNullable a {@link org.aml.typesystem.java.OptionalityNullabilityChecker} object.
	 */
	public void setCheckNullable(OptionalityNullabilityChecker checkNullable) {
		this.checkNullable = checkNullable;
	}

	/**
	 * <p>Getter for the field <code>annotationsFilter</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.java.IAnnotationFilter} object.
	 */
	public IAnnotationFilter getAnnotationsFilter() {
		return annotationsFilter;
	}

	/**
	 * <p>Setter for the field <code>annotationsFilter</code>.</p>
	 *
	 * @param annotationsFilter a {@link org.aml.typesystem.java.IAnnotationFilter} object.
	 */
	public void setAnnotationsFilter(IAnnotationFilter annotationsFilter) {
		this.annotationsFilter = annotationsFilter;
	}

	/**
	 * <p>Getter for the field <code>namingConvention</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.java.ITypeNamingConvention} object.
	 */
	public ITypeNamingConvention getNamingConvention() {
		return namingConvention;
	}

	/**
	 * <p>Setter for the field <code>namingConvention</code>.</p>
	 *
	 * @param namingConvention a {@link org.aml.typesystem.java.ITypeNamingConvention} object.
	 */
	public void setNamingConvention(ITypeNamingConvention namingConvention) {
		this.namingConvention = namingConvention;
	}
}
