package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.restrictions.KnownPropertyRestricton;
import org.json.JSONObject;

public class AdditionalPropertiesHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject,
			JSONSchemaParser parser) {
		if (object!=null&&Boolean.FALSE.equals(object)){
		derivedType.addMeta(new KnownPropertyRestricton());
		}
		return derivedType;
	}

	@Override
	public String name() {
		return "additionalProperties";
	}

}
