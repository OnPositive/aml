package org.aml.typesystem.java;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ITypeModel;

/**
 * <p>IJavaTypeBuilder interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IJavaTypeBuilder {

	/**
	 * <p>getType.</p>
	 *
	 * @param mdl a {@link org.aml.typesystem.ITypeModel} object.
	 * @return a {@link org.aml.typesystem.AbstractType} object.
	 */
	AbstractType getType(ITypeModel mdl);
}
