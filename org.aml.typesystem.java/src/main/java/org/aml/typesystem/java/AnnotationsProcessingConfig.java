package org.aml.typesystem.java;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.aml.typesystem.IAnnotationModel;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.java.JavaTypeBuilder.AnnotationsConfigInput;
import org.aml.typesystem.meta.FacetRegistry;
import org.aml.typesystem.meta.TypeInformation;

public class AnnotationsProcessingConfig {

	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String NULLABLE = "nullable";
	private static final String REQUIRED = "required";

	public static class MemberMapping {
		@XmlAttribute
		String name;
		@XmlAttribute
		String target;
		@XmlAttribute
		String value;
	}

	public static class AnnotationConfig {
		@XmlAttribute
		String name;
		@XmlElement(name = "member")
		ArrayList<MemberMapping> members = new ArrayList<>();

		@XmlAttribute
		boolean skipFromRaml;
	}

	public static class AnnotationsConfig {

		@XmlElement(name = "annotation")
		ArrayList<AnnotationConfig> config;
	}

	protected HashMap<String, AnnotationConfig> config = new HashMap<>();

	public void append(InputStream str) {
		AnnotationsConfig unmarshal = JAXB.unmarshal(str, AnnotationsConfig.class);
		for (AnnotationConfig c : unmarshal.config) {
			config.put(c.name, c);
		}
	}

	public boolean process(AnnotationsConfigInput cfg, IAnnotationModel annotation) {
		if (config.containsKey(annotation.getCanonicalName())) {
			AnnotationConfig annotationConfig = config.get(annotation.getCanonicalName());
			for (MemberMapping m : annotationConfig.members) {
				String facetName = m.target;
				String facetAttr = null;
				int di = facetName.indexOf('.');
				if (di != -1) {
					facetAttr = facetName.substring(di+1);
					facetName = facetName.substring(0, di);
				}

				TypeInformation facet = FacetRegistry.facet(facetName);
				Object value = null;
				if (m.name != null) {
					value = annotation.getValue(m.name);
				} else {
					if (m.value != null) {
						value = m.value;
						if (value.equals(TRUE)) {
							value = true;
						}
						if (value.equals(FALSE)) {
							value = false;
						}
					}
				}
				if (facetName.equals(REQUIRED)){
					
					cfg.required=(boolean) Boolean.valueOf(""+value);
				}
				if (facetName.equals(NULLABLE)){
				
					cfg.nullable=(boolean) Boolean.valueOf(""+value);
				}
				if (value==null||value.equals(false)||value.equals("")){
					continue;
				}
				
				if (facet != null) {
					if (cfg.type.isBuiltIn()||cfg.isProperty){
						if (!cfg.type.isAnonimous()){
						cfg.type=TypeOps.derive("", cfg.type);
						}
					}
					if (cfg.type.hasDirectMeta(facet.getClass())) {
						facet = cfg.type.oneMeta(facet.getClass());
					} else {
						cfg.type.addMeta(facet);
					}
					if (facetAttr != null) {
						try {
							Field declaredField = facet.getClass().getDeclaredField(facetAttr);
							declaredField.setAccessible(true);
							declaredField.set(facet, value);
						} catch (Exception e) {
							throw new IllegalStateException();
						}
					}
					else{
						if (facet instanceof ISimpleFacet){
							ISimpleFacet f=(ISimpleFacet) facet;
							f.setValue(value);
						}
					}
				}
			}
			if (annotationConfig.skipFromRaml) {
				return true;
			}
		}
		return false;
	}

	public void clear() {
		this.config.clear();
	}
}
