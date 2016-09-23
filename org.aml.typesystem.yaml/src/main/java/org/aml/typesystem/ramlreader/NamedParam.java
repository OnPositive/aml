package org.aml.typesystem.ramlreader;

import java.math.BigDecimal;
import java.util.List;

import org.aml.apimodel.INamedParam;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.DisplayName;
import org.aml.typesystem.meta.facets.Example;
import org.aml.typesystem.meta.restrictions.Pattern;
import org.aml.typesystem.meta.restrictions.minmax.MaxLength;
import org.aml.typesystem.meta.restrictions.minmax.Maximum;
import org.aml.typesystem.meta.restrictions.minmax.MinLength;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;

public class NamedParam implements INamedParam {
	
	protected AbstractType type;
	protected boolean required;
	protected boolean repeat;

	public NamedParam(AbstractType type,boolean required,boolean repeat) {
		super();
		this.type = type;		
		this.required=required;
	}

	public List<String> getEnumeration() {
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
		String name = type.name();		
		return name;
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

}
