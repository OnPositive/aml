package org.aml.registry.model;

public class ToolDescription extends ItemDescription{

	protected String category;
	
	protected boolean needsConfig;
	
	public boolean isNeedsConfig() {
		return needsConfig;
	}

	public void setNeedsConfig(boolean needsConfig) {
		this.needsConfig = needsConfig;
	}

	public String getLibUrl() {
		return libUrl;
	}

	public void setLibUrl(String libUrl) {
		this.libUrl = libUrl;
	}

	protected String libUrl;
	
	public void setCategory(String category) {
		this.category=category;
	}
	
	public String getCategory() {
		return category;
	}
	
}
