package org.aml.typesystem.java;

import org.aml.typesystem.ITypeModel;

/**
 * <p>ITypeNamingConvention interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface ITypeNamingConvention  extends IConfiguarionExtension{

	/**
	 * <p>name.</p>
	 *
	 * @param mdl a {@link org.aml.typesystem.ITypeModel} object.
	 * @return a {@link java.lang.String} object.
	 */
	String name(ITypeModel mdl);
}
