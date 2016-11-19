package org.aml.typesystem.yamlwriter;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.aml.apimodel.INamedParam;
import org.aml.typesystem.AbstractType;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.StreamReader;

public abstract class GenericWriter {

	protected static final String NOVALUE = "<<NOVALUE!!!";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected final static void addScalarField(String name, LinkedHashMap tr, Object source, Supplier<Object> acc) {
		Object vl = acc.get();
		if (vl != null) {
			if (vl instanceof Collection) {
				Collection<?> c = (Collection<?>) vl;
				if (c.isEmpty()) {
					return;
				}
			}
			if (vl instanceof String) {
				String ss = (String) vl;
				vl = cleanupStringValue(ss);
			}
			tr.put(name, vl);
		}
	}
	protected boolean inParam=false;
	protected Object dumpNamedParam(INamedParam r) {
		inParam=true;
		try{
		return typeRespresentation(r.getTypeModel(), false);
		}finally {
			inParam=false;
		}
	}
	
	protected abstract Object typeRespresentation(AbstractType typeModel, boolean allowNamed);
	protected abstract Object dumpType(AbstractType typeModel);
	
	protected LinkedHashMap<String, Object> fillRromList(Collection<AbstractType> types2) {
		LinkedHashMap<String, Object> tps = new LinkedHashMap<>();
		ArrayList<AbstractType> ts = new ArrayList<>(types2);
		Collections.sort(ts, new Comparator<AbstractType>() {

			@Override
			public int compare(AbstractType o1, AbstractType o2) {
				int s1 = 0;
				int s2 = 0;
				if (o1.isObject()) {
					s1 = 1000;
				}
				if (o2.isObject()) {
					s2 = 1000;
				}
				if (s1 == s2) {
					return o1.name().compareTo(o2.name());
				}
				return s1 - s2;
			}
		});
		for (AbstractType t : ts) {
			if (t.isAnonimous()){
				continue;
			}
			tps.put(t.name(), dumpType(t));
		}
		return tps;
	}
	
	protected final LinkedHashMap<String, Object> toMap(Object obj) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field f : declaredFields) {
			f.setAccessible(true);
			try {
				Object object = f.get(obj);
				if (object != null && !object.equals(Boolean.FALSE)) {
					result.put(f.getName(), object);
				}
			} catch (Exception e) {
				throw new IllegalStateException();
			}
		}
		return result;
	}

	protected final String dumpMap(LinkedHashMap<Object, Object> toStore, String header) {
		DumperOptions dumperOptions = new DumperOptions();
		toStore = cleanMap(toStore);
		dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
		dumperOptions.setAllowUnicode(true);
		Yaml rl = new Yaml(dumperOptions);
		StringWriter stringWriter = new StringWriter();
		BufferedWriter ws = new BufferedWriter(stringWriter);
		try {
			ws.write(header);
			ws.newLine();
			rl.dump(toStore, ws);
			return stringWriter.toString().replaceAll(NOVALUE, "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final LinkedHashMap<Object, Object> cleanMap(Map<Object, Object> toStore) {
		LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
		for (Object k : toStore.keySet()) {
			if (k instanceof String) {
				k = cleanupStringValue((String) k);
			}
			Object object = toStore.get(k);
			object = cleanObject(object);
			result.put(k, object);			
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public final Object cleanObject(Object object) {
		if (object instanceof Map) {
			object = cleanMap((Map<Object, Object>) object);
		} else if (object instanceof String) {
			object = cleanupStringValue((String) object);
		} else if (object instanceof List) {
			object = cleanupList((List<?>) object);
		}
		else if (object instanceof Set) {
			object = cleanupList((List<?>) new ArrayList<>((Set)object));
		}
		return object;
	}

	private ArrayList<?> cleanupList(List<?> object) {
		ArrayList<Object> array = new ArrayList<>();
		for (Object o : object) {
			array.add(cleanObject(o));
		}
		return array;
	}

	protected final <T> void dumpCollection(String prefix, LinkedHashMap<String, Object> target, Collection<T> value, Function<T, Object> func, Function<T, Object> keyFunc) {
		if (!value.isEmpty()) {
			LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
			for (T v : value) {
				Object apply = func.apply(v);
				if (apply instanceof Map) {
					@SuppressWarnings("rawtypes")
					Map m = (Map) apply;
					if (m.isEmpty()) {
						apply = NOVALUE;
					}
				}
				result.put(keyFunc.apply(v), apply);
			}
			target.put(prefix, result);
		}
	}

	public static final  String cleanupStringValue(String ss) {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < ss.length(); i++) {
			if (StreamReader.isPrintable(ss.charAt(i))) {
				bld.append(ss.charAt(i));
			} else {
				continue;
			}
		}
		return bld.toString();
	
	}

	public GenericWriter() {
		super();
	}

}