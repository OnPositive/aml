package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.json.JSONObject;

public class ItemsHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseProperties, JSONSchemaParser parser) {
		JSONObject obj=(JSONObject) object;		
		AbstractType type = parser.getType(obj);
		derivedType.addMeta(new ComponentShouldBeOfType(type));
		return derivedType;
	}

	@Override
	public String name() {
		return "items";
	}
}
