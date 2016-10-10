package org.aml.typesystem.ramlreader;

import org.aml.apimodel.Action;
import org.aml.typesystem.AbstractType;

public class MimeTypeImpl extends org.aml.apimodel.impl.MimeTypeImpl {

	public MimeTypeImpl(AbstractType model, Action owner) {
		super(model, owner);
	}

	public void setOwningReponse(ResponseImpl responseImpl) {
		this.owningResponse=responseImpl;
	}

	

}