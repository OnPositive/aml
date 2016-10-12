package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Annotation;
import org.json.JSONObject;

public class AddAnnotationHandler implements IFacetHandler{

	protected String annotationName;
	
	public AddAnnotationHandler(String string) {
		this.annotationName=string;
	}

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject,
			JSONSchemaParser parser) {
		derivedType.addMeta(new Annotation(annotationName, null, null));
		return derivedType;
	}

	@Override
	public String name() {
		return this.annotationName;
	}

}
