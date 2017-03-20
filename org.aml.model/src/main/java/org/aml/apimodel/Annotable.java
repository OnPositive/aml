package org.aml.apimodel;

import java.util.List;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Annotation;

public interface Annotable {

	List<Annotation>annotations();
	
	default public <T>T annotation(Class<T>cl){
		for (Annotation a:this.annotations()){
			T result=AbstractType.tryConvert(cl, a);
			if (result!=null){
				return result;
			}
		}
		return null;
	}
}
