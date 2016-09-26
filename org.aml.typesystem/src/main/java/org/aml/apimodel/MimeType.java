package org.aml.apimodel;

import java.util.List;

import org.aml.typesystem.AbstractType;

public interface MimeType {
	
	
	public String getType() ;

	/**
	 * 
	 * @return string formatted example
	 */
	public String getExample();

	public List<INamedParam> getFormParameters() ;

	public AbstractType getTypeModel();

}
