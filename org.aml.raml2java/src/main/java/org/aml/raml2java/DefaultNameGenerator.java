package org.aml.raml2java;

import java.util.HashSet;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.IAnnotation;
import org.aml.typesystem.meta.TypeInformation;
import org.aml.typesystem.meta.facets.Annotation;

public class DefaultNameGenerator implements INameGenerator{

	private static final String AML_NAMESPACE = "org.aml.java.mapping";
	protected String defaultPackageName;
	protected HashSet<String>used=new HashSet<>();
	
	static String[] keywords=new String[]{"package","class","interface","private","public","protected","volatile","for","while","do","break","continue","if","synhronized","trasient","implements","extends","enum","goto","static"};
	
	public DefaultNameGenerator(String string) {
		this.defaultPackageName=string;
		
	}


	@Override
	public String fullyQualifiedName(AbstractType t) {
		String name = t.name();
		name=JavaWriter.escape(name);
		for (String s:keywords){
			if (name.equals(s)){
				name=Character.toUpperCase(name.charAt(0))+name.substring(1);
			}
		}
		String defaultPackageName2 = defaultPackageName;
		if (t.getSource()!=null){
			for (IAnnotation a:t.getSource().annotations()){
				if (a.annotationType().name().equals("package")){
					if (a.annotationType().getNameSpaceId().equals(AML_NAMESPACE)){
					defaultPackageName2=""+a.value();					
					}
				}
			}
		}
		String string = defaultPackageName2+"."+name;
		
		for (TypeInformation x:t.meta()){
			if (x instanceof Annotation){
				Annotation a=(Annotation) x;
				if (a.annotationType().name().equals("package")){
					if (a.annotationType().getNameSpaceId().equals(AML_NAMESPACE)){
						string=""+a.value()+"."+name;
					}
				}
			}
		};
		
		if (!used.add(string)){
			for (int i=2;i<Integer.MAX_VALUE;i++){
				string = defaultPackageName2+"."+name+i;
				if (used.add(string)){
					return string;
				}
			}
		}
		return string;
	}
}
