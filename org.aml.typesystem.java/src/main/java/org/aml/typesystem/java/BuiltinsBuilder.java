package org.aml.typesystem.java;

import java.util.HashMap;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.meta.facets.Format;

public class BuiltinsBuilder implements IJavaTypeBuilder {

	protected HashMap<String, IJavaTypeBuilder> bldrs = new HashMap<>();

	static class SimpleBuilder implements IJavaTypeBuilder {

		protected AbstractType base;

		public SimpleBuilder(AbstractType base, boolean nullable, String format) {
			super();
			this.base = base;
			this.nullable = nullable;
			this.format = format;
		}

		protected boolean nullable;
		protected String format;

		@Override
		public AbstractType getType(ITypeModel mdl) {
			if (this.format != null) {
				String name=this.format;
				if (this.nullable){
					name=Character.toUpperCase(name.charAt(0))+name.substring(1);
				}
				AbstractType result = TypeOps.derive(name, base);
				if (this.nullable) {
					result.setNullable(this.nullable);
				}
				if (this.format != null) {
					result.addMeta(new Format(this.format));
				}
				return result;
			}
			
			if (this.nullable){
				String name=this.base.name();
				name=Character.toUpperCase(name.charAt(0))+name.substring(1);
				AbstractType result = TypeOps.derive(name, base);
				result.setNullable(true);
				return result;
			}
			return base;
		}

	}

	public BuiltinsBuilder() {
		bldrs.put(int.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, false, int.class.getName()));
		bldrs.put(long.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, false, long.class.getName()));
		bldrs.put(short.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, false, short.class.getName()));
		bldrs.put(byte.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, false, byte.class.getName()));
		bldrs.put(double.class.getName(), new SimpleBuilder(BuiltIns.NUMBER, false, double.class.getName()));
		bldrs.put(float.class.getName(), new SimpleBuilder(BuiltIns.NUMBER, false, float.class.getName()));
		bldrs.put(boolean.class.getName(), new SimpleBuilder(BuiltIns.BOOLEAN, false, null));
		bldrs.put(char.class.getName(), new SimpleBuilder(BuiltIns.STRING, false, null));
		bldrs.put(Integer.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, true, int.class.getName()));
		bldrs.put(Long.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, true, long.class.getName()));
		bldrs.put(Short.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, true, short.class.getName()));
		bldrs.put(Byte.class.getName(), new SimpleBuilder(BuiltIns.INTEGER, true, byte.class.getName()));
		bldrs.put(Double.class.getName(), new SimpleBuilder(BuiltIns.NUMBER, true, double.class.getName()));
		bldrs.put(Float.class.getName(), new SimpleBuilder(BuiltIns.NUMBER, true, float.class.getName()));
		bldrs.put(Boolean.class.getName(), new SimpleBuilder(BuiltIns.BOOLEAN, true, null));
		bldrs.put(String.class.getName(), new SimpleBuilder(BuiltIns.STRING, true, null));
	}

	private static BuiltinsBuilder bld = new BuiltinsBuilder();

	public static BuiltinsBuilder getInstance() {
		return bld;
	}

	@Override
	public AbstractType getType(ITypeModel mdl) {
		if (bldrs.containsKey(mdl.getFullyQualifiedName())) {
			return bldrs.get(mdl.getFullyQualifiedName()).getType(mdl);
		}
		return null;
	}

}