package org.aml.java2raml;

import java.util.Collection;

import org.aml.typesystem.ITypeModel;

/**
 * <p>ITypeCollector interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface ITypeCollector {

	/**
	 * <p>gather.</p>
	 *
	 * @param cfg a {@link org.aml.java2raml.Config} object.
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<ITypeModel> gather(Config cfg);
}
