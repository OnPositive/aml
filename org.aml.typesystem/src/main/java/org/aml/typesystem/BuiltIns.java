package org.aml.typesystem;

import org.aml.typesystem.meta.BasicMeta;
import org.aml.typesystem.meta.facets.FacetDeclaration;
import org.aml.typesystem.meta.facets.Polymorphic;
import org.aml.typesystem.meta.facets.internal.InstanceOfRestriction;
import org.aml.typesystem.meta.facets.internal.NothingRestriction;
import org.aml.typesystem.meta.facets.internal.ORRestricton;
import org.aml.typesystem.meta.restrictions.IsArrayRestriction;
import org.aml.typesystem.meta.restrictions.IsObjectRestriction;

/**
 * <p>BuiltIns class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class BuiltIns {

	private static class RootType extends AbstractType{

		public RootType(String name) {
			super(name);
		}

		@Override
		public AbstractType noPolymorph() {
			return this;
		}
		
	}
	
	/** Constant <code>ANY</code> */
	public static final AbstractType ANY = new RootType("any");
	
	/** Constant <code>NIL</code> */
	public static final AbstractType NIL = new RootType("nil");

	/** Constant <code>SCALAR</code> */
	public static final AbstractType SCALAR =TypeOps.derive("scalar", ANY);;

	/** Constant <code>ARRAY</code> */
	public static final AbstractType ARRAY = TypeOps.derive("array", ANY);

	/** Constant <code>OBJECT</code> */
	public static final AbstractType OBJECT = TypeOps.derive("object", ANY);

	
	/** Constant <code>BOOLEAN</code> */
	public static final AbstractType BOOLEAN = TypeOps.derive("boolean", SCALAR);
	
	/** Constant <code>NOTHING</code> */
	public static final AbstractType NOTHING = new RootType("nothing");

	/** Constant <code>NUMBER</code> */
	public static final AbstractType NUMBER = TypeOps.derive("number", SCALAR);
	
	

	/** Constant <code>INTEGER</code> */
	public static final AbstractType INTEGER = TypeOps.derive("integer", NUMBER);

	/** Constant <code>DATETIME</code> */
	public static final AbstractType DATETIME = TypeOps.derive("datetime", SCALAR);
	/** Constant <code>TIME</code> */
	public static final AbstractType TIMEONLY = TypeOps.derive("time-only", SCALAR);
	
	/** Constant <code>TIME</code> */
	public static final AbstractType DATEONLY = TypeOps.derive("date-only", SCALAR);
	
	/** Constant <code>TIME</code> */
	public static final AbstractType DATETIMEONLY = TypeOps.derive("datetime-only", SCALAR);

	/** Constant <code>FILE</code> */
	public static final AbstractType FILE = TypeOps.derive("file", SCALAR);
	
	/** Constant <code>POLYMORPH</code> */
	public static final AbstractType POLYMORPH = TypeOps.derive("polymorphic", OBJECT);

	/** Constant <code>RECURRENT_TYPE</code> */
	public static final AbstractType RECURRENT_TYPE = new RootType("<rec>");
	/** Constant <code>UNKNOWN_TYPE</code> */
	public static final AbstractType UNKNOWN_TYPE = new RootType("<rec>");

	/** Constant <code>STRING</code> */
	public static final AbstractType STRING = TypeOps.derive("string", SCALAR);

	private static final TypeRegistryImpl builtInsRegostry = new TypeRegistryImpl(null);
	
	static {
		RECURRENT_TYPE.addMeta(BasicMeta.BUILTIN);
		ANY.addMeta(BasicMeta.BUILTIN);
		builtInsRegostry.registerType(ANY);
		NOTHING.addMeta(BasicMeta.BUILTIN);
		NOTHING.addMeta(NothingRestriction.INSTANCE);
		builtInsRegostry.registerType(NOTHING);
		SCALAR.addMeta(BasicMeta.BUILTIN);
		SCALAR.addMeta(new ORRestricton(new InstanceOfRestriction(Number.class),
				new InstanceOfRestriction(String.class), new InstanceOfRestriction(Boolean.class)));
		builtInsRegostry.registerType(SCALAR);
		builtInsRegostry.registerType(FILE);
		NUMBER.addMeta(BasicMeta.BUILTIN);
		builtInsRegostry.registerType(DATETIME);
		builtInsRegostry.registerType(TIMEONLY);
		builtInsRegostry.registerType(DATEONLY);
		builtInsRegostry.registerType(DATETIMEONLY);
		builtInsRegostry.registerType(NIL);
		NIL.addMeta(BasicMeta.BUILTIN);
		DATEONLY.addMeta(BasicMeta.BUILTIN);
		TIMEONLY.addMeta(BasicMeta.BUILTIN);
		DATETIMEONLY.addMeta(BasicMeta.BUILTIN);
		DATETIME.addMeta(BasicMeta.BUILTIN);
		DATETIME.addMeta(new FacetDeclaration("format", BuiltIns.STRING));
		NUMBER.addMeta(new InstanceOfRestriction(Number.class));
		builtInsRegostry.registerType(NUMBER);
		INTEGER.addMeta(new InstanceOfRestriction(Integer.class));
		builtInsRegostry.registerType(INTEGER);
		STRING.addMeta(BasicMeta.BUILTIN);
		STRING.addMeta(new InstanceOfRestriction(String.class));
		builtInsRegostry.registerType(STRING);
		BOOLEAN.addMeta(BasicMeta.BUILTIN);
		BOOLEAN.addMeta(new InstanceOfRestriction(Boolean.class));
		builtInsRegostry.registerType(BOOLEAN);
		OBJECT.addMeta(BasicMeta.BUILTIN);
		OBJECT.addMeta(new IsObjectRestriction());
		builtInsRegostry.registerType(OBJECT);
		ARRAY.addMeta(BasicMeta.BUILTIN);
		ARRAY.addMeta(IsArrayRestriction.INSTANCE);
		builtInsRegostry.registerType(ARRAY);
		POLYMORPH.addMeta(new Polymorphic(true));
		POLYMORPH.addMeta(BasicMeta.BUILTIN);
		builtInsRegostry.registerType(POLYMORPH);
	}

	/**
	 * <p>getBuiltInTypes.</p>
	 *
	 * @return a {@link org.aml.typesystem.ITypeRegistry} object.
	 */
	public static ITypeRegistry getBuiltInTypes() {
		return builtInsRegostry;
	}

	private BuiltIns() {
		
	}

}
