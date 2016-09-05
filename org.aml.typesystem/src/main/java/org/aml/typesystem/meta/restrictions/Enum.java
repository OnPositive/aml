package org.aml.typesystem.meta.restrictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

public class Enum extends FacetRestriction<ArrayList<String>> {

	private final ArrayList<String> values = new ArrayList<>();

	public Enum(Collection<String> vls) {
		this.values.addAll(vls);
	}

	@Override
	public Status check(Object o) {
		final Object val = ObjectAccess.value(o);
		if (val != null && !values.contains(val.toString())) {
			return error();
		}
		return Status.OK_STATUS;
	}

	@Override
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof Enum) {
			final Enum er = (Enum) restriction;
			final LinkedHashSet<String> vls = new LinkedHashSet<String>(this.values);
			vls.retainAll(er.values);
			if (vls.isEmpty()) {
				return nothing(restriction);
			} else {
				return new Enum(vls);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "value should be one of:" + this.values;
	}



	@Override
	public ArrayList<String> value() {
		return values;
	}

	public List<String> options() {
		return values;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.SCALAR;
	}

	@Override
	protected String checkValue() {
		if (new HashSet<>(values).size() < values.size()) {
			return "enum facet can only contain unique items";
		}
		return null;
	}

}
