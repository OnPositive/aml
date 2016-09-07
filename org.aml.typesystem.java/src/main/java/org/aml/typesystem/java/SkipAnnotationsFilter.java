package org.aml.typesystem.java;

import org.aml.typesystem.IAnnotationModel;

/**
 * <p>SkipAnnotationsFilter class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class SkipAnnotationsFilter implements IAnnotationFilter{

	/** {@inheritDoc} */
	@Override
	public boolean preserve(IAnnotationModel mdl) {
		return false;
	}

}
