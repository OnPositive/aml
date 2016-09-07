package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.facets.Facet;

/**
 * <p>OriginalName class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class OriginalName extends Facet<String>{

	/**
	 * <p>Constructor for OriginalName.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public OriginalName(String value) {
		super(value);
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "name";
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
