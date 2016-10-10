package org.aml.typesystem.jsonschema.reader;

import org.json.JSONObject;

public class ObjectPropertiesResolver implements IReferenceResolver{

	protected JSONObject baseObject;
	
	public ObjectPropertiesResolver(JSONObject baseObject) {
		super();
		this.baseObject = baseObject;
	}

	@Override
	public JSONObject resolveReference(String reference) {
		return baseObject.getJSONObject(reference);
	}

}
