package org.aml.typesystem.meta.restrictions;

import org.aml.typesystem.RestrictionsConflict;
import org.aml.typesystem.Status;
import org.aml.typesystem.meta.IHasType;
import org.aml.typesystem.meta.facets.internal.NothingRestrictionWithLocation;

/**
 * <p>Abstract TypedRestriction class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class  TypedRestriction extends AbstractRestricton implements IHasType{

	
	/** {@inheritDoc} */
	@Override
	protected AbstractRestricton innerOptimize() {
		if (this.range()==null){
			return this;
		}
		final Status checkConfluent = this.range().checkConfluent();
		
		if (!checkConfluent.isOk()){
			final RestrictionsConflict restrictionsConflict = (RestrictionsConflict)checkConfluent;
			return new NothingRestrictionWithLocation(restrictionsConflict.getStack(), checkConfluent.getMessage(), restrictionsConflict.getConflicting());
		}
		return super.innerOptimize();
	}
}
