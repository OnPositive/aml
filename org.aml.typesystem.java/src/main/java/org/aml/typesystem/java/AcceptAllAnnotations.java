package org.aml.typesystem.java;

import org.aml.typesystem.IAnnotationModel;

/**
 * <p>AcceptAllAnnotations class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class AcceptAllAnnotations implements IAnnotationFilter{

	/** {@inheritDoc} */
	@Override
	public boolean preserve(IAnnotationModel mdl) {
		return true;
	}

}
