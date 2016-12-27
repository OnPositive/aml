package org.aml.registry.model;

public class SupportedLibrary {

	public String getLibraryUrl() {
		return libraryUrl;
	}

	public void setLibraryUrl(String libraryUrl) {
		this.libraryUrl = libraryUrl;
	}

	public String getSupportedItems() {
		return supportedItems;
	}

	public void setSupportedItems(String supportedItems) {
		this.supportedItems = supportedItems;
	}

	protected String libraryUrl;
	
	protected String supportedItems;
}
