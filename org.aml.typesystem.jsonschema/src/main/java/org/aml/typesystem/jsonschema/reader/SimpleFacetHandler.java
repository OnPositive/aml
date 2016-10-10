package org.aml.typesystem.jsonschema.reader;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.FacetRegistry;
import org.aml.typesystem.meta.TypeInformation;
import org.json.JSONObject;

public class SimpleFacetHandler implements IFacetHandler{

	protected String facetName;
	
	public SimpleFacetHandler(Class<? extends TypeInformation> facetType) {
		super();
		this.facetName = FacetRegistry.getFacetName(facetType);
	}

	@Override
	public AbstractType handle(String facetName, Object object, AbstractType derivedType, JSONObject baseObject,
			JSONSchemaParser parser) {
		ISimpleFacet fs=(ISimpleFacet) FacetRegistry.facet(facetName);
		fs.setValue(object);
		derivedType.addMeta((TypeInformation) fs);
		return derivedType;
	}

	@Override
	public String name() {
		return facetName;
	}

}
