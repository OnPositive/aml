package org.aml.apimodel;

import java.util.LinkedHashMap;

public interface SecuredByConfig extends Annotable{
	String name();	
	LinkedHashMap<String, Object>settings();
}
