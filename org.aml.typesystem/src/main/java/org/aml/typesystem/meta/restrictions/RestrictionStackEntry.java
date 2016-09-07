package org.aml.typesystem.meta.restrictions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>RestrictionStackEntry class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RestrictionStackEntry {
	protected String id;
	protected RestrictionStackEntry previous;
	protected AbstractRestricton restriction;

	/**
	 * <p>Constructor for RestrictionStackEntry.</p>
	 */
	public RestrictionStackEntry() {
		this.id = "top";
	}

	/**
	 * <p>Constructor for RestrictionStackEntry.</p>
	 *
	 * @param previous a {@link org.aml.typesystem.meta.restrictions.RestrictionStackEntry} object.
	 * @param restriction a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 * @param id a {@link java.lang.String} object.
	 */
	public RestrictionStackEntry(RestrictionStackEntry previous, AbstractRestricton restriction, String id) {
		super();
		this.previous = previous;
		this.restriction = restriction;
		this.id = id;
	}

	/**
	 * <p>getResctriction.</p>
	 *
	 * @return a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	public AbstractRestricton getResctriction() {
		return this.restriction;
	}

	/**
	 * <p>pop.</p>
	 *
	 * @return a {@link org.aml.typesystem.meta.restrictions.RestrictionStackEntry} object.
	 */
	public RestrictionStackEntry pop() {
		return previous;
	}

	/**
	 * <p>push.</p>
	 *
	 * @param r a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 * @return a {@link org.aml.typesystem.meta.restrictions.RestrictionStackEntry} object.
	 */
	public RestrictionStackEntry push(AbstractRestricton r) {
		return new RestrictionStackEntry(this, r, r.restrictionDescription());
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringWriter out = new StringWriter();
		final PrintWriter rs = new PrintWriter(out);
		if (this.restriction != null) {
			rs.println(
					this.restriction + (restriction.ownerType() != null ? " (" + restriction.ownerType() + ")" : ""));
		}
		if (this.previous != null && previous.restriction != null) {
			rs.println(previous.toString());
		}
		return out.toString();
	}
}
