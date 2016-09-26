package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.facets.Example;

public class MimeTypeImpl implements MimeType {

	protected AbstractType model;

	public MimeTypeImpl(NamedParam p) {
		this.model = p.getTypeModel();
	}

	public String getType() {
		return model.name();
	}

	public String getExample() {
		if (model.hasDirectMeta(Example.class)){
			Example oneMeta = model.oneMeta(Example.class);
			Object vl=oneMeta.value();
			if (vl instanceof String){
				return ""+vl;
			}
		}
		return null;
	}

	public AbstractType getTypeModel() {
		return model;
	}

	@Override
	public List<INamedParam> getFormParameters() {
		ArrayList<INamedParam> np = new ArrayList<>();
		for (IProperty p : model.toPropertiesView().allProperties()) {
			NamedParam vp=new NamedParam(p.range(), !p.isRequired(), false);
			np.add(vp);
		}
		return np;
	}

}
