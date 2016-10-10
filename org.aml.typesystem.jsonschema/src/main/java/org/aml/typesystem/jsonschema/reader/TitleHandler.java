package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.DisplayName;
import org.json.JSONObject;

public class TitleHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseProperties, JSONSchemaParser parser) {
		derivedType.addMeta(new DisplayName(""+object));
		return derivedType;
	}

	@Override
	public String name() {
		return "title";
	}

}
