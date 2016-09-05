package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.RestrictionStackEntry;

public class NothingRestrictionWithLocation extends NothingRestriction {

	protected AbstractRestricton another;
	protected String message;
	protected RestrictionStackEntry stack;

	public NothingRestrictionWithLocation(RestrictionStackEntry stack, String message, AbstractRestricton another) {
		this.stack = stack;
		this.another = another;
		this.message = message;
	}

	public AbstractRestricton another() {
		return this.another;
	}

	public String getMessage() {
		return message;
	}

	public RestrictionStackEntry getStack() {
		return stack;
	}

}
