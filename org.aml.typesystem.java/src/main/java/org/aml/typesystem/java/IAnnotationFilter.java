package org.aml.typesystem.java;

import org.aml.typesystem.IAnnotationModel;

public interface IAnnotationFilter extends IConfiguarionExtension{
	
	boolean preserve(IAnnotationModel mdl);
}
