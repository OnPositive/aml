package org.aml.server.core;

public interface DependencyManager {

	void replaceDepencencies(int id,int[] newDependencies);
	
	int[] dependencies(int id);
	int[] dependentItems(int id);
	
	
	
}