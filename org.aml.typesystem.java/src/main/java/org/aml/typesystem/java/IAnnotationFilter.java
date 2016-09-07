package org.aml.typesystem.java;

import org.aml.typesystem.IAnnotationModel;

/**
 * <p>IAnnotationFilter interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IAnnotationFilter extends IConfiguarionExtension{
	
	/**
	 * <p>preserve.</p>
	 *
	 * @param mdl a {@link org.aml.typesystem.IAnnotationModel} object.
	 * @return a boolean.
	 */
	boolean preserve(IAnnotationModel mdl);
}
