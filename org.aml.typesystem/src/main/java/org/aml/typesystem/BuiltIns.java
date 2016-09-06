package org.aml.typesystem;

import org.aml.typesystem.meta.BasicMeta;
import org.aml.typesystem.meta.facets.Polymorphic;
import org.aml.typesystem.meta.facets.internal.InstanceOfRestriction;
import org.aml.typesystem.meta.facets.internal.NothingRestriction;
import org.aml.typesystem.meta.facets.internal.ORRestricton;
import org.aml.typesystem.meta.restrictions.IsArrayRestriction;
import org.aml.typesystem.meta.restrictions.IsObjectRestriction;

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
	
	public static final AbstractType ANY = new RootType("any");
	
	public static final AbstractType NIL = new RootType("nil");

	public static final AbstractType SCALAR =TypeOps.derive("scalar", ANY);;

	public static final AbstractType ARRAY = TypeOps.derive("array", ANY);

	public static final AbstractType OBJECT = TypeOps.derive("object", ANY);

	
	public static final AbstractType BOOLEAN = TypeOps.derive("boolean", SCALAR);
	
	public static final AbstractType NOTHING = new RootType("nothing");

	public static final AbstractType NUMBER = TypeOps.derive("number", SCALAR);

	public static final AbstractType INTEGER = TypeOps.derive("integer", NUMBER);

	public static final AbstractType DATE = TypeOps.derive("date", SCALAR);

	public static final AbstractType FILE = TypeOps.derive("file", SCALAR);
	
	public static final AbstractType POLYMORPH = TypeOps.derive("polymorphic", OBJECT);

	public static final AbstractType RECURRENT_TYPE = new RootType("<rec>");
	public static final AbstractType UNKNOWN_TYPE = new RootType("<rec>");

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
		builtInsRegostry.registerType(DATE);
		builtInsRegostry.registerType(NIL);
		NIL.addMeta(BasicMeta.BUILTIN);
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

	public static ITypeRegistry getBuiltInTypes() {
		return builtInsRegostry;
	}

	private BuiltIns() {
		
	}

}