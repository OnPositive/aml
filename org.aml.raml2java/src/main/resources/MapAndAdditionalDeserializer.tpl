package {packageName};


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapAndAdditionalDeserializer extends StdDeserializer<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, Field> directFields = new HashMap<>();
	private String[] patterns;
	private Class<?>[] classes;
	private Field[] patternAndAdditionalFields;
	

	public MapAndAdditionalDeserializer(Class<?> target) {
		super(target);		
		try {
			Field patternsField =_valueClass.getDeclaredField("$PATTERNS");
			patternsField.setAccessible(true);
			patterns = (String[]) patternsField.get(null);
			Field fieldsField = _valueClass.getDeclaredField("$FIELDS");
			Class<?> cl = target;

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
			classes = (Class[]) classesField.get(null);
			int i = 0;
			this.patternAndAdditionalFields = new Field[fields.length];
			for (String f : fields) {
				Field declaredField = target.getDeclaredField(f);
				declaredField.setAccessible(true);
				this.patternAndAdditionalFields[i] = declaredField;
				i++;
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		Iterator<String> itr = node.fieldNames();
		Object result=null;
		try {
			result = _valueClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException();
		}
		while (itr.hasNext()) {
			String next = itr.next();
			JsonNode jsonNode = node.get(next);
			if (directFields.containsKey(next)) {
				Object readValue = oc.readValue(jsonNode.traverse(),
						TypeFactory.defaultInstance().constructType(directFields.get(next).getGenericType()));
				try {
					directFields.get(next).set(result, readValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException();
				}
				continue;
			}
			for (int i=0;i<patterns.length;i++){
				String p=patterns[i];
				if (next.matches(p)||p.length()==0){
					JavaType constructType = TypeFactory.defaultInstance().constructType(classes[i]);
					Object readValue = oc.readValue(jsonNode.traverse(),constructType);
					try {
						Map<String,Object>map=(Map<String, Object>) patternAndAdditionalFields[i].get(result);
						map.put(next, readValue);
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						throw new IllegalStateException();
					}
					break;
				}
			}
		}
		return result;
	}
}