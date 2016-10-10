package org.aml.typesystem.jsonschema.reader;

import org.json.JSONObject;

public class SimplePathReferenceResolver implements IReferenceResolver{

	JSONObject base;
	
	public SimplePathReferenceResolver(JSONObject base) {
		super();
		this.base = base;
	}

	@Override
	public JSONObject resolveReference(String reference) {
		String[] split = reference.split("/");
		JSONObject result=base;
		for (String s:split){
			if (s.equals("#")){
				continue;
			}
			if (result.has(s)){
				result=result.getJSONObject(s);
			}
			else{
				throw new IllegalStateException("Can not resolve reference:"+reference);
			}
		}
		return result;
	}

}
