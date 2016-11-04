package org.aml.raml08210.registry.old;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RAMLFile {

	String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSpecAuthor() {
		return specAuthor;
	}
	public void setSpecAuthor(String specAuthor) {
		this.specAuthor = specAuthor;
	}
	@JsonProperty("RAMLVersion")
	public String getRAMLVersion() {
		return RAMLVersion;
	}
	public void setRAMLVersion(String rAMLVersion) {
		RAMLVersion = rAMLVersion;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@JsonProperty("raml-1-parser_result")
	public String getRaml_1_parser_result() {
		return raml_1_parser_result;
	}
	public void setRaml_1_parser_result(String raml_1_parser_result) {
		this.raml_1_parser_result = raml_1_parser_result;
	}
	String description;
	String specAuthor;
	String RAMLVersion;
	String location;
	String logo;
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	String raml_1_parser_result;
	String developerPortalURI;
	public String getDeveloperPortalURI() {
		return developerPortalURI;
	}
	public void setDeveloperPortalURI(String developerPortalURI) {
		this.developerPortalURI = developerPortalURI;
	}
	ArrayList<Object>notebooks;
	public ArrayList<Object> getNotebooks() {
		return notebooks;
	}
	public void setNotebooks(ArrayList<Object> notebooks) {
		this.notebooks = notebooks;
	}
}
