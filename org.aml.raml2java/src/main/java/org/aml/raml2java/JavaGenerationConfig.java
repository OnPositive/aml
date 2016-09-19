package org.aml.raml2java;

import java.util.ArrayList;

public class JavaGenerationConfig {

	public static enum MultipleInheritanceStrategy{
		ALL_PLAIN,MIX_IN
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
	protected boolean containerStrategyCollection=false;
	
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