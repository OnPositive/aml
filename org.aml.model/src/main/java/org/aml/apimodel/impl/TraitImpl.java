package org.aml.apimodel.impl;

import org.aml.apimodel.Trait;

public class TraitImpl extends ActionImpl implements Trait{

	public TraitImpl(String name) {
		super(name);
	}

	@Override
	public String name() {
		return this.httpMethod;
	}

}
