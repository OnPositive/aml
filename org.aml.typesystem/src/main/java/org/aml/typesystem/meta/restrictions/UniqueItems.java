package org.aml.typesystem.meta.restrictions;

import java.util.HashSet;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.IArray;

public class UniqueItems extends BooleanRestriction {

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

	@Override
	public String toString() {
		return "Items should be unique";
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.ARRAY;
	}

	@Override
	protected String checkValue() {
		return null;
	}

	@Override
	public void setValue(Object vl) {
		
	}

}
