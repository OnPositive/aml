package org.aml.typesystem.jsonschema.reader;

import org.json.JSONObject;

public interface IReferenceResolver {

	JSONObject resolveReference(String reference);
}
