package org.aml.java2raml;

import java.util.ArrayList;

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
	
	
}
