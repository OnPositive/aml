package org.aml.apimodel.impl;

import org.aml.apimodel.DocumentationItem;

public class DocumentationItemImpl implements DocumentationItem{

	protected String title;
	protected String content;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
