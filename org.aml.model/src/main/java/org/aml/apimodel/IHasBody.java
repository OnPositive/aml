package org.aml.apimodel;

import java.util.List;

public interface IHasBody {

	List<MimeType> body();

	default MimeType body(String name){
		return this.body().stream().filter(x -> x.getType().equals(name)).findFirst().orElse(null);
	}
}
