package org.aml.typesystem.java;

import org.aml.typesystem.IMember;

/**
 * <p>NoneFilter class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class NoneFilter implements IMemberFilter{

	/** {@inheritDoc} */
	@Override
	public boolean accept(IMember member) {
		return false;
	}

}
