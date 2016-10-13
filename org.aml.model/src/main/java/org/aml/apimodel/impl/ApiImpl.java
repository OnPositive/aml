
package org.aml.apimodel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aml.apimodel.Api;
import org.aml.apimodel.DocumentationItem;
import org.aml.apimodel.Resource;
import org.aml.apimodel.SecuredByConfig;
import org.aml.typesystem.AbstractType;


public class ApiImpl extends TopLevelModelImpl implements Api {

	protected ArrayList<Resource> resources = new ArrayList<>();
	protected ArrayList<SecuredByConfig> securedBy=new ArrayList<>();
	protected List<DocumentationItem> documentation=new ArrayList<>();
	protected List<String>protocols=new ArrayList<>();
	protected List<String>mediaTypes=new ArrayList<>();
	

	public List<DocumentationItem> getDocumentation() {
		return documentation;
	}

	public ArrayList<SecuredByConfig> getSecuredBy() {
		return securedBy;
	}

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
	@Override
	public String version() {
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
	public List<Resource> resources() {
		return resources;
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

	public void addAnnotationType(AbstractType derive) {
		this.atypes.registerType(derive);
	}

	public void addSecuredBy(SecuredByConfigImpl securedByConfigImpl) {
		securedBy.add(securedByConfigImpl);
	}

	public void setDocumentation(List<DocumentationItem> arrayList) {
		this.documentation=arrayList;	
	}

	@Override
	public List<DocumentationItem> documentation() {
		return documentation;
	}

	public void setProtocols(List<String> protocols) {
		this.protocols=protocols;
	}

	public List<String> getProtocols() {
		return protocols;
	}

	public List<String> getMediaType() {
		return mediaTypes;
	}
	
	public void setMediaTypes(List<String> mediaTypes) {
		this.mediaTypes = mediaTypes;
	}


	
}