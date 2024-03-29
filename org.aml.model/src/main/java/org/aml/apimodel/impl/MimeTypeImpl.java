package org.aml.apimodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.Action;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.ParameterLocation;
import org.aml.apimodel.Response;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.DisplayName;
import org.aml.typesystem.meta.facets.Example;

public class MimeTypeImpl extends AnnotableImpl implements MimeType {

	protected AbstractType model;
	protected Action owner;
	protected Response owningResponse;
	protected String name;
	
	protected boolean isDefault=false;
	
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public MimeTypeImpl(AbstractType model, Action owner) {
		this.model = model;
		this.name = model.name();
		this.owner = owner;
	}

	public MimeTypeImpl() {
		this.model = BuiltIns.OBJECT;
		this.name = "application/json";
	}

	public MimeTypeImpl(AbstractType model, Action owner, String name) {
		this.model = model;
		this.owner = owner;
		this.name = name;
	}

	public String getType() {
		return name;
	}

	public AbstractType getPlainModel() {
		return this.model;
	}

	public String getExample() {
		if (model.hasDirectMeta(Example.class)) {
			Example oneMeta = model.oneMeta(Example.class);
			Object vl = oneMeta.value();
			if (vl instanceof String) {
				return "" + vl;
			}
		}
		return null;
	}

	public AbstractType getTypeModel() {
		if (model.isBuiltIn()) {
			return model;
		}
		if (!isDefault()&&!model.isAnonimous()){
			return model;
		}
		if (!model.isUnion() && model.superTypes().size() == 1) {
			boolean canSkip = true;
			for (TypeInformation i : model.declaredMeta()) {
				if (i instanceof DisplayName) {
					continue;
				}
				canSkip = false;
			}
			if (canSkip && !model.superType().isAnonimous()) {
				return model.superType();
			}
		}
		// okey,actual structure is complex;
		return this.model.clone("");
	}

	@Override
	public List<INamedParam> getFormParameters() {
		ArrayList<INamedParam> np = new ArrayList<>();
		if (this.getType().indexOf("form") != -1) {
			for (IProperty p : model.toPropertiesView().allProperties()) {
				NamedParamImpl vp = new NamedParamImpl(p.id(), p.range(), !p.isRequired(), false);
				vp.setLocation(ParameterLocation.FORM);
				np.add(vp);
			}
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

	public void setTypeModel(AbstractType t) {
		this.model = t;
	}

	public void setName(String s) {
		this.name = s;
	}

	public void setOwningReponse(Response responseImpl) {
		this.owningResponse=responseImpl;
	}

}
