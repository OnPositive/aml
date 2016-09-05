package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.FacetRegistry;

public abstract class FacetRestriction<T> extends AbstractRestricton implements ISimpleFacet {

	public FacetRestriction() {
		super();
	}

	@Override
	public final String facetName() {
		return FacetRegistry.getFacetName(this.getClass());
	}
	
	public abstract AbstractType requiredType();

	@Override
	public final Status validate(ITypeRegistry registry) {
		if (!ownerType.isSubTypeOf(requiredType())) {
			return error(facetName()+" facet can only be used with "+requiredType().name()+" types");
		}
		String checkValue = checkValue();
		if (checkValue!=null){
			return error(checkValue);
		}
		return Status.OK_STATUS;
	}
	
	protected abstract String checkValue();

	@Override
	public abstract T value();
}