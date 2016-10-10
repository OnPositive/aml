package org.aml.typesystem.jsonschema.reader;

import java.util.HashSet;

import org.aml.typesystem.AbstractType;
import org.json.JSONArray;
import org.json.JSONObject;

public class PropertiesHandler implements IFacetHandler{

	private static final String REQUIRED = "required";

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseProperties, JSONSchemaParser parser) {
		JSONObject obj=(JSONObject) object;
		HashSet<String>requiredIds=new HashSet<>();
		if (baseProperties.has(REQUIRED)){
			JSONArray ar=(JSONArray) baseProperties.get(REQUIRED);
			for (int i=0;i<ar.length();i++){
				requiredIds.add(ar.getString(i));
			}
		}
		String[] names = JSONObject.getNames(obj);
		if(names!=null){
		for (String id:names){
			JSONObject propertyType = obj.getJSONObject(id);
			AbstractType type = parser.getType(propertyType);
			derivedType.declareProperty(id, type, !requiredIds.contains(id));
		}
		}
		return derivedType;
	}

	@Override
	public String name() {
		return "properties";
	}
}
