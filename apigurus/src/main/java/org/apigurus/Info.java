package org.apigurus;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Info {

	Contact contact;
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	String description;
	String title;
	String version;
	
	Map<String,Object>registry=new LinkedHashMap<String, Object>();
	

	@JsonAnyGetter()
	public Map<String, Object> getExtra() {
		return registry;
	}
	@JsonAnySetter()
	public void setExtra(String name, Object value) {
		registry.put(name, value);
	}
}
