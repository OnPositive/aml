package org.aml.typesystem.yamlwriter;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.beans.IProperty;
import org.aml.typesystem.beans.IPropertyView;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.XMLFacet;
import org.aml.typesystem.meta.restrictions.ComponentShouldBeOfType;
import org.aml.typesystem.meta.restrictions.HasPropertyRestriction;
import org.aml.typesystem.meta.restrictions.PropertyIs;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;

public class RamlWriter {

	private static final String NOVALUE = "<<NOVALUE!!!";
	private static final String ANNOTATION_TYPES = "annotationTypes";
	private static final String RAML_1_0_LIBRARY = "#%RAML 1.0 Library";
	private static final String ITEMS = "items";
	private static final String PROPERTIES = "properties";
	private static final String TYPES = "types";
	private static final String TYPE = "type";

	public String store(AbstractType deriveObjectType) {
		
		return null;
	}
	
	LinkedHashMap<String, Object>dumpType(AbstractType t){
		LinkedHashMap<String, Object>result=new LinkedHashMap<>();
		Set<AbstractType> superTypes = t.superTypes();
		if(superTypes.size()>0){
			if (superTypes.size()==1){
				String name = superTypes.iterator().next().name();
				if (t.isNullable()){
					name=name+" | nil";
				}
				result.put(TYPE,name);
			}
			else{
				ArrayList<String>types=new ArrayList<>();
				for (AbstractType ts:superTypes){
					String name = ts.name();
					if (t.isNullable()){
						name=name+" | nil";
					}
					types.add(name);
				}
				result.put(TYPE,types);
			}
		}
		if(t.isSubTypeOf(BuiltIns.OBJECT)){
			IPropertyView propertiesView = t.toPropertiesView();
			LinkedHashMap<String, Object>dumpedProps=new LinkedHashMap<>();
			for (IProperty p:propertiesView.properties()){
				Object vl=null;
				vl = typeRespresentation(p.range());
				String id = p.id();
				if (!p.isRequired()){
					id=id+"?";
				}
				dumpedProps.put(id, vl);
			}
			if (!dumpedProps.isEmpty()){
				result.put(PROPERTIES, dumpedProps);
			}
		}
		Set<TypeInformation> meta = t.declaredMeta();
		for (TypeInformation ti:meta){
			if (ti instanceof PropertyIs){
				continue;
			}
			if (ti instanceof HasPropertyRestriction){
				continue;
			}
			if (ti instanceof ComponentShouldBeOfType){
				ComponentShouldBeOfType cs=(ComponentShouldBeOfType) ti;
				result.put(ITEMS, typeRespresentation(cs.range()));
			}
			
			if (ti instanceof ISimpleFacet){
				ISimpleFacet fs=(ISimpleFacet) ti;
				Object value = fs.value();
				if (value instanceof Map){
					Map<?,?> mm=(Map<?,?>) value;
					if (mm.isEmpty()){
						value=NOVALUE;
					}
				}
				result.put(fs.facetName(), value);
			}
			else{
				if (ti instanceof XMLFacet){					
					result.put("xml",toMap(ti));
				}
			}
		}
		return result;		
	}
	
	LinkedHashMap<String, Object> toMap(Object obj){
		LinkedHashMap<String, Object>result=new LinkedHashMap<>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field f:declaredFields){
			f.setAccessible(true);
			try {
				Object object = f.get(obj);
				if (object!=null&&!object.equals(Boolean.FALSE)){
					result.put(f.getName(), object);
				}
			} catch (Exception e) {
				throw new IllegalStateException();
			}
		}
		return result;
	}

	private Object typeRespresentation(AbstractType p) {
		Object vl;
		if (p.isAnonimous()){
			if (p.isArray()){
				if (p.declaredMeta().size()==1){
					ComponentShouldBeOfType oneMeta = p.oneMeta(ComponentShouldBeOfType.class);
					if (oneMeta!=null){
						Object typeRespresentation = typeRespresentation(oneMeta.range());
						if (typeRespresentation instanceof String){
							return typeRespresentation.toString()+"[]";
						}
					}
				}
			}
			HashMap<String, Object> dumpType = dumpType(p);
			vl=dumpType;
		}
		else{
			String name = p.name();
			vl=name;
			if (p.isNullable()){
				return vl+" | nil";
			}
		}
		return vl;
	}

	public String store(ITypeRegistry registry,ITypeRegistry ar) {
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
		Yaml rl = new Yaml(dumperOptions);
		LinkedHashMap<String, Object> toStore = new LinkedHashMap<>();
		Collection<AbstractType> types2 = registry.types();
		LinkedHashMap<String, Object> atps = fillRromList(ar.types());
		if (!atps.isEmpty()){
			toStore.put(ANNOTATION_TYPES, atps);
		}
		types2 = registry.types();		
		LinkedHashMap<String, Object> tps = fillRromList(types2);
		toStore.put(TYPES, tps);
		StringWriter stringWriter = new StringWriter();
		BufferedWriter ws=new BufferedWriter(stringWriter);
		try{
		ws.write(RAML_1_0_LIBRARY);
		ws.newLine();
		rl.dump(toStore, ws);
		return stringWriter.toString().replaceAll(NOVALUE, "");
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	private LinkedHashMap<String, Object> fillRromList(Collection<AbstractType> types2) {
		LinkedHashMap<String,Object> tps = new LinkedHashMap<>();		
		ArrayList<AbstractType>ts=new ArrayList<>(types2);
		Collections.sort(ts,new Comparator<AbstractType>() {

			@Override
			public int compare(AbstractType o1, AbstractType o2) {
				int s1=0;
				int s2=0;
				if (o1.isObject()){
					s1=1000;
				}
				if (o2.isObject()){
					s2=1000;
				}
				if (s1==s2){
					return o1.name().compareTo(o2.name());
				}
				return s1-s2;
			}
		});
		for (AbstractType t : ts) {
			tps.put(t.name(),dumpType(t));
		}
		return tps;
	}
}