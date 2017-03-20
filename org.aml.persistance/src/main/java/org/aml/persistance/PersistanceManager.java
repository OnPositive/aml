package org.aml.persistance;

public interface PersistanceManager {

	<T> T list(Class<T>targetClass,String meta,Object... args);
	<T> T create(Class<T>targetClass,String meta,Object... args);
	<T> T update(Class<T>targetClass,String meta,Object... args);
	<T> T delete(Class<T>targetClass,String meta,Object... id);
	<T> T get(Class<T>targetClass,String meta,Object... id);
}
