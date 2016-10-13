package org.aml.apimodel;

import java.util.List;

public interface Api extends TopLevelModel,IHasResources{
	
	
	String title();
	
	String version();
	
	List<DocumentationItem> documentation();

	
}
