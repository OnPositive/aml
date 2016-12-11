package org.aml.registry.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Overlays {

	String name;
	Map<String,String>overlaysFor=new LinkedHashMap<String, String>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getOverlaysFor() {
		return overlaysFor;
	}

	public void setOverlaysFor(Map<String, String> overlaysFor) {
		this.overlaysFor = overlaysFor;
	}
}
