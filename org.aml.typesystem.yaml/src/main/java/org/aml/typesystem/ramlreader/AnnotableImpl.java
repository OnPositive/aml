package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Annotable;
import org.aml.typesystem.meta.facets.Annotation;

public class AnnotableImpl implements Annotable{
	protected ArrayList<Annotation>annotations=new ArrayList<>();
	@Override
	public List<Annotation> annotations() {
		return this.annotations;
	}
}
