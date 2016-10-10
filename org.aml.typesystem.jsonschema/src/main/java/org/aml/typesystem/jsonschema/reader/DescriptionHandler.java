package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Description;
import org.json.JSONObject;

public class DescriptionHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseProperties, JSONSchemaParser parser) {
		derivedType.addMeta(new Description(""+object));
		return derivedType;
	}

	@Override
	public String name() {
		return "description";
	}

}
