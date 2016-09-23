package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.NamedParam;
import org.aml.apimodel.Action;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.typesystem.AbstractType;

public class MethodImpl extends AnnotableImpl implements Action{

	protected String method;
	protected Resource parent;
	protected ArrayList<MimeType>body=new ArrayList<>();
	protected ArrayList<String>protocols=new ArrayList<>();
	protected String description;
	protected String displayName;
	protected ArrayList<NamedParam>queryParameters=new ArrayList<>();
	protected ArrayList<NamedParam>headers=new ArrayList<>();
	protected AbstractType queryString;
	protected ArrayList<Response>responses;
	protected ArrayList<String>is=new ArrayList<>();
	
	@Override
	public String method() {
		return method;
	}

	@Override
	public Resource resource() {
		return parent;
	}

	@Override
	public List<org.aml.apimodel.MimeType> body() {
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
	public List<NamedParam> queryParameters() {
		return queryParameters;
	}

	@Override
	public List<NamedParam> headers() {
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

	@Override
	public Resource getResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasBody() {
		return false;
	}

	@Override
	public ArrayList<String> getIs() {
		return is;
	}

}
