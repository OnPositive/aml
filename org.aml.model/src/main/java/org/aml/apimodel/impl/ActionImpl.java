package org.aml.apimodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Action;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.apimodel.SecuredByConfig;
import org.aml.typesystem.AbstractType;

public class ActionImpl extends AnnotableImpl implements Action{

	protected String httpMethod;
	protected Resource resource;
	protected String description;
	protected String displayName;
	protected ArrayList<INamedParam>headers=new ArrayList<>();
	protected ArrayList<Response>responses=new ArrayList<>();
	protected ArrayList<INamedParam>queryParameters=new ArrayList<>();
	protected ArrayList<String>protocols=new ArrayList<>();
	protected ArrayList<String>traits=new ArrayList<>();
	protected ArrayList<MimeType>body=new ArrayList<>();
	protected ArrayList<SecuredByConfig>securedBy=new ArrayList<>();
	
	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public ActionImpl(String httpMethod) {
		this.httpMethod=httpMethod;
	}

	@Override
	public String method() {
		return httpMethod;
	}

	@Override
	public Resource resource() {
		return resource;
	}

	@Override
	public List<MimeType> body() {
		return body;
	}

	@Override
	public List<String> protocols() {
		return protocols;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public String displayName() {
		return displayName;
	}

	@Override
	public List<INamedParam> queryParameters() {
		return queryParameters;
	}

	@Override
	public List<INamedParam> headers() {
		return headers;
	}

	@Override
	public List<Response> responses() {
		return responses;
	}

	@Override
	public boolean hasBody() {
		return !this.body().isEmpty();
	}

	@Override
	public ArrayList<String> getIs() {
		return traits;
	}
	@Override
	public String toString() {
		if (this.resource!=null){
			return this.resource.getUri()+":"+this.httpMethod;
		}
		return this.httpMethod;
	}

	public ResponseImpl addResponse(String code, String mime, AbstractType type) {
		ResponseImpl e = new ResponseImpl(code);
		MimeTypeImpl e2 = new MimeTypeImpl(type, this,mime);
		e2.owningResponse=e;
		e.mimeType.add(e2);
		this.responses.add(e);
		return e;
	}
	
	public void addBody(String mime, AbstractType type) {
		MimeTypeImpl e2 = new MimeTypeImpl(type, this,mime);
		this.body.add(e2);
	}

	public void addQueryParameter(NamedParamImpl namedParamImpl) {
		this.queryParameters.add(namedParamImpl);
	}

	@Override
	public ArrayList<SecuredByConfig> securedBy() {
		return securedBy;
	}

	public void addSecurityScopes(String name, String[] scopes) {
		SecuredByConfigImpl e = new SecuredByConfigImpl(name);
		e.settings().put("scopes", scopes);
		securedBy.add(e);	
	}

	@Override
	public String name() {
		return method();
	}
}