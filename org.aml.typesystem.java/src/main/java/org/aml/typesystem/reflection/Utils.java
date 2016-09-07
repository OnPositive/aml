package org.aml.typesystem.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.aml.typesystem.ITypeModel;

/**
 * <p>Utils class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class Utils {
	
	/**
	 * <p>isCollection.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	public static boolean isCollection(Class<?> clazz){
		
		if(clazz.getCanonicalName().equals("java.util.Collection")){
			return true;
		}
		
		Class<?> cl = clazz;
		while(cl!=null){
			for(Class<?> iCl : cl.getInterfaces()){
				if(isCollection(iCl)){
					return true;
				}
			}
			cl = cl.getSuperclass();			
		}		
		return false;		
	}
	
	/**
	 * <p>getParameterTypes.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link java.util.List} object.
	 */
	public static List<ITypeModel> getParameterTypes(Object obj){
		Class<?> type = null;
		Type gType = null;
		if(obj instanceof Field){
			type = ((Field)obj).getType();
			gType = ((Field)obj).getGenericType();
		}
		else if(obj instanceof Method){
			type = ((Method)obj).getReturnType();
			gType = ((Method)obj).getGenericReturnType();
		}
		if(type==null){
			return null;
		}
		ArrayList<ITypeModel> list = new ArrayList<ITypeModel>(); 
		if(Collection.class.isAssignableFrom(type)){
			if(gType instanceof ParameterizedType){
				Type[] args = ((ParameterizedType)gType).getActualTypeArguments();
				if(args!=null&&args.length!=0){
					if(args[0] instanceof Class){
						list.add(new ReflectionType((Class<?>) args[0]));
					}
					else if(args[0] instanceof ParameterizedType){
						Type rawType = ((ParameterizedType)args[0]).getRawType();
						if(rawType instanceof Class){
							list.add(new ReflectionType((Class<?>) rawType));
						}
					}
					
				}
			}
		}
		else if(Map.class.isAssignableFrom(type)){
			if(gType instanceof ParameterizedType){
				Type[] args = ((ParameterizedType)gType).getActualTypeArguments();
				if(args!=null&&args.length>=2){
					for(int i = 0 ; i < 2 ; i++ ){
						Type t  = args[i];
						if(t instanceof Class){
							list.add(new ReflectionType((Class<?>) t));
						}
						else if(args[0] instanceof ParameterizedType){
							Type rawType = ((ParameterizedType)t).getRawType();
							if(rawType instanceof Class){
								list.add(new ReflectionType((Class<?>) rawType));
							}
						}
					}
					
				}
			}
		}
		else{
			list.add(new ReflectionType(type));
		}
		return list;
	}
	
	/**
	 * <p>extractReturnJavadoc.</p>
	 *
	 * @param javadocComment a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String extractReturnJavadoc(String javadocComment) {
		return extractParamJavadoc(javadocComment, "return");
	}
	
	
	/**
	 * <p>extractParamJavadoc.</p>
	 *
	 * @param javadocComment a {@link java.lang.String} object.
	 * @param pName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String extractParamJavadoc(String javadocComment, String pName) {
		String str = javadocComment;
		if(str==null){
			return null;
		}
		String tag = "@"+pName;
		int ind1 = str.indexOf(tag);
		if(ind1<0){
			return null;
		}
		ind1+=tag.length();
		int ind2 = str.indexOf("\n",ind1);
		if(ind2<0){
			ind2 = str.length();
		}
		return str.substring(ind1,ind2).replaceAll("(\\n)(\\s*)\\*(\\s*)","\n").trim();
	}
	
	
	/**
	 * <p>extractMethodJavadoc.</p>
	 *
	 * @param javadocComment a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String extractMethodJavadoc(String javadocComment) {
		String str = javadocComment;
		if(str==null){
			return null;
		}
		int ind = str.indexOf("@");
		if(ind<0){
			ind = str.length();
		}
		return str.substring(0,ind).replaceAll("(\\n)(\\s*)\\*(\\s*)","\n").trim();
	}

}
