package org.aml.jav2raml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeLibrary;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.UnionType;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.PropertyViewImpl;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;

public class JavaWriter {

	private JCodeModel mdl=new JCodeModel();
	private JavaGenerationConfig config;
	
	public JCodeModel getModel(){
		return mdl;
	}

	public JavaWriter(JavaGenerationConfig cfg) {
		this.config=cfg;
	}
	

	int tp=0;
	JType defineType(AbstractType tp) throws JClassAlreadyExistsException {
		if (tp.isScalar()) {
			return createScalarType(tp);
		}
		if (tp.isArray()) {
			return createArrayType(tp);
		}
		if (tp.isUnion()) {
			return createUnion(tp);
		}
		final Set<AbstractType> superTypes = tp.superTypes();
		if (tp.isArray()) {
			final ComponentShouldBeOfType oneMeta = tp.oneMeta(ComponentShouldBeOfType.class);
			AbstractType range = oneMeta.range();
			if (range.isPolymorphic()){
				for (AbstractType t : tp.allSubTypes()) {
					if (t != tp) {
						defineType(t);
					}
				}
			}
			return defineType(range).array();
		}
		String name = tp.name();
		if (name.length()==0){
			name="UnnamedType"+(this.tp++);
		}
		JDefinedClass dc = mdl._class(config.defaultPackageName+"." + name);
		defined.put(tp, dc);
		boolean allProperties = false;
		ArrayList<AbstractType> supers = new ArrayList<>();
		for (AbstractType t : superTypes) {
			if (t.isBuiltIn()) {
				continue;
			} else {
				supers.add(t);
			}
		}
		
		if (supers.size() == 1) {
			AbstractType next = supers.iterator().next();
			
			final JType type = getType(next);
			if (type==null){
				getType(next);
			}
			dc._extends((JClass) type);

		} else {
			for (AbstractType t : supers) {
				dc._implements(mdl.ref("I" + t.name()));
			}
			allProperties = true;
		}
		final PropertyViewImpl propertyViewImpl = new PropertyViewImpl(tp);

		final List<IProperty> properties = allProperties ? propertyViewImpl.allProperties()
				: propertyViewImpl.properties();
		for (IProperty p : properties) {
			if (p.isMap() || p.isAdditional()) {
				JType mt = mdl.ref(Map.class).narrow(mdl.ref(String.class)).narrow(getType(p.range()));
				dc.field(JMod.PRIVATE, mt, pid(p));
			} else {
				dc.field(JMod.PRIVATE, getType(p.range()), pid(p));
			}
		}
		for (final IProperty p : properties) {
			final AbstractType range = p.range();
			JType type = getType(range);
			if (p.isMap() || p.isAdditional()) {
				JType mt = mdl.ref(Map.class).narrow(mdl.ref(String.class)).narrow(getType(p.range()));
				type = mt;
			}
			final JMethod method = dc.method(JMod.PUBLIC, type,
					"get" + Character.toUpperCase(pid(p).charAt(0)) + pid(p).substring(1));
			method.body().add(new JStatement() {

				@Override
				public void state(JFormatter f) {
					f.p("return this." + pid(p) + ";");
					f.nl();
				}
			});
			final JMethod method2 = dc.method(JMod.PUBLIC, void.class,
					"set" + Character.toUpperCase(pid(p).charAt(0)) + pid(p).substring(1));
			method2.param(type, "value");
			method2.body().add(new JStatement() {

				@Override
				public void state(JFormatter f) {
					f.p("this." + pid(p) + "=value;");
					f.nl();
				}
			});

		}
		return dc;
	}

	JType createUnion(AbstractType tp) throws JClassAlreadyExistsException {
		String name = tp.name();
		if (name==null||name.length()==0){
			name="Union"+(this.tp++);
		}
		JDefinedClass dc = mdl._class("org.raml." + name);

		for (AbstractType t : tp.superTypes()) {
			if (t instanceof UnionType) {
				UnionType k = (UnionType) t;
				final Set<AbstractType> allOptions = k.allOptions();
				for (AbstractType z : allOptions) {
					dc.field(JMod.PRIVATE, getType(z), z.name());
				}
				for (final AbstractType pz : allOptions) {
					JType type = getType(pz);

					final JMethod method = dc.method(JMod.PUBLIC, type,
							"get" + Character.toUpperCase(pz.name().charAt(0)) + pz.name().substring(1));
					method.body().add(new JStatement() {

						@Override
						public void state(JFormatter f) {
							f.p("return this." + pz.name() + ";");
							f.nl();
						}
					});
					final JMethod method2 = dc.method(JMod.PUBLIC, void.class,
							"set" + Character.toUpperCase(pz.name().charAt(0)) + pz.name().substring(1));
					method2.param(type, "value");
					method2.body().add(new JStatement() {

						@Override
						public void state(JFormatter f) {
							f.p("this." + pz.name() + "=value;");
							f.nl();
						}
					});

				}
			}
		}
		return dc;
	}

	JType createScalarType(AbstractType tp) throws JClassAlreadyExistsException {
		JDefinedClass dc = mdl._class("org.raml." + tp.name());
		Class<?> range = String.class;
		if (tp.isNumber()) {
			range = double.class;
		}
		if (tp.isBoolean()) {
			range = boolean.class;
		}
		dc.field(JMod.PRIVATE, range, "value");

		final JMethod method = dc.method(JMod.PUBLIC, range, "getValue");
		method.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("return this." + "value" + ";");
				f.nl();
			}
		});
		final JMethod method2 = dc.method(JMod.PUBLIC, void.class, "setValue");
		method2.param(range, "value");
		method2.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("this." + "value" + "=value;");
				f.nl();
			}
		});

		return dc;
	}

	JType createArrayType(AbstractType tp) throws JClassAlreadyExistsException {
		AbstractType range2 = tp.oneMeta(ComponentShouldBeOfType.class).range();
		if (range2==null){
			range2=BuiltIns.STRING;
		}
		if (tp.isAnonimous()) {
			if (range2.isPolymorphic()){
				for (AbstractType t : range2.allSubTypes()) {
					if (t != tp) {
						defineType(t);
					}
				}
			}
			JType range = getType(range2).array();
			return range;
		}
		JDefinedClass dc = mdl._class("org.raml." + tp.name());
		JType range = getType(range2).array();

		dc.field(JMod.PRIVATE, range, "value");

		final JMethod method = dc.method(JMod.PUBLIC, range, "getValue");
		method.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("return this." + "value" + ";");
				f.nl();
			}
		});
		final JMethod method2 = dc.method(JMod.PUBLIC, void.class, "setValue");
		method2.param(range, "value");
		method2.body().add(new JStatement() {

			@Override
			public void state(JFormatter f) {
				f.p("this." + "value" + "=value;");
				f.nl();
			}
		});

		return dc;
	}

	int num = 0;
	protected LinkedHashMap<IProperty, String> maps = new LinkedHashMap<>();

	protected String pid(IProperty p) {
		if (p.isAdditional()) {
			return "additional";
		}
		if (p.isMap()) {
			if (maps.containsKey(p)) {
				return maps.get(p);
			}
			final String string = "map" + (num++);
			maps.put(p, string);
			return string;
		}
		return p.id();
	}

	HashMap<AbstractType, JType> defined = new HashMap<>();

	public JType getType(AbstractType t) {
		if (t == BuiltIns.ANY) {
			return mdl._ref(Object.class);
		}
		if (t == BuiltIns.POLYMORPH) {
			return mdl._ref(Object.class);
		}
		if (t.isScalar()) {
			if (t.isString()) {
				return mdl._ref(String.class);
			}
			if (t.isNumber()) {
				return mdl._ref(double.class);
			}
			if (t.isBoolean()) {
				return mdl._ref(boolean.class);
			}
			return mdl._ref(String.class);
		}
		if (defined.containsKey(t)) {
			return defined.get(t);
		}
		try {
			JType tp = defineType(t);
			defined.put(t, tp);
			return tp;
		} catch (JClassAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void write(ITypeLibrary types) {
		for (AbstractType t:types.types()){
			getType(t);
		}
		for (AbstractType t:types.annotationTypes()){
			getType(t);
		}
	}
	public void write(ITypeRegistry types) {
		for (AbstractType t:types){
			getType(t);
		}
	}
}