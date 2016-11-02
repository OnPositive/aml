package org.aml.registry.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.raml.v2.internal.utils.StreamUtils;

public class ItemDescription implements Cloneable {

	protected String name;
	protected String org;
	protected String version;
	protected String description;
	protected String icon;
	protected String location;
	protected String kind;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String contents() {
		if (location.startsWith("http://") || location.startsWith("https://")) {
			try {
				return StreamUtils.toString(new URL(location).openStream());
			} catch (FileNotFoundException e) {
				throw new IllegalStateException(e);
			} catch (MalformedURLException e) {
				throw new IllegalStateException(e);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		try {
			return FileUtils.readFileToString(new File(location));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
