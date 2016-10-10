package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.json.JSONObject;

public class SkipFacetHandler implements IFacetHandler{

	protected String name;
	
	public SkipFacetHandler(String name) {
		super();
		this.name = name;
	}

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject,
			JSONSchemaParser parser) {
		return derivedType;
	}

	@Override
	public String name() {
		return name;
	}

}