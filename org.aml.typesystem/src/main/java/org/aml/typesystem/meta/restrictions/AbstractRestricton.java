package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.Status;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.internal.NothingRestriction;
import org.aml.typesystem.meta.facets.internal.NothingRestrictionWithLocation;

/**
 * <p>Abstract AbstractRestricton class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class AbstractRestricton extends TypeInformation {

	private static final ThreadLocal<RestrictionStackEntry> stack = new ThreadLocal<RestrictionStackEntry>() {

		@Override
		protected RestrictionStackEntry initialValue() {
			return new RestrictionStackEntry();
		}

	};

	/**
	 * <p>Constructor for AbstractRestricton.</p>
	 */
	public AbstractRestricton() {
		super(true);
	}

	/**
	 * <p>check.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	public abstract Status check(Object o);

	/**
	 * if this restriction applied together with r0 can be replaced with one
	 * simpler primitive restriction returns this simpler restriction otherwise
	 * null;
	 *
	 * @return simpler restriction or null
	 * @param restriction a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 */
	protected abstract AbstractRestricton composeWith(AbstractRestricton restriction);

	/**
	 * <p>error.</p>
	 *
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	protected Status error() {
		return new Status(Status.ERROR, 0, this.toString());
	}

	/**
	 * <p>error.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.Status} object.
	 */
	protected Status error(String message) {
		return new Status(Status.ERROR, 0, message);
	}

	/**
	 * <p>facetName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String facetName();

	/**
	 * <p>nothing.</p>
	 *
	 * @param another a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 * @return a {@link org.aml.typesystem.meta.facets.internal.NothingRestriction} object.
	 */
	protected NothingRestriction nothing(AbstractRestricton another) {
		return new NothingRestrictionWithLocation(stack.get(), "Conflicting restrictions", another);
	}
	
	/**
	 * <p>nothing.</p>
	 *
	 * @param another a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
	 * @param message a {@link java.lang.String} object.
	 * @return a {@link org.aml.typesystem.meta.facets.internal.NothingRestriction} object.
	 */
	protected NothingRestriction nothing(AbstractRestricton another,String message) {
		return new NothingRestrictionWithLocation(stack.get(), message, another);
	}

	/**
	 * <p>restrictionDescription.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String restrictionDescription() {
		return this.toString();
	}
	
	/**
	 * <p>preoptimize.</p>
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
	 * <p>innerOptimize.</p>
	 *
	 * @return optimized version of restriction or this;
	 */
	protected AbstractRestricton innerOptimize() {		
		return this;
	}

	/**
	 * performs attempt to compute composed restriction from this and parameter restriction
	 *
	 * @param restriction a {@link org.aml.typesystem.meta.restrictions.AbstractRestricton} object.
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
