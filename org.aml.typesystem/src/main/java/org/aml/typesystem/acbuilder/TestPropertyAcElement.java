package org.aml.typesystem.acbuilder;

public class TestPropertyAcElement extends AcElement{	
	protected final String propName;
	protected boolean inverse;

	public TestPropertyAcElement(String propName,boolean inverse) {
		super();
		this.propName = propName;
		this.inverse=inverse;
	}

	@Override
	public AcElementKind kind() {
		if (inverse){
			return AcElementKind.PROPERTY_NOT_EXISTANCE;
		}
		return AcElementKind.PROPERTY_EXISTANCE;
	}

	@Override
	public String getProperty() {
		return propName;
	}	
}
