package org.aml.registry.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Overlays {

	String nameId;
	public String getNameId() {
		return nameId;
	}

	public void setNameId(String nameId) {
		this.nameId = nameId;
	}

	Map<String,String>overlaysFor=new LinkedHashMap<String, String>();
	


	public Map<String, String> getOverlaysFor() {
		return overlaysFor;
	}

	public void setOverlaysFor(Map<String, String> overlaysFor) {
		this.overlaysFor = overlaysFor;
	}
}
