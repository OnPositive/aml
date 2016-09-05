package org.aml.typesystem;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.aml.typesystem.meta.facets.internal.NothingRestrictionWithLocation;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.RestrictionStackEntry;

public class RestrictionsConflict extends Status {

	protected final AbstractRestricton conflicting;

	protected final RestrictionStackEntry stack;

	public RestrictionsConflict(RestrictionStackEntry stack, AbstractRestricton another) {
		super(Status.ERROR, Status.RESTRICTIONS_CONFLICT, "Restrictions conflict");
		this.stack = stack;
		this.conflicting = another;
	}

	public String getConflictDescription() {
		final StringWriter out = new StringWriter();
		final PrintWriter ws = new PrintWriter(out);
		ws.println("Restrictions conflict:");
		ws.println(stack.getResctriction() + " conflicts with " + conflicting);
		ws.println("at");
		ws.println(stack.pop());
		return out.toString();
	}

	public AbstractRestricton getConflicting() {
		return conflicting;
	}

	public RestrictionStackEntry getStack() {
		return stack;
	}

	public AbstractRestricton toRestriction() {
		return new NothingRestrictionWithLocation(stack, message, conflicting);
	}

}
