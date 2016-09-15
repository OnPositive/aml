package {packageName};


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MapAndAdditionalSerializer extends StdSerializer<Object> {

	private HashMap<String, Field> directFields = new LinkedHashMap<>();
	private Class<?> _valueClass;
	private Field[] patternAndAdditionalFields;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected MapAndAdditionalSerializer(Class<?> t) {
		super((Class<Object>) t);
		this._valueClass = t;
		Field patternsField;
		try {
			patternsField = t.getDeclaredField("$PATTERNS");

			patternsField.setAccessible(true);
			Field fieldsField = t.getDeclaredField("$FIELDS");
			Class<?> cl = t;

			while (cl != null && cl != Object.class) {
				Field[] declaredFields = cl.getDeclaredFields();
				for (Field f : declaredFields) {
					String name = f.getName();
					f.setAccessible(true);
					JsonProperty nameAlterness = f.getAnnotation(JsonProperty.class);
					if (nameAlterness != null) {
						name = nameAlterness.value();
					}
					if (Modifier.isStatic(f.getModifiers())) {
						continue;
					}
					JsonIgnore jsonIgnore = f.getAnnotation(JsonIgnore.class);
					if (jsonIgnore != null) {
						if (jsonIgnore.value()) {
							continue;
						}
					}
					directFields.put(name, f);
				}
				cl = cl.getSuperclass();
			}
			fieldsField.setAccessible(true);
			String[] fields = (String[]) fieldsField.get(null);
			Field classesField = _valueClass.getDeclaredField("$CLASSES");
			classesField.setAccessible(true);
			int i = 0;
			this.patternAndAdditionalFields = new Field[fields.length];
			for (String f : fields) {
				Field declaredField = t.getDeclaredField(f);
				declaredField.setAccessible(true);
				this.patternAndAdditionalFields[i] = declaredField;
				i++;
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		for (String name : directFields.keySet()) {
			try {
				Object object = directFields.get(name).get(value);				
				if (object != null) {
					jgen.writeFieldName(name);
					jgen.writeObject(object);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
		for (Field pattern : patternAndAdditionalFields) {
			try {
				Map<String, Object> map = (Map<String, Object>) pattern.get(value);
				if (map != null) {
					for (String k : map.keySet()) {
						Object object = map.get(k);
						if (object != null) {
							jgen.writeFieldName(k);
							if (object != null) {
								jgen.writeObject(object);
							}
						}
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
		jgen.writeEndObject();
	}
}
