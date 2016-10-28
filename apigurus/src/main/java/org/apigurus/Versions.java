package org.apigurus;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Versions {

	Map<String,ApiVersion>registry=new LinkedHashMap<String, ApiVersion>();
	

	@JsonAnyGetter()
	public Map<String, ApiVersion> getRegistry() {
		return registry;
	}
	@JsonAnySetter()
	public void set(String name, ApiVersion value) {
		registry.put(name, value);
	}
}
