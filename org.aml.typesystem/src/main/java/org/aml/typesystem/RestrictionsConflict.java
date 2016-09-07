package org.aml.typesystem;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.aml.typesystem.meta.facets.internal.NothingRestrictionWithLocation;
import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.RestrictionStackEntry;

/**
 * <p>RestrictionsConflict class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RestrictionsConflict extends Status {

	protected final AbstractRestricton conflicting;

	protected final RestrictionStackEntry stack;

	/**
	 * <p>Constructor for RestrictionsConflict.</p>
	 *
	 * @param stack a {@link org.aml.typesystem.meta.restrictions.RestrictionStackEntry} object.
	 * @param another a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	public RestrictionsConflict(RestrictionStackEntry stack, AbstractRestricton another) {
		super(Status.ERROR, Status.RESTRICTIONS_CONFLICT, "Restrictions conflict");
		this.stack = stack;
		this.conflicting = another;
	}

	/**
	 * <p>getConflictDescription.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getConflictDescription() {
		final StringWriter out = new StringWriter();
		final PrintWriter ws = new PrintWriter(out);
		ws.println("Restrictions conflict:");
		ws.println(stack.getResctriction() + " conflicts with " + conflicting);
		ws.println("at");
		ws.println(stack.pop());
		return out.toString();
	}

	/**
	 * <p>Getter for the field <code>conflicting</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	public AbstractRestricton getConflicting() {
		return conflicting;
	}

	/**
	 * <p>Getter for the field <code>stack</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.meta.restrictions.RestrictionStackEntry} object.
	 */
	public RestrictionStackEntry getStack() {
		return stack;
	}

	/**
	 * <p>toRestriction.</p>
	 *
	 * @return a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	public AbstractRestricton toRestriction() {
		return new NothingRestrictionWithLocation(stack, message, conflicting);
	}

}
