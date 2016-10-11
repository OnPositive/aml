package org.aml.apimodel.impl;

import java.util.LinkedHashMap;

import org.aml.apimodel.SecuredByConfig;

public class SecuredByConfigImpl extends AnnotableImpl implements SecuredByConfig{

	protected String name;
	protected LinkedHashMap<String, Object>settings=new LinkedHashMap<>();
	
	public SecuredByConfigImpl(String name) {
		super();
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public LinkedHashMap<String, Object> settings() {
		return settings;
	}

	public SecuredByConfig with(String string, Object val) {
		this.settings.put(string, val);
		return this;
	}

}
