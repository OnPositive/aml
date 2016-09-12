package org.aml.raml2java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeLibrary;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JAnnotatable;
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
		if (this.config.defaultPackageName!=null){
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
						return define;
					}
				}
			}
			
			
				if (range.isObject()){
					if (!range.directPropertySet().isEmpty()||range.superTypes().size()>1){
						AbstractType derive = TypeOps.derive(typePropertyName(member), range.superTypes().toArray(new AbstractType[range.superTypes().size()]));
						derive.metaInfo.addAll(range.meta());
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
			if (!t.isAnonimous()){
			new AnnotationTypeGenerator(this).define(t);
			}
		}
		for (AbstractType t : types.types()) {
			if (!t.isAnonimous()){
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
		this.config.defaultPackageName=string;
	}

}