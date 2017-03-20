package org.aml.persistance.jdo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseMeta {

	ArrayList<Object>meta=new ArrayList<>();
	public String name;
	static HashMap<String,Class<? extends CanInit>>initMap=new HashMap<>();
	
	static{
		initMap.put("basicPaging", PagingInfo.class);
		initMap.put("reference", ReferenceInfo.class);
	}
	
	public <T> T meta(Class<T>target){
		for (Object z:meta){
			if (target.isInstance(z)){
				return target.cast(z);
			}
		}
		return null;
	}
	
	void init(Map<String,Object>map){
		this.name=(String) map.get("name");
		for (String s:map.keySet()){
			Class<? extends CanInit>cl=initMap.get(s);
			if (cl!=null){
				try {
					CanInit mm=cl.newInstance();
					mm.init(map.get(s));
					this.meta.add(mm);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}
}
