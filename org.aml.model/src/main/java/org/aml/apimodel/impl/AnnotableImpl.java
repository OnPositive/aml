package org.aml.apimodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Annotable;
import org.aml.typesystem.meta.facets.Annotation;

public class AnnotableImpl implements Annotable{

	protected ArrayList<Annotation>annotations=new ArrayList<>();
	
	@Override
	public List<Annotation> annotations() {
		return annotations;
	}

	@Override
	public <T> T annotation(Class<T> cl) {
		// TODO Auto-generated method stub
		return null;
	}

}
