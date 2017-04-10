package org.aml.persistance.jdo;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.Query;

import org.aml.persistance.HALLinks;
import org.aml.persistance.PersistanceManager;

import com.google.gson.Gson;

public class JDOPersistanceManager implements PersistanceManager {

	protected javax.jdo.PersistenceManager manager;
	protected IdentityHashMap<String, MethodMeta> meta = new IdentityHashMap<>();

	MethodMeta getMeta(String meta) {
		if (this.meta.containsKey(meta)) {
			return this.meta.get(meta);
		}
		MethodMeta mmeta = new MethodMeta(meta);
		this.meta.put(meta, mmeta);
		return mmeta;
	}

	public JDOPersistanceManager(javax.jdo.PersistenceManager persistenceManager) {
		this.manager = persistenceManager;
	}

	@Override
	public <T> T create(Class<T> targetClass, String meta, Object... args) {
		@SuppressWarnings("unchecked")
		T instance = (T) args[args.length - 1];
		Class<?> actualTargetClass = getActualType(targetClass);
		Object actualInstance = transformInstance(actualTargetClass, "create", instance);
		fillReferences(targetClass, actualInstance, meta, args);
		Object result = manager.makePersistent(actualInstance);
		return (T) transformInstance(targetClass, "create", result);
	}

	private void fillReferences(Class<?> tc, Object actualInstance, String meta, Object[] args) {
		MethodMeta meta2 = getMeta(meta);
		for (int i = 0; i < meta2.list.size(); i++) {
			ArgumentMeta x = meta2.list.get(i);
			ReferenceInfo meta3 = x.meta(ReferenceInfo.class);
			try {
				if (meta3 != null) {
					Class<?> cl = Class.forName(actualInstance.getClass().getPackage().getName() + "." + meta3.clazz);
					Method[] methods = actualInstance.getClass().getMethods();
					for (Method m : methods) {
						if (m.getName().startsWith("set") && m.getParameterTypes().length == 1) {
							if (m.getParameterTypes()[0].equals(cl)) {
								Object findInstanceById = findInstanceById(meta2, cl, args);
								m.invoke(actualInstance, findInstanceById);
							}
						}
					}
				}
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new IllegalStateException(e);
			}

		}
	}

	@Override
	public <T> T update(Class<T> targetClass, String meta, Object... args) {
		@SuppressWarnings("unchecked")
		Object instance = findInstance(targetClass, meta, args);
		Object body = args[args.length - 1];
		Class<?> cl = body.getClass();
		Class<?> actualTargetClass = getActualType(targetClass);
		while (cl != null) {
			for (Field f : cl.getDeclaredFields()) {
				f.setAccessible(true);
				try {
					Object object = f.get(body);
					if (f.getName().equals("id")) {
						continue;
					}

					Method setter = findSetter(actualTargetClass, f);
					if (setter != null) {
						setter.invoke(instance, object);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException();
				} catch (InvocationTargetException e) {
					throw new IllegalStateException();
				}
			}
			cl = cl.getSuperclass();
		}
		Object makePersistent = manager.makePersistent(instance);
		return (T) transformInstance(targetClass, "update", makePersistent);

	}

	private Method findSetter(Class<?> actualTargetClass, Field f) {
		try {
			return actualTargetClass.getMethod(
					"set" + Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1), f.getType());
		} catch (NoSuchMethodException | SecurityException e) {

		}
		return null;
	}

	public Object findInstance(Class<?> targetClass, String meta, Object... args) {
		MethodMeta meta2 = getMeta(meta);
		Class<?> actualTargetClass = getActualType(targetClass);
		return findInstanceById(meta2, actualTargetClass, args);
	}

	protected Object findInstanceById(MethodMeta meta2, Class<?> actualTargetClass, Object... args) {
		String simpleName = actualTargetClass.getSimpleName();
		Query<?> newQuery = manager.newQuery(actualTargetClass);
		HashMap<String, Object> params = new HashMap<>();
		for (int i = 0; i < meta2.list.size(); i++) {
			ArgumentMeta argumentMeta = meta2.list.get(i);
			ReferenceInfo meta3 = argumentMeta.meta(ReferenceInfo.class);
			if (meta3 != null) {

				if (meta3.clazz.equals(simpleName)) {
					newQuery.filter(meta3.property + "==p" + i);
					newQuery.declareParameters(args[i].getClass().getName() + " " + "p" + i);
					params.put("p" + i, args[i]);
				}
			}
		}
		newQuery.setNamedParameters(params);
		List<?> executeList = newQuery.executeList();
		return executeList.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T delete(Class<T> targetClass, String meta, Object... args) {
		Object instance = findInstance(targetClass, meta, args);
		manager.deletePersistent(instance);
		return (T) transformInstance(targetClass, "delete", instance);
	}

	@Override
	public <T> T get(Class<T> targetClass, String meta, Object... id) {
		Object instance = findInstance(targetClass, meta, id);
		return (T) transformInstance(targetClass, "details", instance);
	}

	Field find(Class<?> cl, String name) {
		for (Field f : cl.getDeclaredFields()) {
			if (f.getName().equals(name)) {
				return f;
			}
		}
		if (cl.getSuperclass() != null) {
			return find(cl.getSuperclass(), name);
		}
		return null;
	}

	Class<?> getComponentType(Class<?> cl, String name) {
		Field find = find(cl, name);
		Class<?> type = find.getType();
		if (type.getComponentType() != null) {
			return type.getComponentType();
		}
		Type genericType = find.getGenericType();
		if (Collection.class.isAssignableFrom(type)) {
			if (genericType instanceof ParameterizedType) {
				ParameterizedType p0 = (ParameterizedType) genericType;
				Type t = p0.getActualTypeArguments()[0];
				if (t instanceof Class<?>) {
					return (Class<?>) t;
				}
			}
		}
		return null;
	}

	@Override
	public <T> T list(Class<T> targetClass, String meta, Object... args) {
		MethodMeta meta2 = getMeta(meta);
		PagingInfo pagingInfo = meta2.meta(PagingInfo.class);
		Class<?> requestType = null;
		boolean isTargetArray = false;
		if (targetClass.isArray()) {
			requestType = targetClass.getComponentType();
			isTargetArray = true;
		}
		long limit = 50;
		long offset = -1;
		long end = -1;
		if (pagingInfo != null) {
			if (pagingInfo.results != null) {
				requestType = getComponentType(targetClass, pagingInfo.results);
			}
			if (pagingInfo.offset != null) {
				int findParameterNum = meta2.findParameterNum(pagingInfo.offset);
				if (findParameterNum != -1) {
					if (args[findParameterNum] != null) {
						offset = Long.parseLong("" + args[findParameterNum]);
					} else {
						offset = 0;
					}
				}
				findParameterNum = meta2.findParameterNum(pagingInfo.limit);
				if (findParameterNum != -1 && args[findParameterNum] != null) {
					limit = (int) Long.parseLong("" + args[findParameterNum]);
					end = offset + limit;
				} else {
					limit = 20;
					end = offset + limit;
				}
			}
		}
		Class<?> actualRequestType = getActualType(requestType);
		Query<?> newQuery = manager.newQuery(actualRequestType);

		newQuery.setResult("count(this)");
		fillParams(meta2, actualRequestType, newQuery, args);
		Long count = (Long) newQuery.executeResultUnique();
		newQuery = manager.newQuery(actualRequestType);
		fillParams(meta2, actualRequestType, newQuery, args);
		if (end != -1) {
			newQuery = newQuery.range(offset, end);
		}
		List<?> executeList = newQuery.executeList();
		executeList = transform(executeList, requestType);
		if (isTargetArray) {

		} else {
			if (pagingInfo.results != null) {
				try {
					T result = targetClass.newInstance();
					if (pagingInfo.results != null) {
						Field find = find(targetClass, pagingInfo.results);
						if (find != null) {
							find.setAccessible(true);
						}
						if (find.getType().isArray()) {
							Class<?> res = find.getType().getComponentType();
							Object newInstance = Array.newInstance(res, executeList.size());
							for (int i = 0; i < executeList.size(); i++) {
								Array.set(newInstance, i, executeList.get(i));
							}
							find.set(result, newInstance);
						} else {
							find.set(result, executeList);
						}
					}
					if (pagingInfo.total != null) {
						Field find = find(targetClass, pagingInfo.total);
						if (find != null) {
							find.setAccessible(true);
						}
						if (find.getType() == int.class || find.getType() == Integer.class) {
							find.set(result, count.intValue());
						} else {
							find.set(result, count);
						}
					}
					return result;
				} catch (InstantiationException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		return null;
	}

	protected void fillParams(MethodMeta meta2, Class<?> actualRequestType, Query<?> newQuery, Object... args) {
		HashMap<String, Object> parameters = new HashMap<>();
		for (int i = 0; i < meta2.list.size(); i++) {
			ArgumentMeta argumentMeta = meta2.list.get(i);
			ReferenceInfo meta3 = argumentMeta.meta(ReferenceInfo.class);
			if (meta3 != null) {
				// okey now we should check for parent key
				Class<?> cl;
				try {
					cl = Class.forName(actualRequestType.getPackage().getName() + "." + meta3.clazz);
					Field[] fields = actualRequestType.getDeclaredFields();
					for (Field f : fields) {

						if (f.getType().equals(cl)) {
							newQuery.setFilter(f.getName() + "." + meta3.property + "==p" + i);
							newQuery.declareParameters("long p" + i);
							parameters.put("p" + i, args[i]);
							// okey.
						}

					}
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		
		newQuery.setNamedParameters(parameters);
	}

	Object transformInstance(Class<?> requestType, String role, Object val) {
		try {
			Object result = requestType.newInstance();
			copyTo(requestType, val, result,role);
			return result;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void copyTo(Class<?> requestType, Object val, Object result, String relation)
			throws IllegalAccessException {
		Class<?> clazz = requestType;
		Class<? extends Object> class1 = val.getClass();

		while (clazz != null) {
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field f : declaredFields) {
				f.setAccessible(true);
				Field find = find(class1, f.getName());
				if (find != null) {
					VisibleWhen annotation = find.getAnnotation(VisibleWhen.class);
					if (annotation != null) {
						String[] split = annotation.value().trim().split(",");
						boolean found = false;
						for (String s : split) {
							if (s.trim().equals("+" + relation)) {
								found = true;
							}
						}
						if (!found) {
							f.set(result, null);
							continue;
						}
					}
					Object vl = getValue(val, find);
					if (vl != null) {
						if (!vl.getClass().getName().startsWith("java.")&&!f.getType().isAssignableFrom(vl.getClass())){
							vl=transformInstance(f.getType(), "parent", vl);							
						}
						f.set(result, vl);
					}
				}
				else if (HALLinks.class.isAssignableFrom(f.getType())){
					HALLinks links=new HALLinks();
					links.put("self", "hello");
					f.set(result, links);
					try {
						Field tf=requestType.getDeclaredField("_teplateLinks");
						tf.setAccessible(true);
						String linksTemplate=(String) tf.get(null);
						List<Object>map=(List<Object>) new Gson().fromJson(linksTemplate, Object.class);
						for (Object i:map){
							Map<String,Object>mp=(Map<String, Object>) i;
							String key=(String) mp.get("rel");
							String url=(String) mp.get("url");
							LinkedHashMap<String, Object>rs=new LinkedHashMap<>();
							rs.put("href", url);
							rs.put("method", mp.get("method"));
							links.put(key,rs);
						}
						
					} catch (NoSuchFieldException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			clazz = clazz.getSuperclass();
		}
	}

	private Object getValue(Object val, Field find) {
		String name = find.getName();
		Method m = null;
		try {
			m = find.getDeclaringClass().getMethod("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
		} catch (NoSuchMethodException | SecurityException e) {
			try {
				m = find.getDeclaringClass()
						.getMethod("is" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
			} catch (NoSuchMethodException | SecurityException e1) {

			}

		}
		if (m != null) {
			try {
				return m.invoke(val);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private List<?> transform(List<?> executeList, Class<?> requestType) {
		ArrayList<Object> result = new ArrayList<>();
		for (Object r : executeList) {
			result.add(transformInstance(requestType, "list", r));
		}
		return result;
	}

	private Class<?> getActualType(Class<?> requestType) {
		String name = requestType.getPackage().getName() + ".persistence";
		String fullName = name + "." + requestType.getSimpleName();
		try {
			return Class.forName(fullName);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
}
