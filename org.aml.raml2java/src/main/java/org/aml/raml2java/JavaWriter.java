package org.aml.raml2java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeLibrary;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JExpressionImpl;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JType;
import com.sun.codemodel.writer.SingleStreamCodeWriter;

public class JavaWriter {

	private JCodeModel mdl = new JCodeModel();
	private HashMap<AbstractType, JType> defined = new HashMap<>();

	private JavaGenerationConfig config;

	public JavaGenerationConfig getConfig() {
		return config;
	}

	public JavaWriter(JavaGenerationConfig cfg) {
		super();
		this.config = cfg;
		if (this.config.defaultPackageName != null) {
			this.setDefaultPackageName(this.config.getDefaultPackageName());
		}
	}

	public JavaWriter() {
		this(new JavaGenerationConfig());
	}

	public JCodeModel getModel() {
		return mdl;
	}

	protected INameGenerator nameGenerator = new DefaultNameGenerator("org.aml.test");
	public IPropertyNameGenerator propNameGenerator = new IPropertyNameGenerator() {

		@Override
		public String name(IProperty p) {
			return p.id();
		}
	};

	public JDefinedClass defineClass(AbstractType t, ClassType type) {
		String fullyQualifiedName = nameGenerator.fullyQualifiedName(t);
		try {
			return mdl._class(fullyQualifiedName, type);
		} catch (JClassAlreadyExistsException e) {
			throw new IllegalStateException(e);
		}
	}

	public Object getDefault(AbstractType range) {
		if (range.isString()) {
			return "";
		} else if (range.isBoolean()) {
			return false;
		} else if (range.isNumber()) {
			return 0;
		} else if (range.isScalar()) {
			return "";
		}
		if (range.isArray()) {
			return new Object[0];
		}
		return null;
	}

	public void annotate(JAnnotatable annotable, AbstractType tp) {
		Set<TypeInformation> declaredMeta = tp.declaredMeta();
		for (TypeInformation i : declaredMeta) {
			if (i instanceof Annotation) {
				Annotation ann = (Annotation) i;
				AbstractType annotationType = ann.annotationType();
				JClass type = (JClass) getType(annotationType);
				JAnnotationUse annotate = annotable.annotate((JClass) type);
				Object value = ann.value();
				if (annotationType.isScalar()) {
					String name = "value";
					addParam(annotate, value, name);
				} else {
					if (annotationType.isObject()) {
						HashMap<String, ?> valueMap = (HashMap<String, ?>) value;
						for (String c : valueMap.keySet()) {
							addParam(annotate, valueMap.get(c), c);
						}
					}
					if (annotationType.isArray()) {
						List<Object> vl = (List<Object>) value;
						JAnnotationArrayMember paramArray = annotate.paramArray("value");
						for (Object o : vl) {
							addParam(paramArray, o);
						}
					}
				}
			}
		}
	}

	private void addParam(JAnnotationArrayMember annotate, Object value) {
		if (value instanceof String) {
			annotate.param("" + value);
		}
		if (value instanceof Double) {
			annotate.param((Double) value);
		}
		if (value instanceof Long) {
			annotate.param((Long) value);
		}
		if (value instanceof Float) {
			annotate.param((Float) value);
		}
		if (value instanceof Boolean) {
			annotate.param((Boolean) value);
		}
		if (value instanceof Integer) {
			annotate.param((Integer) value);
		}
	}

	private void addParam(JAnnotationUse annotate, Object value, String name) {
		if (value instanceof String) {
			annotate.param(name, "" + value);
		}
		if (value instanceof Double) {
			annotate.param(name, (Double) value);
		}
		if (value instanceof Long) {
			annotate.param(name, (Long) value);
		}
		if (value instanceof Float) {
			annotate.param(name, (Float) value);
		}
		if (value instanceof Boolean) {
			annotate.param(name, (Boolean) value);
		}
		if (value instanceof Integer) {
			annotate.param(name, (Integer) value);
		}
		if (value.getClass().isArray()) {
			JAnnotationArrayMember paramArray = annotate.paramArray(name);
			for (int i = 0; i < Array.getLength(value); i++) {
				addParam(paramArray, Array.get(value, i));
			}
		}
		if (Collection.class.isAssignableFrom(value.getClass())) {
			JAnnotationArrayMember paramArray = annotate.paramArray(name);
			Collection<Object> vlc = (Collection<Object>) value;
			for (Object o : vlc) {
				addParam(paramArray, o);
			}
		}
	}

	public JExpression toExpr(Object value) {
		if (value instanceof Double) {
			Double d = (Double) value;
			return JExpr.lit(d.doubleValue());
		}
		if (value instanceof Float) {
			Float d = (Float) value;
			return JExpr.lit(d.floatValue());
		}
		if (value instanceof Long) {
			Long d = (Long) value;
			return JExpr.lit(d.longValue());
		}
		if (value instanceof Integer) {
			Integer d = (Integer) value;
			return JExpr.lit(d.intValue());
		}
		if (value instanceof Short) {
			Short d = (Short) value;
			return JExpr.lit(d.shortValue());
		}
		if (value instanceof Byte) {
			Byte d = (Byte) value;
			return JExpr.lit(d.byteValue());
		}
		if (value instanceof Boolean) {
			Boolean d = (Boolean) value;
			return JExpr.lit(d.booleanValue());
		}
		if (value instanceof String) {
			String d = (String) value;
			return JExpr.lit(d);
		}
		if (value instanceof Object[]) {
			Object[] mm = (Object[]) value;
			if (mm.length == 0) {
				return new JExpressionImpl() {

					@Override
					public void generate(JFormatter f) {
						f.p("{}");
					}
				};
			}
		}
		throw new IllegalArgumentException();
	}

	public JType getType(AbstractType range) {
		return getType(range, true, false, null);
	}

	public JType getType(AbstractType range, boolean allowNotJava, boolean convertComplexToAnnotation,
			IProperty member) {
		if (member != null) {
			if (range.isEffectivelyEmptyType()) {
				return getType(range.superTypes().iterator().next(), allowNotJava, convertComplexToAnnotation, member);
			}
		}

		if (defined.containsKey(range)) {
			return defined.get(range);
		}
		if (range.isBuiltIn()) {
			if (range.isString()) {
				return mdl._ref(String.class);
			}
			if (range.isBoolean()) {
				return mdl._ref(boolean.class);
			}
			if (range.isNumber()) {
				if (range.isInteger()) {
					return mdl._ref(int.class);
				}
				return mdl._ref(double.class);
			}
			if (range.isObject()) {
				return mdl._ref(Object.class);
			}
		}

		if (range.isAnonimous()) {
			if (range.isArray()) {
				AbstractType range2 = range.oneMeta(ComponentShouldBeOfType.class).range();
				return getType(range2, allowNotJava, convertComplexToAnnotation, member).array();
			}
			if (range.isString()) {
				if (range.isEnumType()) {
					if (member != null) {
						AbstractType derive = TypeOps.derive(typePropertyName(member), BuiltIns.STRING);
						derive.metaInfo.addAll(range.meta());
						JType define = new EnumTypeGenerator(this).define(derive);
						defined.put(range, define);
						return define;
					}
				}
			}

			if (range.isUnion()) {
				UnionTypeGenerator ug = new UnionTypeGenerator(this);
				AbstractType derive = TypeOps.derive(typePropertyName(member),
						range.superTypes().toArray(new AbstractType[range.superTypes().size()]));
				for (TypeInformation t : range.meta()) {
					derive.addMeta(t.clone());
				}
				JType define = ug.define(derive);
				defined.put(range, define);
				return define;
			}
			if (range.isObject()) {
				if (!range.directPropertySet().isEmpty() || range.superTypes().size() > 1) {
					AbstractType derive = TypeOps.derive(typePropertyName(member),
							range.superTypes().toArray(new AbstractType[range.superTypes().size()]));
					for (TypeInformation t : range.meta()) {
						derive.addMeta(t.clone());
					}
					return getType(derive, false, false, null);
				}
			}
			if (range.superTypes().size() == 1) {
				return getType(range.superTypes().iterator().next(), allowNotJava, convertComplexToAnnotation, member);
			}
		}
		if (range.isEnumType() && range.isString()) {
			JType define = new EnumTypeGenerator(this).define(range);
			defined.put(range, define);
			return define;
		}
		if (!allowNotJava) {
			if (range.isArray()) {
				AbstractType range2 = range.oneMeta(ComponentShouldBeOfType.class).range();
				return getType(range2).array();
			}
			if (range.isScalar()) {
				if (range.superTypes().size() == 1) {
					return getType(range.superTypes().iterator().next(), allowNotJava, convertComplexToAnnotation,
							member);
				}
			}
		}
		if (range.isUnion()) {
			UnionTypeGenerator ug = new UnionTypeGenerator(this);
			JType define = ug.define(range);
			defined.put(range, define);
			return define;
		}
		if (range.isObject()) {
			// if (range.hasSingleSuperType()) {
			SimpleBeanGenerator sb = new SimpleBeanGenerator(this);
			JType define = sb.define(range);
			defined.put(range, define);
			return define;
		}
		return null;
	}

	private String typePropertyName(IProperty member) {
		return member.getDeclaredAt().name() + member.id();
	}

	public String escape(String x) {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < x.length(); i++) {
			char c = x.charAt(i);
			if (i == 0) {
				if (!Character.isJavaIdentifierStart(c)) {
					c = '_';
				}
			} else {
				if (!Character.isJavaIdentifierPart(c)) {
					c = '_';
				}
			}
			bld.append(c);
		}
		return bld.toString();
	}

	public void write(ITypeLibrary types) {
		for (AbstractType t : types.annotationTypes()) {
			if (!t.isAnonimous()) {
				defined.put(t, new AnnotationTypeGenerator(this).define(t));
			}
		}
		for (AbstractType t : types.types()) {
			if (!t.isAnonimous()) {
				getType(t);
			}
		}
	}

	public String writeToString(ITypeLibrary lib) {
		write(lib);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		CodeWriter wr = new SingleStreamCodeWriter(os);
		try {
			getModel().build(wr);
			return new String(os.toByteArray(), "UTF-8");
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	public void setDefaultPackageName(String string) {
		this.nameGenerator = new DefaultNameGenerator(string);
		this.config.defaultPackageName = string;
	}

}