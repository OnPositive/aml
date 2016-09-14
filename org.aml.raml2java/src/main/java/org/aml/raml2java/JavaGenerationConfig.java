package org.aml.raml2java;

public class JavaGenerationConfig {

	public static enum MultipleInheritanceStrategy{
		ALL_PLAIN,MIX_IN
	}
	
	protected String defaultPackageName;
	protected String outputPath;
	
	protected boolean generateInterfaces;
	
	protected MultipleInheritanceStrategy multipleInheritance=MultipleInheritanceStrategy.ALL_PLAIN;
	public boolean gsonSupport;
	public boolean jacksonSupport;
	
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