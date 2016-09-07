package org.aml.typesystem.meta.facets;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

/**
 * <p>Polymorphic class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Polymorphic extends Facet<Boolean>{

	/**
	 * <p>Constructor for Polymorphic.</p>
	 *
	 * @param value a boolean.
	 */
	public Polymorphic(boolean value) {
		super(value,true);
		
	}
	
	/**
	 * <p>Constructor for Polymorphic.</p>
	 */
	public Polymorphic() {
		super(true,true);
		
	}

	/** {@inheritDoc} */
	@Override
	public String facetName() {
		return "polymorphic";
	}

	

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.OBJECT;
	}

}
