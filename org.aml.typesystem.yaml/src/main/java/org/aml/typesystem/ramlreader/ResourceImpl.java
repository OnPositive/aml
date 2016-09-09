package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.raml.model.Method;
import org.aml.typesystem.raml.model.Resource;
import org.raml.v2.api.model.v10.security.SecuritySchemeRef;

public class ResourceImpl extends AnnotableImpl implements Resource{

	String relativeUri;
	String displayName;
	ArrayList<Resource>resources=new ArrayList<>();
	String resourcePath;
	Resource parent;
	ArrayList<Method>methods=new ArrayList<>();
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
	public List<Method> methods() {
		return methods;
	}

	@Override
	public String description() {
		return decription;
	}

	@Override
	public List<SecuritySchemeRef> securedBy() {
		return secured;
	}

	@Override
	public List<AbstractType> uriParameters() {
		return this.uriParameters;
	}

}
