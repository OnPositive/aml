package org.aml.java2raml;

import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.java.JavaTypeBuilder;
import org.aml.typesystem.reflection.ReflectionType;
import org.aml.typesystem.yamlwriter.RamlWriter;

public class Java2Raml {

	private JavaTypeBuilder builder=new JavaTypeBuilder();
	private RamlWriter writer=new RamlWriter();

	
	public void add(ITypeModel model){
		builder.getType(model);
	}
	public void add(Class<?> model){
		builder.getType(new ReflectionType(model));
	}
	
	
	public String flush(){
		return writer.store(builder.getRegistry());		
	}
}
