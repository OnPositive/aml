package org.aml.apimodel;

public interface Api extends TopLevelModel{
	
	Resource[] resources();

	String title();
	
	String version();
	
}