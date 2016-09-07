package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.facets.internal.InstanceOfRestriction;
import org.aml.typesystem.meta.facets.internal.InternalRestriction;
import org.aml.typesystem.values.IArray;

/**
 * <p>IsArrayRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class IsArrayRestriction extends InternalRestriction {

	/** Constant <code>INSTANCE</code> */
	public static final IsArrayRestriction INSTANCE = new IsArrayRestriction();

	private IsArrayRestriction() {
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		if (o == null || o.getClass().isArray() || IArray.class.isInstance(o)) {
			return Status.OK_STATUS;
		}
		return error();
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof IsObjectRestriction) {
			return nothing(restriction);
		}

		if (restriction instanceof InstanceOfRestriction) {
			return nothing(restriction);
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "isArray";
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "should be array";
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
