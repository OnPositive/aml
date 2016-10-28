package org.aml.registry.model;

import java.util.ArrayList;

public class Registry {

	protected String name="Registry";
	protected String title="Registry";
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	protected String version="v1";
	
	protected ArrayList<ApiDescription> apis=new ArrayList<ApiDescription>();
	protected ArrayList<ApiDescription> libraries=new ArrayList<ApiDescription>();
	protected ArrayList<SubRegistryDescription>includes=new ArrayList<SubRegistryDescription>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public ArrayList<ApiDescription> getApis() {
		return apis;
	}
	public void setApis(ArrayList<ApiDescription> apis) {
		this.apis = apis;
	}
	public ArrayList<ApiDescription> getLibraries() {
		return libraries;
	}
	public void setLibraries(ArrayList<ApiDescription> libraries) {
		this.libraries = libraries;
	}
	public ArrayList<SubRegistryDescription> getIncludes() {
		return includes;
	}
	public void setIncludes(ArrayList<SubRegistryDescription> includes) {
		this.includes = includes;
	}  
	
}
