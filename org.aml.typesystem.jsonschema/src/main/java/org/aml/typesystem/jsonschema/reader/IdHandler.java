package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.json.JSONObject;

public class IdHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseProperties, JSONSchemaParser parser) {
		//derivedType.addMeta(new DisplayName(""+object));
		return derivedType;
	}

	@Override
	public String name() {
		return "id";
	}

}
