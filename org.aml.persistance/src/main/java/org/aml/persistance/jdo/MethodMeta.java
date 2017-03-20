package org.aml.persistance.jdo;

import java.util.ArrayList;
import java.util.Map;
import com.google.gson.Gson;

public class MethodMeta extends BaseMeta{

	
	protected ArrayList<ArgumentMeta>list=new ArrayList<>(); 
		
	
	public MethodMeta(String meta) {
		Object[] fromJson = new Gson().fromJson(meta, Object[].class);		
		this.init((Map<String,Object>)fromJson[0]);
		for (int i=1;i<fromJson.length;i++){
			ArgumentMeta m=new ArgumentMeta();
			m.init((Map<String,Object>)fromJson[i]);
			this.list.add(m);
		}
	}


	public int findParameterNum(String offset) {
		for (int i=0;i<list.size();i++){
			if (list.get(i).name.equals(offset)){
				return i;
			}
		}
		return -1;
	}
}
