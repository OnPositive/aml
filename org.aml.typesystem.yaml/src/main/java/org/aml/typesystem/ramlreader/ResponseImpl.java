package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.AbstractParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Response;

public class ResponseImpl extends AnnotableImpl implements Response{

	protected String code;
	protected ArrayList<AbstractParam>headers=new ArrayList<>();
	protected ArrayList<MimeType> body=new ArrayList<>();
	protected String description;
	
	@Override
	public String code() {
		return code;
	}

	@Override
	public List<AbstractParam> headers() {
		return headers;
	}

	@Override
	public List<MimeType> body() {
		return body;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public boolean hasBody() {
		return !body.isEmpty();
	}

}
