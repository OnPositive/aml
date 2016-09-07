package org.aml.java2raml;

import java.util.ArrayList;

import org.aml.typesystem.java.IConfiguarionExtension;

public class Config {

	public static enum MemberProcessingMode{
		FIELDS,
		PROPERTIES,
		NONE
	}
	
	public static enum JavaOptionalityMode{
		PRIMITIVES_ARE_REQUIRED,
		EVERYTHING_IS_REQUIRED,
		OBJECTS_ARE_NULLABLE
	}
	
	public static enum DefaultAnnotationBehavior{
		IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES,GENERATE_ALL;
	}
	protected ILogger log=new ILogger() {
		
		@Override
		public void log(String s) {
			System.err.println(s);
		}
	};
	
	// switch settings
	
	public ILogger getLog() {
		return log;
	}

	public void setLog(ILogger log) {
		this.log = log;
	}

	public MemberProcessingMode getMemberMode() {
		return memberMode;
	}

	public void setMemberMode(MemberProcessingMode memberMode) {
		this.memberMode = memberMode;
	}

	public JavaOptionalityMode getOptinalityMode() {
		return optinalityMode;
	}

	public void setOptinalityMode(JavaOptionalityMode optinalityMode) {
		this.optinalityMode = optinalityMode;
	}

	public DefaultAnnotationBehavior getAnnotationsBehavior() {
		return annotationsBehavior;
	}

	public void setAnnotationsBehavior(DefaultAnnotationBehavior annotationsBehavior) {
		this.annotationsBehavior = annotationsBehavior;
	}

	public ArrayList<String> getAnnotationPackages() {
		return annotationPackages;
	}

	public void setAnnotationPackages(ArrayList<String> annotationPackages) {
		this.annotationPackages = annotationPackages;
	}

	public ArrayList<String> getPackageNamesToLook() {
		return packageNamesToLook;
	}

	public void setPackageNamesToLook(ArrayList<String> packageNamesToLook) {
		this.packageNamesToLook = packageNamesToLook;
	}

	public String getPathToLookForClasses() {
		return pathToLookForClasses;
	}

	public void setPathToLookForClasses(String pathToLookForClasses) {
		this.pathToLookForClasses = pathToLookForClasses;
	}

	public ArrayList<String> getClassMasksToIgnore() {
		return classMasksToIgnore;
	}

	public void setClassMasksToIgnore(ArrayList<String> classMasksToIgnore) {
		this.classMasksToIgnore = classMasksToIgnore;
	}

	public boolean isIgnoreUnreferencedAnnotationDeclarations() {
		return ignoreUnreferencedAnnotationDeclarations;
	}

	public void setIgnoreUnreferencedAnnotationDeclarations(boolean ignoreUnreferencedAnnotationDeclarations) {
		this.ignoreUnreferencedAnnotationDeclarations = ignoreUnreferencedAnnotationDeclarations;
	}

	public ArrayList<String> getAnnotationProfiles() {
		return annotationProfiles;
	}

	public void setAnnotationProfiles(ArrayList<String> annotationProfiles) {
		this.annotationProfiles = annotationProfiles;
	}

	public boolean isIgnoreDefaultProfiles() {
		return ignoreDefaultProfiles;
	}

	public void setIgnoreDefaultProfiles(boolean ignoreDefaultProfiles) {
		this.ignoreDefaultProfiles = ignoreDefaultProfiles;
	}

	protected MemberProcessingMode memberMode=MemberProcessingMode.FIELDS;
	
	protected JavaOptionalityMode optinalityMode=JavaOptionalityMode.EVERYTHING_IS_REQUIRED;
	
	protected DefaultAnnotationBehavior annotationsBehavior=DefaultAnnotationBehavior.IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES;
	
	protected ArrayList<String>annotationPackages=new ArrayList<>();
	
	
	//Collector related (all supported)

	protected ArrayList<String>packageNamesToLook=new ArrayList<>();
	
	protected String pathToLookForClasses;
	
	protected ArrayList<String>classMasksToIgnore=new ArrayList<>();
	
	protected boolean ignoreUnreferencedAnnotationDeclarations=true;
	
	//Annotation profiles related settings
	
	protected ArrayList<String>annotationProfiles=new ArrayList<>();
	
	boolean ignoreDefaultProfiles=false;
	
	
	protected ArrayList<IConfiguarionExtension>extensions=new ArrayList<>();

	public void addExtension(IConfiguarionExtension newInstance) {
		this.extensions.add(newInstance);
	}
	
	
}
