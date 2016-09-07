package org.aml.typesystem.meta.facets.internal;

import org.aml.typesystem.meta.restrictions.AbstractRestricton;
import org.aml.typesystem.meta.restrictions.RestrictionStackEntry;

/**
 * <p>NothingRestrictionWithLocation class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class NothingRestrictionWithLocation extends NothingRestriction {

	protected AbstractRestricton another;
	protected String message;
	protected RestrictionStackEntry stack;

	/**
	 * <p>Constructor for NothingRestrictionWithLocation.</p>
	 *
	 * @param stack a {@link org.aml.typesystem.meta.restrictions.RestrictionStackEntry} object.
	 * @param message a {@link java.lang.String} object.
	 * @param another a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	public NothingRestrictionWithLocation(RestrictionStackEntry stack, String message, AbstractRestricton another) {
		this.stack = stack;
		this.another = another;
		this.message = message;
	}

	/**
	 * <p>another.</p>
	 *
	 * @return a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	public AbstractRestricton another() {
		return this.another;
	}

	/**
	 * <p>Getter for the field <code>message</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * <p>Getter for the field <code>stack</code>.</p>
	 *
	 * @return a {@link org.aml.typesystem.meta.restrictions.RestrictionStackEntry} object.
	 */
	public RestrictionStackEntry getStack() {
		return stack;
	}

}
