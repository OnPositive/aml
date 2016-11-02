package org.aml.registry.model;

public class ApiDescription extends ItemDescription{

	@Override
	public ApiDescription clone()  {
		try {
			return (ApiDescription) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException();
		}
	}
}
