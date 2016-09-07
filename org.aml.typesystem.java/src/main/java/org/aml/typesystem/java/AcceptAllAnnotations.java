package org.aml.typesystem.java;

import org.aml.typesystem.IAnnotationModel;

public class AcceptAllAnnotations implements IAnnotationFilter{

	@Override
	public boolean preserve(IAnnotationModel mdl) {
		return true;
	}

}
