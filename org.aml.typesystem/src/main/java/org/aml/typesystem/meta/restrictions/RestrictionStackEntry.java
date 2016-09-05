package org.aml.typesystem.meta.restrictions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RestrictionStackEntry {
	protected String id;
	protected RestrictionStackEntry previous;
	protected AbstractRestricton restriction;

	public RestrictionStackEntry() {
		this.id = "top";
	}

	public RestrictionStackEntry(RestrictionStackEntry previous, AbstractRestricton restriction, String id) {
		super();
		this.previous = previous;
		this.restriction = restriction;
		this.id = id;
	}

	public AbstractRestricton getResctriction() {
		return this.restriction;
	}

	public RestrictionStackEntry pop() {
		return previous;
	}

	public RestrictionStackEntry push(AbstractRestricton r) {
		return new RestrictionStackEntry(this, r, r.restrictionDescription());
	}

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