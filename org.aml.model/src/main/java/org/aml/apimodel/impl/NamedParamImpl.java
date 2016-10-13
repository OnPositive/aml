package org.aml.apimodel.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.aml.apimodel.INamedParam;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.FacetRegistry;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.DisplayName;
import org.aml.typesystem.meta.facets.Example;
import org.aml.typesystem.meta.restrictions.Enum;
import org.aml.typesystem.meta.restrictions.Pattern;
import org.aml.typesystem.meta.restrictions.minmax.MaxLength;
import org.aml.typesystem.meta.restrictions.minmax.Maximum;
import org.aml.typesystem.meta.restrictions.minmax.MinLength;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;

public class NamedParamImpl extends AnnotableImpl implements INamedParam{

	protected AbstractType type;
	protected boolean required;
	protected boolean repeat;
	protected String name;
	public NamedParamImpl(AbstractType type,boolean required,boolean repeat) {
		super();
		this.type = type;
		this.name=type.name();
		this.required=required;		
	}

	public NamedParamImpl(String string, AbstractType type, boolean required, boolean repeat) {
		this.name=string;
		this.required=required;
		this.repeat=repeat;
		this.type=type;
	}

	public NamedParamImpl() {
		this.type=BuiltIns.STRING;
		this.name="param";
	}

	public List<String> getEnumeration() {
		if (type.oneMeta(org.aml.typesystem.meta.restrictions.Enum.class)!=null){
			return type.oneMeta(Enum.class).value();
		}
		return null;
	}

	public String description() {
		if (type.hasDirectMeta(Description.class)){
			return type.oneMeta(Description.class).value();
		}
		return null;
	}

	public String getExample() {
		if (type.hasDirectMeta(Example.class)){
			return ""+type.oneMeta(Example.class).value();
		}
		return null;
	}

	public String getDisplayName() {
		if (type.hasDirectMeta(DisplayName.class)){
			return ""+type.oneMeta(DisplayName.class).value();
		}
		return null;
	}

	public String getKey() {
		return this.name;
	}

	public String getDefaultValue() {
		Default oneMeta = type.oneMeta(Default.class);
		if (oneMeta!=null){
			return ""+oneMeta.value();
		}
		return null;
	}

	public String getPattern() {
		Pattern oneMeta = type.oneMeta(Pattern.class);
		if (oneMeta!=null){
			return oneMeta.value();
		}
		return null;
	}

	public Integer getMinLength() {
		MinLength oneMeta = type.oneMeta(MinLength.class);
		if (oneMeta!=null){
			return Integer.parseInt(""+oneMeta.value());
		}
		return null;
	}

	public Integer getMaxLength() {
		MaxLength oneMeta = type.oneMeta(MaxLength.class);
		if (oneMeta!=null){
			return Integer.parseInt(""+oneMeta.value());
		}
		return null;
	}

	public BigDecimal getMinimum() {
		Minimum oneMeta = type.oneMeta(Minimum.class);
		if (oneMeta!=null){
			return new BigDecimal(""+oneMeta.value());
		}
		return null;
	}

	public BigDecimal getMaximum() {
		Maximum oneMeta = type.oneMeta(Maximum.class);
		if (oneMeta!=null){
			return new BigDecimal(""+oneMeta.value());
		}
		return null;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isRepeat() {
		return repeat;
	}
	
	
	public TypeKind getTypeKind() {
		if (type.isBoolean()){
			return TypeKind.BOOLEAN;
		}
		if (type.isFile()){
			return TypeKind.FILE;
		}
		if (type.isInteger()){
			return TypeKind.INTEGER;
		}
		if (type.isNumber()){
			return TypeKind.NUMBER;
		}
		if (type.isString()){
			return TypeKind.STRING;
		}
		//FIXME
		return TypeKind.STRING;
	}

	public AbstractType getTypeModel() {
		return this.type;
	}

	public void setType(AbstractType integer) {
		this.type=integer;
	}

	public void setRequired(boolean b) {
		this.required=b;
	}

	public void setName(String paramName) {
		this.name=paramName;
	}

	public void setDescription(String text) {
		final Class<? extends ISimpleFacet> clazz = Description.class;
		setFacet(text, clazz);
	}

	@SuppressWarnings("unchecked")
	public void setFacet(Object value, final Class<? extends ISimpleFacet> clazz) {
		checkType();
		ISimpleFacet f=this.type.oneMeta(clazz);
		if (f==null){
			f=(ISimpleFacet) FacetRegistry.facet(FacetRegistry.getFacetName((Class<? extends TypeInformation>) clazz));
			this.type.addMeta((TypeInformation) f);
		}
		f.setValue(value);
	}

	private void checkType() {
		if (this.type.isBuiltIn()){
			this.type=TypeOps.derive("", this.type);
		}
	}

	public void setDefaultValue(String trim) {
		setFacet(trim, Default.class);
	}

	public void setMaximum(BigDecimal valueOf) {
		setFacet(valueOf.doubleValue(),Maximum.class);
	}
	public void setMinimum(BigDecimal valueOf) {
		setFacet(valueOf.doubleValue(),Minimum.class);
	}

	public void setPattern(String pattern) {
		setFacet(pattern,Pattern.class);
	}

	public void setRepeat(boolean boolValue) {
		this.repeat=true;
	}

	public void setEnumeration(ArrayList<String> list) {
		setFacet(list, Enum.class);
	}
}
