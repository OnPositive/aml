package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

/**
 * <p>Description class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Description extends Facet<String> {

	/**
	 * <p>Constructor for Description.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public Description(String value) {
		super(value, false);
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "description";
	}


	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}
}
