package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.FacetRegistry;

/**
 * <p>Abstract FacetRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class FacetRestriction<T> extends AbstractRestricton implements ISimpleFacet {

	/**
	 * <p>Constructor for FacetRestriction.</p>
	 */
	public FacetRestriction() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public final String facetName() {
		return FacetRegistry.getFacetName(this.getClass());
	}
	
	/** {@inheritDoc} */
	@Override
	public abstract AbstractType requiredType();

	/** {@inheritDoc} */
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
	
	/**
	 * <p>checkValue.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected abstract String checkValue();

	/** {@inheritDoc} */
	@Override
	public abstract T value();
}
