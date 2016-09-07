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

/**
 * <p>Enum class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Enum extends FacetRestriction<ArrayList<String>> {

	private ArrayList<String> values = new ArrayList<>();

	/**
	 * <p>Constructor for Enum.</p>
	 *
	 * @param vls a {@link java.util.Collection} object.
	 */
	public Enum(Collection<String> vls) {
		this.values.addAll(vls);
	}

	/** {@inheritDoc} */
	@Override
	public Status check(Object o) {
		final Object val = ObjectAccess.value(o);
		if (val != null && !values.contains(val.toString())) {
			return error();
		}
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "value should be one of:" + this.values;
	}



	/** {@inheritDoc} */
	@Override
	public ArrayList<String> value() {
		return values;
	}

	/**
	 * <p>options.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> options() {
		return values;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.SCALAR;
	}

	/** {@inheritDoc} */
	@Override
	protected String checkValue() {
		if (new HashSet<>(values).size() < values.size()) {
			return "enum facet can only contain unique items";
		}
		return null;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object vl) {
		this.values=(ArrayList<String>) vl;
	}

}
