package org.aml.apimodel;

import java.util.Map;

public interface SecurityScheme extends Annotable {

	String name();

	String type();

	Map<String, Object> settings();
	
	String description();
}
