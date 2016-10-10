package org.aml.apimodel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.Resource;

public class ResourceImpl extends AnnotableImpl implements Resource{

	protected ArrayList<Resource>resources=new ArrayList<>();
	protected ArrayList<Action>methods=new ArrayList<>();
	protected ArrayList<INamedParam>uriParams=new ArrayList<>();
	protected ResourceImpl parent;
	protected String relativeUrl;
	protected String displayName;
	protected String description;
	protected Api api;
	
	public ResourceImpl(String relative) {
		this.relativeUrl=relative;
	}

	@Override
	public String relativeUri() {
		return relativeUrl;
	}

	@Override
	public String displayName() {
		return displayName;
	}

	@Override
	public List<Resource> resources() {
		return resources;
	}

	@Override
	public Resource parentResource() {
		return parent;
	}

	@Override
	public List<Action> methods() {
		return methods;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public List<? extends INamedParam> uriParameters() {
		return uriParams;
	}

	@Override
	public String getUri() {
		if (this.parent!=null){
			return this.parent.getUri()+this.relativeUri();
		}
		return this.relativeUri();
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
			resource.parent=this;
			this.resources.add(resource);
			return resource;
		});
	}

	public ActionImpl getOrCreateMethod(String httpMethod) {
		Optional<Action> findFirst = this.methods.stream().filter(x -> x.method().equals(httpMethod)).findFirst();
		return (ActionImpl) findFirst.orElseGet(()->{
			ActionImpl res=new ActionImpl(httpMethod);
			res.resource=this;
			this.methods.add(res);
			return res;
		});		
	}
	
	@Override
	public String toString() {
		return this.getUri();
	}

	@Override
	public Api getApi() {
		if (this.parent!=null){
			return this.parent.getApi();
		}
		return api;
	}

	public void addUriParameter(NamedParamImpl namedParamImpl) {
		this.uriParams.add(namedParamImpl);
	}
}