package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Format;
import org.json.JSONObject;

public class FormatHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseProperties, JSONSchemaParser parser) {
		derivedType.addMeta(new Format(""+object));
		return derivedType;
	}

	@Override
	public String name() {
		return "format";
	}

}
