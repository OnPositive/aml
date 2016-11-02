package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Annotation;
import org.json.JSONObject;

public class ReadOnlyHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject,
			JSONSchemaParser parser) {
		if (object instanceof Boolean){
		derivedType.addMeta(new Annotation("extras.Readonly", object));
		}
		else {
			throw new IllegalStateException();
		}
		return derivedType;
	}

	@Override
	public String name() {
		return "readOnly";
	}

}
