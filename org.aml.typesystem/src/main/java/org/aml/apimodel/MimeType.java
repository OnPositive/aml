package org.aml.apimodel;

import java.util.List;
import java.util.Map;

import org.aml.typesystem.AbstractType;

public class MimeType {
	
	protected AbstractType model;

	public String getType() {
		return model.name();
	}
	
	public String getExample() {
		return null;
	}

	public Map<String, List<NamedParam>> getFormParameters() {
		return null;
	}

	public AbstractType getModel() {
		return model;
	}

}
