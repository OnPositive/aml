package org.aml.persistance.jdo;

import java.lang.reflect.Field;
import java.util.Map;

public class MapInit extends CanInit{

	@Override
	void init(Object value) {
		Map<String,Object>mm=(Map<String, Object>) value;
		Class<?> class1 = this.getClass();
		while (class1!=null){
			Field[] declaredFields = class1.getDeclaredFields();
			for (Field f:declaredFields){
				f.setAccessible(true);
				Object val=mm.get(f.getName());
				try {
					f.set(this, val);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
			class1=(Class<?>) class1.getSuperclass();
		}
	}
}
