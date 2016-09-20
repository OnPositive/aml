package org.aml.raml2java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.beans.ISimpleFacet;
import org.aml.typesystem.java.AnnotationsProcessingConfig;
import org.aml.typesystem.java.AnnotationsProcessingConfig.AnnotationConfig;
import org.aml.typesystem.java.AnnotationsProcessingConfig.MemberMapping;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;
import org.aml.typesystem.meta.facets.CustomFacet;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationUse;

public class AnnotationsProcessingInverseConfig implements IClassCustomizer{

	public static class AnnotationGenerationInfo {

		protected String sourceFacetType;

		protected String sourceFacet;

		protected String annotationClassName;

		protected Object value;

		protected String annotationMemberName;
	}

	protected HashMap<String, AnnotationGenerationInfo> configs = new HashMap<>();

	protected JavaWriter writer;
	
	public AnnotationsProcessingInverseConfig(){
		AnnotationsProcessingConfig annotationsProcessingConfig = new AnnotationsProcessingConfig();
		annotationsProcessingConfig.append(AnnotationsProcessingConfig.class.getResourceAsStream("/javax.validation.xml"));
		appendConfig(annotationsProcessingConfig);
	}
	

	public void appendConfig(AnnotationsProcessingConfig annotationsProcessingConfig) {
		for (AnnotationConfig v:annotationsProcessingConfig.getConfiguration()){
			for (MemberMapping m:v.members){
				String target = m.type+"."+m.target;
				AnnotationGenerationInfo i=new AnnotationGenerationInfo();
				i.annotationClassName=v.name;
				i.annotationMemberName=m.name;
				i.value=m.value;
				configs.put(target, i);				
			}
		}
	}


	void processFacets(AbstractType t, PropertyCustomizerParameters cp) {
		HashSet<String> names = new HashSet<>();
		for (AbstractType q : t.allSuperTypes()) {
			names.add(q.name());
		}
		Set<TypeInformation> meta = t.meta();
		ArrayList<TypeInformation> arrayList = new ArrayList<>(meta);
		if (cp.prop.isRequired()){
			meta.add(new CustomFacet("required", true));
		}
		for (TypeInformation i : arrayList) {
			if (i instanceof ISimpleFacet) {
				ISimpleFacet si = (ISimpleFacet) i;
				if (si instanceof Annotation) {

				} else {
					String facetName = si.facetName();
					for (String m : names) {
						String fullName = m + "." + facetName;
						AnnotationGenerationInfo annotationGenerationInfo = configs.get(fullName);
						if (annotationGenerationInfo != null) {
							processConfig(annotationGenerationInfo, si, cp);
						}
					}
				}
			}
		}
		
	}

	protected void processConfig(AnnotationGenerationInfo annotationGenerationInfo, ISimpleFacet si,
			PropertyCustomizerParameters cp) {
		JAnnotatable annotable = cp.getter;
		JAnnotationUse use = null;
		for (JAnnotationUse u : annotable.annotations()) {
			if (u.getAnnotationClass().fullName().equals(annotationGenerationInfo.annotationClassName)) {
				use = u;
				break;
			}
		}
		if (use == null) {
			use = annotable.annotate(writer.getModel().ref(annotationGenerationInfo.annotationClassName));
		}
		writer.addParam(use, si.value(), annotationGenerationInfo.annotationMemberName);
	}

	@Override
	public void customize(ClassCustomizerParameters parameters) {
		this.writer=parameters.writer;
		for (PropertyCustomizerParameters a:parameters.props){
			processFacets(a.prop.range(), a);
		}
	}
}