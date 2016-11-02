package org.aml.typesystem.jsonschema.reader;

import java.util.HashMap;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.ITypeRegistry;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.TypeRegistryImpl;
import org.aml.typesystem.meta.facets.Default;
import org.aml.typesystem.meta.restrictions.Pattern;
import org.aml.typesystem.meta.restrictions.UniqueItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxItems;
import org.aml.typesystem.meta.restrictions.minmax.MaxLength;
import org.aml.typesystem.meta.restrictions.minmax.MaxProperties;
import org.aml.typesystem.meta.restrictions.minmax.Maximum;
import org.aml.typesystem.meta.restrictions.minmax.MinItems;
import org.aml.typesystem.meta.restrictions.minmax.MinLength;
import org.aml.typesystem.meta.restrictions.minmax.MinProperties;
import org.aml.typesystem.meta.restrictions.minmax.Minimum;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONSchemaParser {

	TypeRegistryImpl result = new TypeRegistryImpl(BuiltIns.getBuiltInTypes());
	IReferenceResolver resolver;

	static HashMap<String, IFacetHandler> handlers = new HashMap<>();

	public static final IFacetHandler MINIMUM = new SimpleFacetHandler(Minimum.class);
	public static final IFacetHandler MAXIMUM = new SimpleFacetHandler(Maximum.class);
	public static final IFacetHandler PATTERN = new SimpleFacetHandler(Pattern.class);
	public static final IFacetHandler MINLENGTH = new SimpleFacetHandler(MinLength.class);
	public static final IFacetHandler MAXLENGTH = new SimpleFacetHandler(MaxLength.class);
	public static final IFacetHandler MINITEMS = new SimpleFacetHandler(MinItems.class);
	public static final IFacetHandler MAXITEMS = new SimpleFacetHandler(MaxItems.class);
	public static final IFacetHandler MINPROPERTIES = new SimpleFacetHandler(MinProperties.class);
	public static final IFacetHandler MAXPROPERTIES = new SimpleFacetHandler(MaxProperties.class);
	public static final IFacetHandler DEFAULT = new SimpleFacetHandler(Default.class);
	public static final IFacetHandler ADDITIONALPROPERTIES = new AdditionalPropertiesHandler();
	public static final IFacetHandler REQUIRED = new SkipFacetHandler("required");
	public static final IFacetHandler $SCHEMA = new SkipFacetHandler("$schema");
	public static final IFacetHandler DEFINITIONS = new SkipFacetHandler("definitions");
	public static final IFacetHandler ENUM_DESCRIPTIONS = new SkipFacetHandler("enumDescriptions");
	public static final IFacetHandler ITEMS = new ItemsHandler();
	public static final IFacetHandler UNIQUE_ITEMS = new SimpleFacetHandler(UniqueItems.class);
	public static final IFacetHandler $REF = new SkipFacetHandler("$ref");
	public static final IFacetHandler ENUM = new EnumHandler();
	public static final IFacetHandler ONEOF = new SkipFacetHandler("oneOf");
	public static final IFacetHandler ANYOF = new SkipFacetHandler("anyOf");
	public static final IFacetHandler ALLOF = new SkipFacetHandler("allOf");
	
	public static final IFacetHandler readOnly = new SkipFacetHandler("readOnly");
	

	static IFacetHandler[] allHandlers = new IFacetHandler[] { new TitleHandler(), new DescriptionHandler(),
			new FormatHandler(), new PropertiesHandler(), new IdHandler(), new TypeHandler(), MINIMUM, MAXIMUM, PATTERN,
			MINLENGTH, MAXLENGTH, MINITEMS, MAXITEMS, REQUIRED, $SCHEMA, DEFINITIONS, UNIQUE_ITEMS, MINPROPERTIES,ONEOF,ALLOF,ANYOF,
			MAXPROPERTIES ,ITEMS,$REF,ADDITIONALPROPERTIES,ENUM,DEFAULT,ENUM_DESCRIPTIONS,new AnnotationsHandler(),new ReadOnlyHandler()};

	static {
		for (IFacetHandler h : allHandlers) {
			handlers.put(h.name(), h);
		}
	}

	public JSONSchemaParser(IReferenceResolver resolver) {
		super();
		this.resolver = resolver;
	}

	public void addType(String s, JSONObject object) {
		buildType(s, object,true);		
	}

	private AbstractType buildType(String typeName, JSONObject object,boolean register) {
		String type = object.has("type") ? object.getString("type") : "any";
		String ref = object.optString("$ref");
		if (ref.length()>0){
			JSONObject resolveReference = this.resolver.resolveReference(ref);
			String tName=null;
			if (ref.lastIndexOf('/')!=-1){
				tName=ref.substring(ref.lastIndexOf('/')+1);
			}
			if (resolveReference.has("id")){
				tName=resolveReference.getString("id");
			}
			if (this.result.hasDeclaration(ref)){
				return this.result.getType(ref);
			}			
			return buildType(tName, resolveReference, tName!=null);			
		}
		if (object.has("format")){
			String f=object.getString("format");
			if (f.equals("date-time")){
				type=BuiltIns.DATETIME.name();
				object.remove("format");
			}
			if (f.equals("int32")){
				type=BuiltIns.INTEGER.name();
				//object.remove("format");
			}
			if (f.equals("uint32")){
				type=BuiltIns.INTEGER.name();
				object.put("format", "int32");
			}
			if (f.equals("uint64")){
				type=BuiltIns.INTEGER.name();
				object.put("format", "int64");
			}
			if (f.equals("int64")){
				type=BuiltIns.INTEGER.name();
				//object.remove("format");
			}
		}
		if (type.equals("string")){
			object.remove("format");
		}
		AbstractType baseType = getBaseType(type);
		
		AbstractType derivedType=null;
		if (object.has("oneOf")){
			JSONArray arr=object.getJSONArray("oneOf");
			AbstractType[] options=toTypes(arr);
			derivedType=TypeOps.union(typeName,options);
		}
		else if (object.has("anyOf")){
			JSONArray arr=object.getJSONArray("anyOf");
			AbstractType[] options=toTypes(arr);
			derivedType=TypeOps.union(typeName,options);
		}
		else if (object.has("allOf")){
			JSONArray arr=object.getJSONArray("allOf");
			AbstractType[] options=toTypes(arr);			
			derivedType=TypeOps.derive(typeName,options);
		}
		else{
			derivedType = TypeOps.derive(typeName, baseType);
		}
		if (!derivedType.isAnonimous()){
			result.registerType(derivedType);
		}
		for (String tp : JSONObject.getNames(object)) {
			derivedType = handle(tp, object.get(tp), derivedType, object);
		}
		return derivedType;
	}

	private AbstractType[] toTypes(JSONArray arr) {
		AbstractType[] result=new AbstractType[arr.length()];
		for (int i=0;i<result.length;i++){
			result[i]=getType(arr.getJSONObject(i));
		}
		return result;
	}

	private AbstractType handle(String tp, Object object, AbstractType derivedType, JSONObject base) {
		IFacetHandler iFacetHandler = handlers.get(tp);
		if (iFacetHandler != null) {
			return iFacetHandler.handle(tp, object, derivedType, base, this);
		} else {
			throw new IllegalStateException("Unsupported facet:" + tp);
		}
	}

	private AbstractType getBaseType(String type) {
		return BuiltIns.getBuiltInTypes().getType(type);
	}

	public ITypeRegistry getResult() {
		return result;
	}

	public AbstractType getType(JSONObject propertyType) {
		return buildType("", propertyType,false);
	}
}
