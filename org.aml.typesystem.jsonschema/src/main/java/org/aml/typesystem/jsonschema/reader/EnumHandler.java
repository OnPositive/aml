package org.aml.typesystem.jsonschema.reader;

import java.util.Arrays;

import org.aml.typesystem.AbstractType;
import org.json.JSONArray;
import org.json.JSONObject;

public class EnumHandler implements IFacetHandler{

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject,
			JSONSchemaParser parser) {
		JSONArray arr=(JSONArray) object;
		String[] result=new String[arr.length()];
		for (int i=0;i<result.length;i++){
			result[i]=""+arr.get(i);
		}
		derivedType.addMeta(new org.aml.typesystem.meta.restrictions.Enum(Arrays.asList(result)));
		return derivedType;
	}

	@Override
	public String name() {
		return "enum";
	}

}
