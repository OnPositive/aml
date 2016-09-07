package org.aml.typesystem;

import java.util.List;

/**
 * <p>IGenericElement interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IGenericElement {
	
	/**
	 * <p>getTypeParameters.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<ITypeParameter> getTypeParameters();
}
