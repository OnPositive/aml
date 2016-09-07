package org.aml.typesystem.java;

import org.aml.typesystem.ITypeModel;

/**
 * <p>QualifiedNamingConvention class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class QualifiedNamingConvention implements ITypeNamingConvention{

	/** {@inheritDoc} */
	@Override
	public String name(ITypeModel mdl) {
		return mdl.getFullyQualifiedName();
	}
	
}
