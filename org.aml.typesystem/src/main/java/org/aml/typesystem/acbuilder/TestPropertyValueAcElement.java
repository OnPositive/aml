package org.aml.typesystem.acbuilder;

public class TestPropertyValueAcElement extends AcElement{

	protected final String property;
	protected final String value;
	
	public TestPropertyValueAcElement(String property, String value) {
		super();
		this.property = property;
		this.value = value;
	}

	@Override
	public AcElementKind kind() {
		return AcElementKind.PROPERTY_VALUE;
	}

	@Override
	public String getProperty() {
		return property;
	}
	
}
