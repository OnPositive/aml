package org.aml.raml2java;

import java.util.ArrayList;

import org.aml.java.mapping.primarySuperType;

public class JavaGenerationConfig {
	
	
	public static enum DefaultIntegerFormat{
		INT,LONG,BIGINT
	}
	public static enum DefaultNumberFormat{
		DOUBLE,BIGDECIMAL
	}

	public static enum MultipleInheritanceStrategy{
		ALL_PLAIN,MIX_IN
	}
	
	public static enum WrappersStrategy{
		NONE,OPTIONAL,ALWAYS
	}
	
	protected String defaultPackageName;
	protected String outputPath;	
	protected boolean generateInterfaces;	
	protected MultipleInheritanceStrategy multipleInheritance=MultipleInheritanceStrategy.ALL_PLAIN;
	protected boolean gsonSupport;
	protected boolean jacksonSupport;
	protected boolean jaxbSupport;	
	protected ArrayList<IPropertyCustomizer>customizers=new ArrayList<>();
	protected ArrayList<IClassCustomizer> classCustomizers=new ArrayList<>();
	protected boolean containerStrategyCollection=true;
	protected WrappersStrategy wrapperStrategy=WrappersStrategy.NONE;	
	protected boolean addGenerated=true;
	protected BasicAnnotationProcessingConfig annotationConfig;
	protected DefaultIntegerFormat integerFormat=DefaultIntegerFormat.INT;
	protected DefaultNumberFormat doubleFormat=DefaultNumberFormat.DOUBLE;
	protected boolean includeJsr303Annotations=true;
	protected FacetProcessingConfig cfg = new FacetProcessingConfig();

	protected boolean hashCodeAndEquals;
	protected boolean implementSerializable;
	protected boolean implementClonable;
	protected boolean generateBuilderMethods=true;
	
	public boolean isGenerateBuilderMethods() {
		return generateBuilderMethods;
	}

	public void setGenerateBuilderMethods(boolean generateBuilderMethods) {
		this.generateBuilderMethods = generateBuilderMethods;
	}

	public boolean isIncludeJsr303Annotations() {
		return includeJsr303Annotations;
	}

	public void setIncludeJsr303Annotations(boolean includeJsr303Annotations) {
		this.includeJsr303Annotations = includeJsr303Annotations;
		if (includeJsr303Annotations){
			cfg=new FacetProcessingConfig();
		}
		else{
			cfg.clear();
		}
	}
	
	public boolean isImplementClonable() {
		return implementClonable;
	}

	public void setImplementClonable(boolean implementClonable) {
		this.implementClonable = implementClonable;
	}

	public boolean isImplementSerializable() {
		return implementSerializable;
	}

	public void setImplementSerializable(boolean implementSerializable) {
		this.implementSerializable = implementSerializable;
	}

	public boolean isGenerateHashCodeAndEquals() {
		return hashCodeAndEquals;
	}

	public void setGenerateHashCodeAndEquals(boolean hashCodeAndEquals) {
		this.hashCodeAndEquals = hashCodeAndEquals;
	}

	public WrappersStrategy getWrapperStrategy() {
		return wrapperStrategy;
	}

	public void setWrapperStrategy(WrappersStrategy wrapperStrategy) {
		this.wrapperStrategy = wrapperStrategy;
	}

	
	
	public DefaultIntegerFormat getIntegerFormat() {
		return integerFormat;
	}

	public void setIntegerFormat(DefaultIntegerFormat integerFormat) {
		this.integerFormat = integerFormat;
	}

	public DefaultNumberFormat getDoubleFormat() {
		return doubleFormat;
	}

	public void setDoubleFormat(DefaultNumberFormat doubleFormat) {
		this.doubleFormat = doubleFormat;
	}

	
	
	public boolean isAddGenerated() {
		return addGenerated;
	}

	public void setAddGenerated(boolean addGenerated) {
		this.addGenerated = addGenerated;
	}

	public BasicAnnotationProcessingConfig getAnnotationConfig() {
		return annotationConfig;
	}

	public void setAnnotationConfig(BasicAnnotationProcessingConfig annotationConfig) {
		this.annotationConfig = annotationConfig;
	}
	
	public JavaGenerationConfig() {
		
		classCustomizers.add(new FacetProcessingConfig());
		BasicAnnotationProcessingConfig basicAnnotationProcessingConfig = new BasicAnnotationProcessingConfig();
		basicAnnotationProcessingConfig.addNamespaceToSkipDefinition(primarySuperType.class.getPackage().getName());
		basicAnnotationProcessingConfig.addNamespaceToSkipReference(primarySuperType.class.getPackage().getName());
		annotationConfig=basicAnnotationProcessingConfig;
	}
	
	public boolean isContainerStrategyCollection() {
		return containerStrategyCollection;
	}

	public void setContainerStrategyCollection(boolean containerStrategyCollection) {
		this.containerStrategyCollection = containerStrategyCollection;
	}

	public ArrayList<IClassCustomizer> getClassCustomizers() {
		return classCustomizers;
	}

	public void setClassCustomizers(ArrayList<IClassCustomizer> classCustomizers) {
		this.classCustomizers = classCustomizers;
	}

	public ArrayList<IPropertyCustomizer> getCustomizers() {
		return customizers;
	}

	public void setCustomizers(ArrayList<IPropertyCustomizer> customizers) {
		this.customizers = customizers;
	}

	public boolean isJaxbSupport() {
		return jaxbSupport;
	}

	public void setJaxbSupport(boolean jaxbSupport) {
		this.jaxbSupport = jaxbSupport;
	}

	public boolean isJacksonSupport() {
		return jacksonSupport;
	}

	public void setJacksonSupport(boolean jacksonSupport) {
		this.jacksonSupport = jacksonSupport;
	}

	public boolean isGsonSupport() {
		return gsonSupport;
	}

	public void setGsonSupport(boolean gsonSupport) {
		this.gsonSupport = gsonSupport;
	}

	public String getDefaultPackageName() {
		return defaultPackageName;
	}

	public void setDefaultPackageName(String defaultPackageName) {
		this.defaultPackageName = defaultPackageName;
	}

	public void setMultipleInheritanceStrategy(MultipleInheritanceStrategy st) {
		this.multipleInheritance=st;
	}
}