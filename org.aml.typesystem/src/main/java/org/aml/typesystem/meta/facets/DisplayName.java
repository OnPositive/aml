package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

/**
 * <p>DisplayName class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class DisplayName extends Facet<String> {

	/**
	 * <p>Constructor for DisplayName.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public DisplayName(String value) {
		super(value, false);
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "displayName";
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
