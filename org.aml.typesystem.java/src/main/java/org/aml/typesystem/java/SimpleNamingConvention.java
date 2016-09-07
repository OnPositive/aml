package org.aml.typesystem.java;

import org.aml.typesystem.ITypeModel;

/**
 * <p>SimpleNamingConvention class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class SimpleNamingConvention implements ITypeNamingConvention{

	/** {@inheritDoc} */
	@Override
	public String name(ITypeModel mdl) {
		return mdl.getName();
	}

}
