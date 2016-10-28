package org.apigurus;

import java.util.Date;

public class RegistryEntry {

	protected Date added;
	protected String preferred;
	public String getPreferred() {
		return preferred;
	}
	public void setPreferred(String preferred) {
		this.preferred = preferred;
	}
	public Date getAdded() {
		return added;
	}
	public void setAdded(Date added) {
		this.added = added;
	}
	
	public Versions getVersions() {
		return versions;
	}
	public void setVersions(Versions versions) {
		this.versions = versions;
	}
	protected Versions versions;
}
