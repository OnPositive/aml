package org.aml.apimodel;

import java.util.List;
import java.util.Map;

import org.aml.typesystem.AbstractType;

public interface MimeType {
	
	
	public String getType() ;

	public String getExample();

	public Map<String, List<INamedParam>> getFormParameters() ;

	public AbstractType getTypeModel();

}
