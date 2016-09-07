package org.aml.java2raml;

import java.util.ArrayList;

import org.aml.typesystem.java.IConfiguarionExtension;

/**
 * <p>Config class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
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
	
	/**
	 * <p>Getter for the field <code>log</code>.</p>
	 *
	 * @return a {@link org.aml.java2raml.ILogger} object.
	 */
	public ILogger getLog() {
		return log;
	}

	/**
	 * <p>Setter for the field <code>log</code>.</p>
	 *
	 * @param log a {@link org.aml.java2raml.ILogger} object.
	 */
	public void setLog(ILogger log) {
		this.log = log;
	}

	/**
	 * <p>Getter for the field <code>memberMode</code>.</p>
	 *
	 * @return a {@link org.aml.java2raml.Config.MemberProcessingMode} object.
	 */
	public MemberProcessingMode getMemberMode() {
		return memberMode;
	}

	/**
	 * <p>Setter for the field <code>memberMode</code>.</p>
	 *
	 * @param memberMode a {@link org.aml.java2raml.Config.MemberProcessingMode} object.
	 */
	public void setMemberMode(MemberProcessingMode memberMode) {
		this.memberMode = memberMode;
	}

	/**
	 * <p>Getter for the field <code>optinalityMode</code>.</p>
	 *
	 * @return a {@link org.aml.java2raml.Config.JavaOptionalityMode} object.
	 */
	public JavaOptionalityMode getOptinalityMode() {
		return optinalityMode;
	}

	/**
	 * <p>Setter for the field <code>optinalityMode</code>.</p>
	 *
	 * @param optinalityMode a {@link org.aml.java2raml.Config.JavaOptionalityMode} object.
	 */
	public void setOptinalityMode(JavaOptionalityMode optinalityMode) {
		this.optinalityMode = optinalityMode;
	}

	/**
	 * <p>Getter for the field <code>annotationsBehavior</code>.</p>
	 *
	 * @return a {@link org.aml.java2raml.Config.DefaultAnnotationBehavior} object.
	 */
	public DefaultAnnotationBehavior getAnnotationsBehavior() {
		return annotationsBehavior;
	}

	/**
	 * <p>Setter for the field <code>annotationsBehavior</code>.</p>
	 *
	 * @param annotationsBehavior a {@link org.aml.java2raml.Config.DefaultAnnotationBehavior} object.
	 */
	public void setAnnotationsBehavior(DefaultAnnotationBehavior annotationsBehavior) {
		this.annotationsBehavior = annotationsBehavior;
	}

	/**
	 * <p>Getter for the field <code>annotationPackages</code>.</p>
	 *
	 * @return a {@link java.util.ArrayList} object.
	 */
	public ArrayList<String> getAnnotationPackages() {
		return annotationPackages;
	}

	/**
	 * <p>Setter for the field <code>annotationPackages</code>.</p>
	 *
	 * @param annotationPackages a {@link java.util.ArrayList} object.
	 */
	public void setAnnotationPackages(ArrayList<String> annotationPackages) {
		this.annotationPackages = annotationPackages;
	}

	/**
	 * <p>Getter for the field <code>packageNamesToLook</code>.</p>
	 *
	 * @return a {@link java.util.ArrayList} object.
	 */
	public ArrayList<String> getPackageNamesToLook() {
		return packageNamesToLook;
	}

	/**
	 * <p>Setter for the field <code>packageNamesToLook</code>.</p>
	 *
	 * @param packageNamesToLook a {@link java.util.ArrayList} object.
	 */
	public void setPackageNamesToLook(ArrayList<String> packageNamesToLook) {
		this.packageNamesToLook = packageNamesToLook;
	}

	/**
	 * <p>Getter for the field <code>pathToLookForClasses</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPathToLookForClasses() {
		return pathToLookForClasses;
	}

	/**
	 * <p>Setter for the field <code>pathToLookForClasses</code>.</p>
	 *
	 * @param pathToLookForClasses a {@link java.lang.String} object.
	 */
	public void setPathToLookForClasses(String pathToLookForClasses) {
		this.pathToLookForClasses = pathToLookForClasses;
	}

	/**
	 * <p>Getter for the field <code>classMasksToIgnore</code>.</p>
	 *
	 * @return a {@link java.util.ArrayList} object.
	 */
	public ArrayList<String> getClassMasksToIgnore() {
		return classMasksToIgnore;
	}

	/**
	 * <p>Setter for the field <code>classMasksToIgnore</code>.</p>
	 *
	 * @param classMasksToIgnore a {@link java.util.ArrayList} object.
	 */
	public void setClassMasksToIgnore(ArrayList<String> classMasksToIgnore) {
		this.classMasksToIgnore = classMasksToIgnore;
	}

	/**
	 * <p>isIgnoreUnreferencedAnnotationDeclarations.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isIgnoreUnreferencedAnnotationDeclarations() {
		return ignoreUnreferencedAnnotationDeclarations;
	}

	/**
	 * <p>Setter for the field <code>ignoreUnreferencedAnnotationDeclarations</code>.</p>
	 *
	 * @param ignoreUnreferencedAnnotationDeclarations a boolean.
	 */
	public void setIgnoreUnreferencedAnnotationDeclarations(boolean ignoreUnreferencedAnnotationDeclarations) {
		this.ignoreUnreferencedAnnotationDeclarations = ignoreUnreferencedAnnotationDeclarations;
	}

	/**
	 * <p>Getter for the field <code>annotationProfiles</code>.</p>
	 *
	 * @return a {@link java.util.ArrayList} object.
	 */
	public ArrayList<String> getAnnotationProfiles() {
		return annotationProfiles;
	}

	/**
	 * <p>Setter for the field <code>annotationProfiles</code>.</p>
	 *
	 * @param annotationProfiles a {@link java.util.ArrayList} object.
	 */
	public void setAnnotationProfiles(ArrayList<String> annotationProfiles) {
		this.annotationProfiles = annotationProfiles;
	}

	/**
	 * <p>isIgnoreDefaultProfiles.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isIgnoreDefaultProfiles() {
		return ignoreDefaultProfiles;
	}

	/**
	 * <p>Setter for the field <code>ignoreDefaultProfiles</code>.</p>
	 *
	 * @param ignoreDefaultProfiles a boolean.
	 */
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

	/**
	 * <p>addExtension.</p>
	 *
	 * @param newInstance a {@link org.aml.typesystem.java.IConfiguarionExtension} object.
	 */
	public void addExtension(IConfiguarionExtension newInstance) {
		this.extensions.add(newInstance);
	}
	
	
}
