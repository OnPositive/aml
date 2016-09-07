package org.aml.typesystem.java;

import org.aml.typesystem.IFieldModel;
import org.aml.typesystem.IMember;

/**
 * <p>FieldMemberFilter class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class FieldMemberFilter implements IMemberFilter{

	/** {@inheritDoc} */
	@Override
	public boolean accept(IMember member) {		
		return member instanceof IFieldModel;
	}

}
