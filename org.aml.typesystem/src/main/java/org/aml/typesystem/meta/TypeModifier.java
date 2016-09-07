package org.aml.typesystem.meta;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.Status;

/**
 * <p>TypeModifier class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class TypeModifier extends TypeInformation {

	/**
	 * <p>Constructor for TypeModifier.</p>
	 */
	public TypeModifier() {
		super(false);
	}
	
	/** {@inheritDoc} */
	@Override
	public AbstractType requiredType() {
		return BuiltIns.ANY;
	}

	/** {@inheritDoc} */
	@Override
	public Status validate(ITypeRegistry registry) {
		return Status.OK_STATUS;
	}

}
