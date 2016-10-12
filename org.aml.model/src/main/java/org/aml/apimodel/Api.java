package org.aml.apimodel;

import java.util.List;

public interface Api extends TopLevelModel{
	
	Resource[] resources();

	String title();
	
	String version();
	
	List<DocumentationItem> documentation();
}
