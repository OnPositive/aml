package org.aml.registry.usages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.aml.apimodel.Action;
import org.aml.apimodel.Annotable;
import org.aml.apimodel.Api;
import org.aml.apimodel.INamedParam;
import org.aml.apimodel.Resource;
import org.aml.apimodel.TopLevelModel;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.IType;
import org.aml.typesystem.ITypeLibrary;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.restrictions.IRangeRestriction;

public class UsageCollector {

	protected TopLevelModel indexed;

	protected Stack<String> path = new Stack<>();

	public static final String RESOURCE = "R";
	public static final String METHOD = "M";
	public static final String RESPONSE = "r";
	public static final String TYPE = "T";
	public static final String ANNOTATIONTYPE = "A";

	protected LinkedHashMap<String, ArrayList<String>> usagesByLocation = new LinkedHashMap<>();

	public LinkedHashMap<String, ArrayList<String>> getResults() {
		return usagesByLocation;
	}

	void visit(TopLevelModel mdl) {
		this.indexed = mdl;
		this.visitAnnotations(mdl);
		this.visit(mdl.types());
		this.visit(mdl.annotationTypes());
		if (mdl instanceof Api) {
			Api api = (Api) mdl;
			api.allResources().forEach(x -> {
				visitResource(x);
			});
		}
	}

	private void pushEntry(String kind, String id) {
		path.push(kind);
		path.push(id);
	}

	private void popEntry() {
		path.pop();
		path.pop();
	}

	private void visitResource(Resource x) {
		pushEntry("R", x.getUri());
		try {
			visitAnnotations(x);
			x.uriParameters().forEach(this::visitParam);
			x.methods().forEach(this::visitMethod);
		} finally {
			popEntry();
		}
	}

	private void visitMethod(Action x) {
		pushEntry("M", x.method());
		try {
			x.headers().forEach(this::visitParam);
			visitAnnotations(x);
			x.queryParameters().forEach(this::visitParam);
			x.body().forEach(b -> {
				this.visitType(b.getTypeModel());
			});
			x.responses().forEach(r -> {
				pushEntry(RESPONSE, r.code());
				try {
					visitAnnotations(r);
					r.headers().forEach(this::visitParam);
					r.body().forEach(b -> {
						this.visitType(b.getTypeModel());
					});
				} finally {
					popEntry();
				}
			});
		} finally {
			popEntry();
		}
	}

	private void visitParam(INamedParam p) {
		visitType(p.getTypeModel());
	}

	private void visit(ITypeRegistry types) {
		for (IType t : types) {
			visitType(t);
		}
	}

	protected HashSet<IType> visited = new HashSet<>();

	private void visitType(IType t) {
		
		AbstractType at = (AbstractType) t;
		if (at == null) {
			return;
		}
		if (!at.isAnonimous()) {
			if (at.isAnnotationType()) {
				pushEntry(ANNOTATIONTYPE, at.name());
			} else {
				pushEntry(TYPE, at.name());
			}
		}
		try {
			if (t != null) {
				ITypeLibrary source = at.getSource();
				
				if (source != null && source != indexed) {
					String sourceLocation = source.getSourceLocation();
					if (sourceLocation != null) {
						ArrayList<String> arrayList = usagesByLocation.get(sourceLocation);
						if (arrayList == null) {
							arrayList = new ArrayList<>();
							usagesByLocation.put(sourceLocation, arrayList);
						}
						arrayList.add(buildPath());
					}
					return;
				}
				if (visited.add(t)) {
					t.superTypes().forEach(x -> visitType(x));
					for (TypeInformation i : t.declaredMeta()) {
						visitInfo(i);
					}
				}
			}
		} finally {
			if (!at.isAnonimous()) {
				popEntry();
			}
		}
	}

	private String buildPath() {
		StringBuilder bld = new StringBuilder();
		for (String s : path) {
			bld.append(';');
			bld.append(s);
		}
		return bld.toString();
	}

	private void visitInfo(TypeInformation i) {
		if (i instanceof IRangeRestriction) {
			IRangeRestriction rs = (IRangeRestriction) i;
			visitType(rs.range());
		}
		if (i instanceof Annotation) {
			Annotation a = (Annotation) i;
			visitType(a.annotationType());
		}
	}

	void visitAnnotations(Annotable a) {
		for (Annotation an : a.annotations()) {
			visitInfo(an);
		}
	}
}
