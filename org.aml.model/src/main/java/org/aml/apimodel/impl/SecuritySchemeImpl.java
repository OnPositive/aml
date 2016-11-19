package org.aml.apimodel.impl;

import java.util.HashMap;
import java.util.Map;

import org.aml.apimodel.MethodBase;
import org.aml.apimodel.SecurityScheme;

public class SecuritySchemeImpl extends AnnotableImpl implements SecurityScheme{

	protected String name;
	protected String type;
	protected String description;
	protected MethodBase describedBy;
	
	public MethodBase getDescribedBy() {
		return describedBy;
	}

	public void setDescribedBy(MethodBase describedBy) {
		this.describedBy = describedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	protected HashMap<String, Object>settings=new HashMap<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	public Map<String, Object> settings() {
		return settings;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public MethodBase describedBy() {
		return describedBy;
	}

}
