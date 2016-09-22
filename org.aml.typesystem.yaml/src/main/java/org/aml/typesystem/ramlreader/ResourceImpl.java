package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.AbstractParam;
import org.aml.apimodel.Action;
import org.aml.apimodel.Resource;
import org.aml.typesystem.AbstractType;
import org.raml.v2.api.model.v10.security.SecuritySchemeRef;

public class ResourceImpl extends AnnotableImpl implements Resource{

	String relativeUri;
	String displayName;
	ArrayList<Resource>resources=new ArrayList<>();
	String resourcePath;
	Resource parent;
	ArrayList<Action>methods=new ArrayList<>();
	ArrayList<AbstractType>uriParameters=new ArrayList<>();
	ArrayList<SecuritySchemeRef>secured=new ArrayList<>();
	String decription;
	
	@Override
	public String relativeUri() {
		return relativeUri;
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
	public String resourcePath() {
		return resourcePath;
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
		return decription;
	}


	@Override
	public List<AbstractType> uriParameters() {
		return this.uriParameters;
	}

	@Override
	public String getUri() {
		return null;
	}

	@Override
	public Resource getParentResource() {
		return null;
	}

	@Override
	public AbstractParam[] getUriParameters() {
		return null;
	}

}
