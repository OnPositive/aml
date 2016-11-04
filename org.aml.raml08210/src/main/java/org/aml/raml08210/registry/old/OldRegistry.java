package org.aml.raml08210.registry.old;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class OldRegistry {

	protected ArrayList<LinkedHashMap<String,OldApi>> apis=new ArrayList<>();
	
	public ArrayList<LinkedHashMap<String, OldApi>> getApis() {
		return apis;
	}
	public void setApis(ArrayList<LinkedHashMap<String, OldApi>> apis) {
		this.apis = apis;
	}
	public ArrayList<Object> getLibraries() {
		return libraries;
	}
	public void setLibraries(ArrayList<Object> libraries) {
		this.libraries = libraries;
	}
	protected ArrayList<Object> libraries=new ArrayList<>();
}
