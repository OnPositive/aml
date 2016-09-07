package org.aml.typesystem.meta.restrictions;

import java.util.HashSet;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.IArray;

/**
 * <p>UniqueItems class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class UniqueItems extends BooleanRestriction {

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		if (o instanceof IArray) {
			final IArray array = (IArray) o;
			final HashSet<String> str = new HashSet<>();
			for (int i = 0; i < array.length(); i++) {
				str.add(dump(array.item(i)));
			}
			if (str.size() != array.length()) {
				return error();
			}
		}
		return Status.OK_STATUS;
	}

	/**
	 * {@inheritDoc}
	 *
	 * nothing to compose with
	 */
	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof UniqueItems) {
			return this;
		}
		return null;
	}

	private String dump(Object item) {
		return item.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Items should be unique";
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ARRAY;
	}

	/** {@inheritDoc} */
	@Override
	protected String checkValue() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(Object vl) {
		
	}

}
