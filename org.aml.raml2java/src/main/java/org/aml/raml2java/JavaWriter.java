package org.aml.raml2java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;

import org.aml.java.mapping.container;
import org.aml.java.mapping.defaultIntegerFormat;
import org.aml.java.mapping.defaultNumberFormat;
import org.aml.raml2java.JavaGenerationConfig.WrappersStrategy;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeLibrary;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.Description;
import org.aml.typesystem.meta.facets.Format;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.jsonschema2pojo.Annotator;
import org.jsonschema2pojo.CompositeAnnotator;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.GsonAnnotator;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.rules.RuleFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

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
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JType;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import com.sun.tools.xjc.AbortException;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.Language;
import com.sun.tools.xjc.ModelLoader;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

public class JavaWriter {

	private JCodeModel mdl = new JCodeModel();
	protected HashMap<AbstractType, JType> defined = new HashMap<>();

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
			return escape(p.id());
		}
	};

	public JDefinedClass defineClass(AbstractType t, ClassType type) {
		String fullyQualifiedName = nameGenerator.fullyQualifiedName(t);
		try {
			JDefinedClass _class = mdl._class(fullyQualifiedName, type);
			if (config.addGenerated) {
				_class.annotate(Generated.class).param("value", JavaWriter.class.getPackage().getName()).param("date",
						new Date().toString());
			}
			if (t.hasDirectMeta(Description.class)) {
				String description = t.oneMeta(Description.class).value();
				_class.javadoc().add(description);
			}
			return _class;
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

	@SuppressWarnings("unchecked")
	public void annotate(JAnnotatable annotable, AbstractType tp) {
		Set<TypeInformation> declaredMeta = tp.declaredMeta();
		for (TypeInformation i : declaredMeta) {
			if (i instanceof Annotation) {
				Annotation ann = (Annotation) i;
				AbstractType annotationType = ann.annotationType();
				if (config.getAnnotationConfig().skipReference(annotationType)) {
					continue;
				}
				if (annotationType == null) {
					// partially parsed raml file ignore loosly typed annotation
					continue;
				}
				JClass type = (JClass) getType(annotationType);
				if (type == null) {
					throw new IllegalStateException("Should never happen");
				}
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

	@SuppressWarnings("unchecked")
	protected void addParam(JAnnotationUse annotate, Object value, String name) {
		JClass annotationClass = annotate.getAnnotationClass();
		try {
			Class<?> cl = Class.forName(annotationClass.fullName());
			Method method = cl.getMethod(name);
			Class<?> returnType = method.getReturnType();
			if (returnType == String.class) {
				value = "" + value;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (value instanceof String) {
			annotate.param(name, "" + value);
		}
		if (value instanceof Double) {

			Double value2 = (Double) value;
			if (Long.valueOf(value2.intValue()).doubleValue() == value2.doubleValue()) {
				annotate.param(name, value2.intValue());
			} else {
				annotate.param(name, value2);
			}
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
		if (range.isAnnotation()) {
			if (config.getAnnotationConfig().skipDefinition(range)) {
				String fullyQualifiedName = nameGenerator.fullyQualifiedName(range);
				JClass ref = mdl.ref(fullyQualifiedName);
				defined.put(range, ref);
				return ref;
			}
		}
		if (defined.containsKey(range)) {
			return defined.get(range);
		}
		if (range.isBuiltIn()) {
			if (range.isString()) {
				return mdl._ref(String.class);
			}
			if (range.isFile()) {
				return mdl._ref(byte[].class);
			}
			if (range.isBoolean()) {
				if (config.wrapperStrategy == WrappersStrategy.ALWAYS) {
					return mdl._ref(Boolean.class);
				}
				if (config.wrapperStrategy == WrappersStrategy.OPTIONAL) {
					if (member != null && !member.isRequired()) {
						return mdl._ref(Boolean.class);
					}
				}
				return mdl._ref(boolean.class);
			}
			if (range.isNumber()) {
				return numberType(range, member);
			}
			if (range.isObject()) {
				return mdl._ref(Object.class);
			}
		}

		if (range.isAnonimous()) {
			if (range.isArray()) {
				AbstractType range2 = range.oneMeta(ComponentShouldBeOfType.class).range();

				return toArray(range2, member, convertComplexToAnnotation);
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
				return toArray(range2, member, convertComplexToAnnotation);
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
		if (range.isAnnotationType()) {
			JType define = new AnnotationTypeGenerator(this).define(range);
			defined.put(range, define);
			return define;
		}
		if (range.isSubTypeOf(BuiltIns.EXTERNAL)) {
			String externalSchemaContent = range.getExternalSchemaContent();
			// this is JSON schema
			if (externalSchemaContent.trim().startsWith("{")) {
				GenerationConfig jsonSchemaGenerationConfig = new DefaultGenerationConfig();
				SchemaMapper mp = new SchemaMapper(
						new RuleFactory(jsonSchemaGenerationConfig, getAnnotator(), new SchemaStore()),
						new SchemaGenerator());
				String fullyQualifiedName = nameGenerator.fullyQualifiedName(range);
				URL storeContentToTempFile = storeContentToTempFile(externalSchemaContent);
				URL json = storeContentToTempFile;
				try {
					JType t = mp.generate(getModel(),
							fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1),
							fullyQualifiedName.substring(0, fullyQualifiedName.lastIndexOf('.')), json);
					defined.put(range, t);
					return t;
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			} else {
				try {
					Options opt = new Options();
					String fullyQualifiedName = nameGenerator.fullyQualifiedName(range);
					int lastIndexOf = fullyQualifiedName.lastIndexOf('.');
					String packageName = fullyQualifiedName.substring(0, lastIndexOf);
					opt.defaultPackage = packageName;
					opt.setSchemaLanguage(Language.XMLSCHEMA);
					URL storeContentToTempFile = storeContentToTempFile(externalSchemaContent);
					InputSource is = new InputSource(storeContentToTempFile.toExternalForm());
					opt.addGrammar(is);
					ErrorReceiver receiver = new ErrorReceiver() {

						@Override
						public void warning(SAXParseException exception) throws AbortException {
							exception.printStackTrace(System.err);
						}

						@Override
						public void info(SAXParseException exception) {
							exception.printStackTrace(System.err);
						}

						@Override
						public void fatalError(SAXParseException exception) throws AbortException {
							exception.printStackTrace(System.err);
						}

						@Override
						public void error(SAXParseException exception) throws AbortException {
							exception.printStackTrace(System.err);
						}
					};
					Model model = ModelLoader.load(opt, getModel(), receiver);

					Outline outline = model.generateCode(opt, receiver);
					HashSet<JDefinedClass> classList = new HashSet<>();
					String rootElement = "";
					for (ClassOutline co : outline.getClasses()) {
						JDefinedClass cl = co.implClass;
						if (cl.outer() == null) {
							for (JAnnotationUse c : cl.annotations()) {
								String nm = c.getAnnotationClass().name();
								if (nm.equals("XmlRootElement")) {
									classList.add(cl);
									JAnnotationValue jAnnotationValue = c.getAnnotationMembers().get("name");
									StringWriter w = new StringWriter();
									jAnnotationValue.generate(new JFormatter(w));
									rootElement = w.toString().substring(1, w.toString().length() - 1);
								}

							}
							classList.add(cl);
						}

					}
					if (classList.size() > 1) {
						throw new IllegalArgumentException(
								"Mapping to schemas with more then one element is not implemented");
					} else {

						JDefinedClass next = classList.iterator().next();
						JDefinedClass defineClass = mdl._class(fullyQualifiedName, ClassType.CLASS);
						defineClass._extends(next);
						defineClass.annotate(XmlRootElement.class).param("name", rootElement);
						defined.put(range, defineClass);
						return defineClass;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	static HashMap<String,Class<?>>formats=new HashMap<>();
	
	static{
		formats.put("int32", int.class);
		formats.put("int64", long.class);
		formats.put("int", int.class);
		formats.put("long", long.class);
		formats.put("float", float.class);
		formats.put("double", double.class);
		formats.put("int16", short.class);
		formats.put("int8", byte.class);
	}
	
	private JType numberType(AbstractType range, IProperty member) {
		Format oneMeta = range.oneMeta(Format.class);
		if (oneMeta==null&&member!=null){
			oneMeta=member.range().oneMeta(Format.class);
		}
		String format=null;
		
		defaultNumberFormat annotation = member.getDeclaredAt().annotation(defaultNumberFormat.class, true);
		if (annotation!=null){
			format=annotation.value();
		}
		if (range.isInteger()){
			defaultIntegerFormat ia = member.getDeclaredAt().annotation(defaultIntegerFormat.class, true);
			if (ia!=null){
				format=ia.value();
			}
		}
		if (oneMeta!=null){
			//int32, int64, int, long, float, double, int16, int8
			format=oneMeta.value();			
		}
		if (format!=null){
			Class<?> class1 = formats.get(format);
			JType _ref = mdl._ref(class1);
			if (class1!=null){
				switch (config.getWrapperStrategy()) {
				case NONE:
					return _ref;
				case ALWAYS:
					return _ref.boxify();
				case OPTIONAL:
					return member != null && !member.isRequired()?_ref.boxify():_ref;
				default:
					break;
				}
			}
		}
		if (range.isInteger()) {
			switch (config.getIntegerFormat()) {
			case INT:
				switch (config.getWrapperStrategy()) {
				case NONE:
					return mdl._ref(int.class);
				case OPTIONAL:
					return member != null && !member.isRequired() ? mdl.ref(Integer.class) : mdl._ref(int.class);
				case ALWAYS:
					return mdl.ref(Integer.class);

				default:
					break;
				}
				return mdl._ref(int.class);

			case BIGINT:
				return mdl._ref(BigInteger.class);

			case LONG:
				switch (config.getWrapperStrategy()) {
				case NONE:
					return mdl._ref(long.class);
				case OPTIONAL:
					return member != null && !member.isRequired() ? mdl.ref(Integer.class) : mdl._ref(long.class);
				case ALWAYS:
					return mdl.ref(Long.class);

				default:
					break;
				}

			default:
				break;
			}
			return mdl._ref(int.class);
		}
		switch (config.getDoubleFormat()) {
		case DOUBLE:
			switch (config.getWrapperStrategy()) {
			case NONE:
				return mdl._ref(double.class);
			case OPTIONAL:
				return member != null && !member.isRequired() ? mdl.ref(Double.class) : mdl._ref(double.class);
			case ALWAYS:
				return mdl.ref(Long.class);

			default:
				break;
			}
			return mdl._ref(double.class);

		case BIGDECIMAL:
			return mdl._ref(BigDecimal.class);

		default:
			break;
		}
		return mdl._ref(double.class);
	}

	private JClass toArray(AbstractType range2, IProperty member, boolean convertComplexToAnnotation) {
		JType type = getType(range2, false, false, member);
		boolean containerStrategyCollection = config.containerStrategyCollection;
		boolean set = false;
		container annotation = range2.annotation(container.class, true);

		if (annotation != null) {
			String vl = annotation.value();
			if (vl.equals("list")) {
				containerStrategyCollection = true;
			}
			if (vl.equals("array")) {
				containerStrategyCollection = false;
			}
			if (vl.equals("set")) {
				containerStrategyCollection = true;
				set = true;
			}
		}
		if (containerStrategyCollection && !convertComplexToAnnotation) {
			if (set) {
				return mdl.ref(Set.class).narrow(type);
			}
			return mdl.ref(List.class).narrow(type);
		}
		return type.array();
	}
	
	protected JInvocation toArrayInit(AbstractType range2, IProperty member) {
		range2=range2.oneMeta(ComponentShouldBeOfType.class).range();
		JType type = getType(range2, false, false, member);
		boolean containerStrategyCollection = config.containerStrategyCollection;
		boolean set = false;
		container annotation = range2.annotation(container.class, true);

		if (annotation != null) {
			String vl = annotation.value();
			if (vl.equals("list")) {
				containerStrategyCollection = true;
			}
			if (vl.equals("array")) {
				containerStrategyCollection = false;
			}
			if (vl.equals("set")) {
				containerStrategyCollection = true;
				set = true;
			}
		}
		if (containerStrategyCollection ) {
			if (set) {
				return JExpr._new(mdl.ref(LinkedHashSet.class).narrow(type));
			}
			return JExpr._new(mdl.ref(ArrayList.class).narrow(type));
		}
		return null;
	}

	private Annotator getAnnotator() {
		ArrayList<Annotator> annotators = new ArrayList<>();
		if (config.isGsonSupport()) {
			annotators.add(new GsonAnnotator());
		}
		if (config.isJacksonSupport()) {
			annotators.add(new Jackson2Annotator());
		}
		CompositeAnnotator ac = new CompositeAnnotator(annotators.toArray(new Annotator[annotators.size()]));
		return ac;
	}

	@SuppressWarnings("deprecation")
	private URL storeContentToTempFile(String externalSchemaContent) {
		URL json = null;
		try {
			File createTempFile = File.createTempFile("ddd", ".xsd");
			FileOutputStream fs = new FileOutputStream(createTempFile);
			byte[] bytes = externalSchemaContent.getBytes("UTF8");
			fs.write(bytes, 0, bytes.length);
			fs.close();
			json = createTempFile.toURL();
		} catch (IOException e1) {
			throw new IllegalStateException(e1);
		}
		return json;
	}

	private String typePropertyName(IProperty member) {
		return member.getDeclaredAt().name() + member.id();
	}

	public static String escape(String x) {
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

	public void runCustomizers(PropertyCustomizerParameters propCustomizer) {
		config.customizers.forEach(x -> x.customize(propCustomizer));
	}

	public void runCustomizers(ClassCustomizerParameters cp) {
		config.classCustomizers.forEach(x -> x.customize(cp));
	}
}