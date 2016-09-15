package org.aml.raml2java;

//package {packageName};

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class PatternAndAdditionalTypeAdapter<T> extends TypeAdapter<T> {

	private static final class FixedExclusionStrategy implements ExclusionStrategy {
		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			Expose annotation = f.getAnnotation(Expose.class);
			if (annotation != null && !annotation.deserialize() && !annotation.serialize()) {
				return true;
			}
			return false;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}

	protected Gson gson;

	protected Class<T> target;

	protected HashSet<String> directFields = new HashSet<>();

	@SuppressWarnings("unchecked")
	public PatternAndAdditionalTypeAdapter(Gson gson, Class<T> target) {
		super();
		this.gson = gson;
		this.target = target;
		try {
			Field patternsField = target.getDeclaredField("$PATTERNS");
			patternsField.setAccessible(true);
			patterns = (String[]) patternsField.get(null);
			Field fieldsField = target.getDeclaredField("$FIELDS");
			Class<?> cl = target;
			while (cl != null && cl != Object.class) {
				Field[] declaredFields = cl.getDeclaredFields();
				for (Field f : declaredFields) {
					String name = f.getName();
					SerializedName annotation = f.getAnnotation(SerializedName.class);
					if (annotation != null) {
						name = annotation.value();
					}
					if (Modifier.isStatic(f.getModifiers())) {
						continue;
					}
					Expose annotation2 = f.getAnnotation(Expose.class);
					if (annotation2 != null) {
						if (!annotation2.deserialize() && !annotation2.serialize()) {
							continue;
						}
					}
					directFields.add(name);
				}
				cl = cl.getSuperclass();
			}
			fieldsField.setAccessible(true);
			String[] fields = (String[]) fieldsField.get(null);
			Field classesField = target.getDeclaredField("$CLASSES");
			classesField.setAccessible(true);
			classes = (Class[]) classesField.get(null);

			int i = 0;
			this.fields = new Field[fields.length];
			for (String f : fields) {
				Field declaredField = target.getDeclaredField(f);
				declaredField.setAccessible(true);
				this.fields[i] = declaredField;
				i++;
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {

		}
		generic = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(new HashMap<>()),
				FieldNamingPolicy.IDENTITY,
				Excluder.DEFAULT.withExclusionStrategy(new FixedExclusionStrategy(), true, true)).create(gson,
						TypeToken.get(target));
		try {
			Field declaredField = generic.getClass().getDeclaredField("boundFields");
			declaredField.setAccessible(true);
			Map<String, Object> object = (Map<String, Object>) declaredField.get(generic);
			for (String s : object.keySet()) {
				Method ms = target.getMethod("get" + Character.toUpperCase(s.charAt(0)) + s.substring(1));
				if (true) {
					Object boundFieldImpl = object.get(s);
					Field fl = boundFieldImpl.getClass().getDeclaredField("typeAdapter");
					fl.setAccessible(true);
					JsonAdapter annotation = ms.getAnnotation(JsonAdapter.class);
					Object adapter = null;
					if (annotation != null) {
						adapter = getTypeAdapter(null, gson, TypeToken.get(ms.getGenericReturnType()), annotation);
					}
					adapter = gson.getAdapter(TypeToken.get(ms.getGenericReturnType()));
					fl.set(boundFieldImpl, adapter);
				}
			}
		} catch (Exception e) {

			System.err.println(
					"Property overrides mechanism can not deal with this version of GSON please report an issue at: https://github.com/OnPositive/aml/issues");
		}
	}

	private Object getTypeAdapter(Object constructorConstructor, Gson gson2, TypeToken<?> typeToken,
			JsonAdapter annotation) {
		// should not happen
		throw new UnsupportedOperationException();
	}

	private TypeAdapter<T> generic;

	private String[] patterns;

	private Field[] fields;

	private Class<?>[] classes;

	@SuppressWarnings("unchecked")
	@Override
	public void write(JsonWriter out, T value) throws IOException {
		JsonObject jsonTree = generic.toJsonTree(value).getAsJsonObject();
		if (this.fields != null) {
			for (Field f : fields) {
				try {
					Map<String, Object> map = (Map<String, Object>) f.get(value);
					for (String s : map.keySet()) {
						if (!jsonTree.has(s)) {
							jsonTree.add(s, gson.toJsonTree(map.get(s)));
						}
					}

				} catch (IllegalArgumentException | IllegalAccessException e1) {
					throw new IllegalStateException();
				}
			}
		}
		Streams.write(jsonTree, out);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T read(JsonReader in) throws IOException {
		JsonElement vl = new JsonParser().parse(in);
		T result = generic.fromJsonTree(vl);
		JsonObject asJsonObject = vl.getAsJsonObject();
		if (this.directFields != null && this.patterns != null && this.classes != null) {
			Set<Entry<String, JsonElement>> entrySet = asJsonObject.entrySet();
			for (Entry<String, JsonElement> e : entrySet) {
				if (directFields.contains(e.getKey())) {
					continue;
				}
				for (int i = 0; i < patterns.length; i++) {
					String p = patterns[i];
					if (e.getKey().matches(p) || p.length() == 0) {
						Object fvl = gson.fromJson(e.getValue(), classes[i]);
						try {
							Map<String, Object> map = (Map<String, Object>) fields[i].get(result);
							map.put(e.getKey(), fvl);
						} catch (IllegalArgumentException | IllegalAccessException e1) {
							throw new IllegalStateException();
						}
						break;
					}
				}
			}

		}
		return result;
	}
}