package org.aml.typesystem.ramlreader;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Action;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Response;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.DisplayName;
import org.aml.typesystem.meta.facets.Example;

public class MimeTypeImpl implements MimeType {

	protected AbstractType model;
	protected MethodImpl owner;
	protected Response owningResponse;

	public MimeTypeImpl(NamedParam p,MethodImpl owner) {
		this.model = p.getTypeModel();
		
		this.owner=owner;
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
		if (!model.isUnion()&&model.superTypes().size()==1){
    		boolean canSkip=true;
    		for (TypeInformation i:model.declaredMeta()){
    			if (i instanceof DisplayName){
    				continue;
    			}
    			canSkip=false;
    		}
    		if (canSkip&&!model.superType().isAnonimous()){
    			return model.superType();
    		}
    	}
		//okey,actual structure is complex;
		return this.model.clone(this.getMethod().resource().getUri()+this.getMethod().method()+"Type");
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

	@Override
	public Action getMethod() {
		return owner;
	}

	@Override
	public Response owningResponse() {
		return owningResponse;
	}

}