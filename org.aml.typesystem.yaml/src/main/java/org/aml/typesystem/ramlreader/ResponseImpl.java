package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.raml.model.Response;

public class ResponseImpl extends AnnotableImpl implements Response{

	protected String code;
	protected ArrayList<AbstractType>headers=new ArrayList<>();
	protected ArrayList<AbstractType> body=new ArrayList<>();
	protected String description;
	
	@Override
	public String code() {
		return code;
	}

	@Override
	public List<AbstractType> headers() {
		return headers;
	}

	@Override
	public List<AbstractType> body() {
		return body;
	}

	@Override
	public String description() {
		return description;
	}

}
