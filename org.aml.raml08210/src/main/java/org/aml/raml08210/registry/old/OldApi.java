package org.aml.raml08210.registry.old;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OldApi {
	protected String logo;
	protected ArrayList<RAMLFile> RAMLFiles;
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	@JsonProperty("RAMLFiles")
	public ArrayList<RAMLFile> getRAMLFiles() {
		return RAMLFiles;
	}
	public void setRAMLFiles(ArrayList<RAMLFile> rAMLFiles) {
		RAMLFiles = rAMLFiles;
	} 
}
