package org.aml.typesystem.jsonschema.reader;

import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Annotation;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnnotationsHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject,
			JSONSchemaParser parser) {
		
		if (object instanceof JSONObject){
			JSONObject obj=(JSONObject) object;
			Object value = obj.get("required");
			if (value instanceof JSONArray){
				value=((JSONArray) value).toList();
			}
			derivedType.addMeta(new Annotation("extras.RequiredIn", value));
		}
		else{
			throw new IllegalStateException();
		}
		return derivedType;
	}

	@Override
	public String name() {
		return "annotations";
	}

}
