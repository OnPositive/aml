
package org.aml.apimodel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aml.apimodel.Api;
import org.aml.apimodel.Resource;
import org.aml.typesystem.AbstractType;

public class ApiImpl extends TopLevelModelImpl implements Api {

	protected ArrayList<Resource> resources = new ArrayList<>();
	protected String title;
	protected String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	protected String version;

	public List<Resource> getResources() {
		return resources;
	}

	@Override
	public Resource[] resources() {
		return resources.toArray(new Resource[resources.size()]);
	}

	public void addType(AbstractType t) {
		this.types.registerType(t);
	}

	public ResourceImpl getOrCreateResource(String path) {
		int p=path.indexOf('/');
		ResourceImpl rs=this.getOrCreateResourceWithSegment(path.substring(0, p!=-1?p:path.length()));
		if (p!=-1){
			return rs.getOrCreateResource(path.substring(p+1));
		}
		return rs;
	}

	private ResourceImpl getOrCreateResourceWithSegment(String string) {
		Optional<Resource> findFirst = this.resources.stream().filter(x -> x.relativeUri().equals("/"+string)).findFirst();
		return (ResourceImpl) findFirst.orElseGet(() -> {
			ResourceImpl resource = new ResourceImpl("/"+string);
			resource.api=this;
			this.resources.add(resource);
			return resource;
		});
	}

	@Override
	public String title() {
		return title;
	}

}