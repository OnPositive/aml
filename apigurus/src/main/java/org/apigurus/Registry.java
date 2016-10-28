package org.apigurus;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Registry {

	Map<String,RegistryEntry>registry=new LinkedHashMap<String, RegistryEntry>();
	

	@JsonAnyGetter()
	public Map<String, RegistryEntry> getRegistry() {
		return registry;
	}
	@JsonAnySetter()
	public void set(String name, RegistryEntry value) {
		registry.put(name, value);
	}
}
