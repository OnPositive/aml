package org.aml.apimodel;

import java.util.List;

public interface Api extends TopLevelModel,IHasResources{
	
	String title();
	
	String description();
	
	String version();
	
	List<DocumentationItem> documentation();
	
	List<String> getMediaType();
	
	List<String> getProtocols();

	List<SecuredByConfig> getSecuredBy();

	String getBaseUrl();	
}
