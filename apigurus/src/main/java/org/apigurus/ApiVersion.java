package org.apigurus;

import java.util.Date;

public class ApiVersion {
	protected Date added;
	public Date getAdded() {
		return added;
	}
	public void setAdded(Date added) {
		this.added = added;
	}
	public String getSwaggerUrl() {
		return swaggerUrl;
	}
	public void setSwaggerUrl(String swaggerUrl) {
		this.swaggerUrl = swaggerUrl;
	}
	public String getSwaggerYamlUrl() {
		return swaggerYamlUrl;
	}
	public void setSwaggerYamlUrl(String swaggerYamlUrl) {
		this.swaggerYamlUrl = swaggerYamlUrl;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	protected String swaggerUrl;
	protected String swaggerYamlUrl;
	protected Date updated;
	protected Info info;
	protected HasUrl externalDocs;
	public HasUrl getExternalDocs() {
		return externalDocs;
	}
	public void setExternalDocs(HasUrl externalDocs) {
		this.externalDocs = externalDocs;
	}
}
