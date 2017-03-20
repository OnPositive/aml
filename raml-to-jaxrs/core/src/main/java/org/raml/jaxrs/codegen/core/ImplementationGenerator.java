package org.raml.jaxrs.codegen.core;

import static org.apache.commons.lang.StringUtils.capitalize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.jdo.annotations.Element;

import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.MimeType;
import org.aml.apimodel.Resource;
import org.aml.apimodel.Response;
import org.aml.core.mappings.create;
import org.aml.core.mappings.delete;
import org.aml.core.mappings.details;
import org.aml.core.mappings.list;
import org.aml.core.mappings.reference;
import org.aml.core.mappings.update;
import org.aml.persistance.ResourceWithPersitanceManager;
import org.aml.persistance.jdo.VisibleWhen;
import org.aml.raml2java.JavaGenerationConfig.MultipleInheritanceStrategy;
import org.aml.raml2java.ClassCustomizerParameters;
import org.aml.raml2java.IClassCustomizer;
import org.aml.raml2java.JavaWriter;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpressionImpl;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JMods;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

public class ImplementationGenerator extends Generator {

	HashMap<JDefinedClass, String> original = new HashMap<>();

	HashMap<AbstractType, JType> defined = new HashMap<>();

	static class ChildDefinition {
		public ChildDefinition(String pName, AbstractType type, JDefinedClass child) {
			this.propName = pName;
			this.range = type;
			this.child = child;
		}

		String propName;
		AbstractType range;
		JDefinedClass child;
	}

	HashMap<AbstractType, ArrayList<ChildDefinition>> children = new HashMap<>();

	public void recordParent(AbstractType type, String pName, AbstractType range, JDefinedClass clazz) {
		if (range.isAnonimous()) {
			range = range.superType();
		}
		if (defined.containsKey(range)) {
			processChildDefinition(defined.get(range), new ChildDefinition(pName, type, clazz));
		} else {
			ArrayList<ChildDefinition> arrayList = children.get(range);
			if (arrayList == null) {
				arrayList = new ArrayList<>();
				children.put(range, arrayList);
			}
			arrayList.add(new ChildDefinition(pName, type, clazz));
		}
	}

	protected void processChildDefinition(JType jType, ChildDefinition childDefinition) {
		String trtLabel = childDefinition.range.name().toLowerCase() + "s";
		JDefinedClass jDefinedClass = (JDefinedClass) jType;
		if (jDefinedClass.fields().get(trtLabel) != null) {
			return;
		}
		JVar init = jDefinedClass.field(JMod.PRIVATE, context.getCodeModel().ref(Set.class).narrow(childDefinition.child), trtLabel)
				.init(JExpr._new(context.getCodeModel().ref(HashSet.class)));
		init.annotate(Element.class)
				.param("dependent", "true");
		init.annotate(VisibleWhen.class).param("value","+none");
		// System.out.println(jType);
	}

	@Override
	protected Context createContext(Api raml, Configuration configuration) throws IOException {
		return new Context(configuration, raml) {

			JavaWriter dbWriter = new JavaWriter();

			{
				dbWriter.setDefaultPackageName(configuration.getBasePackageName() + "."
						+ configuration.getModelPackageName() + ".persistence");
				dbWriter.setModel(getWriter().getModel());
				dbWriter.getConfig().setMultipleInheritanceStrategy(MultipleInheritanceStrategy.ALWAYS_PLAIN);
				dbWriter.getConfig().getClassCustomizers()
						.add(new ImplementationClassCustomizer(ImplementationGenerator.this));
				getWriter().getConfig().getClassCustomizers().add(new IClassCustomizer() {
					
					@Override
					public void customize(ClassCustomizerParameters parameters) {
						parameters.getClazz().annotate(JsonInclude.class).param("value", com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
					}
				});
			}

			@Override
			public JType getType(AbstractType tp) {
				if (!defined.containsKey(tp) && !tp.name().startsWith("Anonimous") && !tp.isAnonimous()) {
					JType type = dbWriter.getType(tp);
					defined.put(tp, type);
				}
				return super.getType(tp);
			}

			/**
			 * <p>
			 * createResourceInterface.
			 * </p>
			 *
			 * @param name
			 *            a {@link java.lang.String} object.
			 * @return a {@link com.sun.codemodel.JDefinedClass} object.
			 * @throws java.lang.Exception
			 *             if any.
			 */
			public JDefinedClass createResourceInterface(final String name) throws Exception {
				String actualName = cleanName(name);

				final JPackage pkg = codeModel
						._package(configuration.getBasePackageName() + "." + configuration.getRestIFPackageName());
				JClass intefaceToImpl = codeModel.directClass(pkg.name() + "." + actualName);

				JDefinedClass rs = pkg._class(actualName + "Impl");
				original.put(rs, intefaceToImpl.fullName());
				rs._extends(ResourceWithPersitanceManager.class);
				rs._implements(intefaceToImpl);
				return rs;
			}
		};
	}

	protected AbstractType determineTargetClass(Action a) {
		for (MimeType m : a.body()) {
			return m.getTypeModel();
		}
		for (Response r : a.responses()) {
			for (MimeType m : r.body()) {
				return m.getTypeModel();
			}
		}
		String ref = findRef(a.resource().uriParameters());
		if (ref == null) {
			ref = findRef(a.queryParameters());

		}
		if (ref == null) {
			ref = findRef(a.headers());
		}
		if (ref != null) {
			return a.resource().getApi().types().getType(ref);
		}
		return null;
	}

	String findRef(List<? extends INamedParam> n) {
		for (INamedParam p : n) {
			String rs = tryProcess(p.getTypeModel().annotation(reference.class, false));
			if (rs != null) {
				return rs;
			}
		}
		return null;
	}

	String tryProcess(reference r) {
		if (r != null) {
			int withDot = r.value().lastIndexOf('.');
			String rs = r.value().substring(0, withDot);
			return rs;
		}
		return null;
	}

	JSONObject generateParameterInfo(Collection<? extends TypeInformation> t, String name) {
		JSONObject object = new JSONObject();
		t.forEach(x -> {
			if (x instanceof Annotation) {
				Annotation a = (Annotation) x;
				Object vl = a.value();

				try {
					object.put(a.annotationType().name(), vl);
				} catch (JSONException e) {
					throw new IllegalStateException(e);
				}

			}
		});
		try {
			object.put("name", name);
		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}
		return object;
	}

	protected void customizeMethod(JDefinedClass resourceInterface, JMethod method, Action action) {
		method.mods().setPublic();
		JSONArray infoArray = new JSONArray();
		infoArray.put(generateParameterInfo(action.annotations(), ""));
		Resource resource = action.resource();
		while (resource != null) {
			resource.uriParameters()
					.forEach(x -> infoArray.put(generateParameterInfo(x.getTypeModel().meta(), x.getKey())));
			resource = resource.parentResource();
		}
		action.headers().forEach(x -> infoArray.put(generateParameterInfo(x.getTypeModel().meta(), x.getKey())));
		action.queryParameters()
				.forEach(x -> infoArray.put(generateParameterInfo(x.getTypeModel().meta(), x.getKey())));
		action.body().forEach(x -> {
			infoArray.put(generateParameterInfo(x.getTypeModel().meta(), "body"));
		});
		JMods mods = resourceInterface.field(JMod.STATIC, String.class, method.name() + "_meta")
				.init(JExpr.lit(infoArray.toString())).mods();
		mods.setFinal(true);
		mods.setPrivate();

		String name = method.type().name();
		Optional<Response> findAny = action.responses().stream().filter(x -> x.code().startsWith("2")).findAny();

		if (findAny.isPresent()) {
			List<MimeType> body = findAny.get().body();
			AbstractType ts = null;
			for (MimeType t : body) {
				ts = t.getTypeModel();
			}
			String resultName = ".withOK()";
			String code = findAny.get().code();
			if (ts != null) {
				if (code.equals("201")) {
					resultName = ".withJsonCreated(result)";
				} else if (code.equals("203")) {
					resultName = ".withJsonNonAuthoritativeInformation(result)";
				} else
					resultName = ".withJsonOK(result)";
			} else {
				if (code.equals("204")) {
					resultName = ".withNoContent()";
				}
			}
			String rName = resultName;
			delete annotation = action.annotation(delete.class);
			if (annotation != null) {
				AbstractType determineTargetClass = determineTargetClass(action);
				String kind = "delete";
				addCode(method, determineTargetClass, kind);
			}
			details det = action.annotation(details.class);
			if (det != null) {
				AbstractType determineTargetClass = determineTargetClass(action);
				String kind = "get";
				addCode(method, determineTargetClass, kind);
			}
			list list = action.annotation(list.class);
			if (list != null) {
				AbstractType determineTargetClass = determineTargetClass(action);
				String kind = "list";
				addCode(method, determineTargetClass, kind);
			}
			create create = action.annotation(create.class);
			if (create != null) {
				AbstractType determineTargetClass = determineTargetClass(action);
				String kind = "create";
				addCode(method, determineTargetClass, kind);
			}
			update update = action.annotation(update.class);
			if (update != null) {
				AbstractType determineTargetClass = determineTargetClass(action);
				String kind = "update";
				addCode(method, determineTargetClass, kind);
			}
			method.body()._return(new JExpressionImpl() {

				@Override
				public void generate(JFormatter f) {
					f.p(name + rName);
				}
			});
			return;
		}
		System.err.println("Method without okeish response - can not proceed correctly");
		return;
	}

	protected void addCode(JMethod method, AbstractType tp, String kind) {
		JType type = context.getType(tp);
		if (type == null) {
			return;
		}
		JInvocation iv = JExpr.invoke(JExpr.ref("manager"), kind).arg(JExpr.dotclass((JClass) type))
				.arg(JExpr.ref(method.name() + "_meta"));

		method.body().decl((JClass) type, "result", iv);
		method.params().forEach(x -> iv.arg(x));
	}

	protected JClass createResourceMethodReturnType(final String methodName, final Action action,
			final JDefinedClass resourceInterface) throws Exception {
		String className = capitalize(methodName) + "Response";
		return context.getCodeModel().directClass(className);
	}

}
