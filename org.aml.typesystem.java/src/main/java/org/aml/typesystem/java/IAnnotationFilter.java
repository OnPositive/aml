package org.aml.typesystem.java;

import org.aml.typesystem.IAnnotationModel;

public interface IAnnotationFilter {
	
	boolean preserve(IAnnotationModel mdl);
}
