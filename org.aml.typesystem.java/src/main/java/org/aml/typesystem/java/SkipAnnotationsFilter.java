package org.aml.typesystem.java;

import org.aml.typesystem.IAnnotationModel;

public class SkipAnnotationsFilter implements IAnnotationFilter{

	@Override
	public boolean preserve(IAnnotationModel mdl) {
		return false;
	}

}
