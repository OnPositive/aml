package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.Status;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.internal.NothingRestriction;
import org.aml.typesystem.meta.facets.internal.NothingRestrictionWithLocation;

public abstract class AbstractRestricton extends TypeInformation {

	private static final ThreadLocal<RestrictionStackEntry> stack = new ThreadLocal<RestrictionStackEntry>() {

		@Override
		protected RestrictionStackEntry initialValue() {
			return new RestrictionStackEntry();
		}

	};

	public AbstractRestricton() {
		super(true);
	}

	public abstract Status check(Object o);

	/**
	 * if this restriction applied together with r0 can be replaced with one
	 * simpler primitive restriction returns this simpler restriction otherwise
	 * null;
	 *
	 * @param r0
	 * @return simpler restriction or null
	 */
	protected abstract AbstractRestricton composeWith(AbstractRestricton restriction);

	protected Status error() {
		return new Status(Status.ERROR, 0, this.toString());
	}

	protected Status error(String message) {
		return new Status(Status.ERROR, 0, message);
	}

	public abstract String facetName();

	protected NothingRestriction nothing(AbstractRestricton another) {
		return new NothingRestrictionWithLocation(stack.get(), "Conflicting restrictions", another);
	}
	
	protected NothingRestriction nothing(AbstractRestricton another,String message) {
		return new NothingRestrictionWithLocation(stack.get(), message, another);
	}

	public String restrictionDescription() {
		return this.toString();
	}
	
	/**
	 * 
	 * @return optimized restrction or this
	 */
	protected AbstractRestricton preoptimize() {
		try {
			AbstractRestricton.stack.set(stack.get().push(this));
			return innerOptimize();
		} finally {
			AbstractRestricton.stack.set(stack.get().pop());
		}
	}

	/**
	 * 
	 * @return optimized version of restriction or this;
	 */
	protected AbstractRestricton innerOptimize() {		
		return this;
	}

	/**
	 * performs attempt to compute composed restriction from this and parameter restriction
	 * @param restriction
	 * @return  composed restriction or null;
	 */
	public AbstractRestricton tryCompose(AbstractRestricton restriction) {
		AbstractRestricton.stack.set(stack.get().push(this));
		try {
			final AbstractRestricton composeWith = composeWith(restriction);

			return composeWith;
		} finally {
			AbstractRestricton.stack.set(stack.get().pop());
		}
	}
}
