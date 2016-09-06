package org.aml.typesystem.meta.restrictions;

import java.util.regex.PatternSyntaxException;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.Status;
import org.aml.typesystem.values.ObjectAccess;

public class Pattern extends FacetRestriction<String> {

	private String value;

	public Pattern(String pattern) {
		super();
		this.value = pattern;
	}

	@Override
	public Status check(Object o) {
		final Object val = ObjectAccess.value(o);
		if (val instanceof String) {
			final String str = (String) val;
			if (!str.matches(value)) {
				return new Status(Status.ERROR, 0, "string should match to " + this.value);
			}
		}
		return Status.OK_STATUS;
	}

	@Override
	/**
	 * patterns can not be unified with anything
	 */
	protected AbstractRestricton composeWith(AbstractRestricton restriction) {
		if (restriction instanceof Pattern) {
			final Pattern p1 = (Pattern) restriction;
			if (p1.value.equals(this.value)) {
				return this;
			}
			return  nothing(restriction,"pattern restrictions can not be composed at one type");
		}
		return null;
	}

	@Override
	public String value() {
		return value;
	}

	public String toString(){
		return "should pass reg exp:"+this.value;
	}

	@Override
	public AbstractType requiredType() {
		return BuiltIns.STRING;
	}

	@Override
	protected String checkValue() {
		try {
			
			java.util.regex.Pattern.compile(value);
			return null;
		} catch (final PatternSyntaxException e) {
			return e.getMessage();
		}
	}

	@Override
	public void setValue(Object vl) {
		this.value=(String) vl;
	}
}
