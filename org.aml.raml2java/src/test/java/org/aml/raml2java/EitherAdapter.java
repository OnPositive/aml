package org.aml.raml2java;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class EitherAdapter extends XmlAdapter<Custom,Either>{

	public EitherAdapter() {
		System.out.println("Created");
	}
	
	@Override
	public Either unmarshal(Custom v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Custom marshal(Either v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
