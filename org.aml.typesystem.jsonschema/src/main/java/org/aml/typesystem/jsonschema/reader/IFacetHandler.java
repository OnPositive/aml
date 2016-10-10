package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.json.JSONObject;

public interface IFacetHandler {

	AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject, JSONSchemaParser parser);
	
	String name();
}
