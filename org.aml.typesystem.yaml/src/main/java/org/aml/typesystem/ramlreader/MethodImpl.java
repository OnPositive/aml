package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.raml.model.Method;
import org.aml.typesystem.raml.model.Resource;
import org.aml.typesystem.raml.model.Response;
import org.raml.v2.api.model.v10.methods.TraitRef;
import org.raml.v2.api.model.v10.security.SecuritySchemeRef;

public class MethodImpl extends AnnotableImpl implements Method{

	protected String method;
	protected Resource parent;
	protected ArrayList<AbstractType>body=new ArrayList<>();
	protected ArrayList<String>protocols=new ArrayList<>();
	protected ArrayList<TraitRef>is=new ArrayList<>();
	protected ArrayList<SecuritySchemeRef>secured=new ArrayList<>();
	protected String description;
	protected String displayName;
	protected ArrayList<AbstractType>queryParameters=new ArrayList<>();
	protected ArrayList<AbstractType>headers=new ArrayList<>();
	protected AbstractType queryString;
	protected ArrayList<Response>responses;
	
	@Override
	public String method() {
		return method;
	}

	@Override
	public Resource resource() {
		return parent;
	}

	@Override
	public List<AbstractType> body() {
		return body;
	}

	@Override
	public List<String> protocols() {
		return protocols;
	}

	@Override
	public List<TraitRef> is() {
		return is;
	}

	@Override
	public List<SecuritySchemeRef> securedBy() {
		return secured;
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
	public List<AbstractType> queryParameters() {
		return queryParameters;
	}

	@Override
	public List<AbstractType> headers() {
		return headers;
	}

	@Override
	public AbstractType queryString() {
		return queryString;
	}

	@Override
	public List<Response> responses() {
		return responses;
	}

}
