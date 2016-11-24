package org.aml.registry.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.aml.apimodel.TopLevelModel;
import org.aml.registry.internal.LocalRegistry;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.apache.commons.io.FileUtils;
import org.raml.v2.api.loader.CompositeResourceLoader;
import org.raml.v2.api.loader.FileResourceLoader;
import org.raml.v2.api.loader.ResourceLoader;
import org.raml.v2.internal.utils.StreamUtils;

public class ItemDescription implements Cloneable {

	protected String name;
	protected String org;
	protected String version;
	protected String description;
	protected String icon;
	protected String location;
	protected String kind;
	protected String originalLocation;
	protected String originalSpecLanguage;

	public String getOriginalSpecLanguage() {
		return originalSpecLanguage;
	}

	public void setOriginalSpecLanguage(String originalLanguage) {
		this.originalSpecLanguage = originalLanguage;
	}

	public String getOriginalLocation() {
		return originalLocation;
	}

	public void setOriginalLocation(String originalLocation) {
		this.originalLocation = originalLocation;
	}

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

	transient LocalRegistry localRegistry;

	public LocalRegistry getLocalRegistry() {
		return localRegistry;
	}

	public void setLocalRegistry(LocalRegistry localRegistry) {
		this.localRegistry = localRegistry;
	}

	public TopLevelModel resolve() {
		if (this.localRegistry != null) {
			ResourceLoader resourceLoader = new ResourceLoader() {

				public InputStream fetchResource(String resourceName) {
					return localRegistry.get(resourceName);
				}
			};
			return new TopLevelRamlModelBuilder().build(contents(),
					new CompositeResourceLoader(new FileResourceLoader(new File(location).getParent()), resourceLoader),
					location);
		}
		return new TopLevelRamlModelBuilder().build(contents(),
				new CompositeResourceLoader(new FileResourceLoader(new File(location).getParent())), location);
	}

}