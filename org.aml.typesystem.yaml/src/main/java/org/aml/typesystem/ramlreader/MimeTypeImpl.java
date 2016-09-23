package org.aml.typesystem.ramlreader;

import java.util.List;
import java.util.Map;

import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.typesystem.AbstractType;

public class MimeTypeImpl implements MimeType{
	
	protected AbstractType model;

	public MimeTypeImpl(NamedParam p) {
		this.model=p.getTypeModel();
	}

	public String getType() {
		return model.name();
	}
	
	public String getExample() {
		return null;
	}

	public Map<String, List<INamedParam>> getFormParameters() {
		return null;
	}

	public AbstractType getTypeModel() {
		return model;
	}

}
