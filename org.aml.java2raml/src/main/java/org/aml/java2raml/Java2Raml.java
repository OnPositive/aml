package org.aml.java2raml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.aml.typesystem.ITypeModel;
import org.aml.typesystem.java.AcceptAllAnnotations;
import org.aml.typesystem.java.AcceptAllAnnotationsFromPackages;
import org.aml.typesystem.java.AllRequired;
import org.aml.typesystem.java.BeanPropertiesFilter;
import org.aml.typesystem.java.FieldMemberFilter;
import org.aml.typesystem.java.JavaTypeBuilder;
import org.aml.typesystem.java.NoneFilter;
import org.aml.typesystem.java.TypeBuilderConfig;
import org.aml.typesystem.reflection.ReflectionType;
import org.aml.typesystem.yamlwriter.RamlWriter;

public class Java2Raml {

	private JavaTypeBuilder builder = new JavaTypeBuilder();
	private RamlWriter writer = new RamlWriter();

	public void add(ITypeModel model) {
		builder.getType(model);
	}

	public void add(Class<?> model) {
		builder.getType(new ReflectionType(model));
	}

	public String flush() {
		return writer.store(builder.getRegistry(), builder.getAnnotationTypeRegistry());
	}

	public TypeBuilderConfig getTypeBuilderConfig() {
		return builder.getConfig();
	}

	public String processConfigToString(ClassLoader classLoader, Config cfg) {
		Collection<ITypeModel> gather = new ClassLoaderBasedCollector(classLoader).gather(cfg);

		switch (cfg.memberMode) {
		case FIELDS:
			builder.getConfig().setMemberFilter(new FieldMemberFilter());
			break;
		case PROPERTIES:
			builder.getConfig().setMemberFilter(new BeanPropertiesFilter());
		case NONE:
			builder.getConfig().setMemberFilter(new NoneFilter());
		}
		switch (cfg.optinalityMode) {
		case EVERYTHING_IS_REQUIRED:
			builder.getConfig().setCheckNullable(new AllRequired());
			break;
		case PRIMITIVES_ARE_REQUIRED:
			builder.getConfig().setCheckNullable(new AllObjectsAreOptional());
		case OBJECTS_ARE_NULLABLE:
			builder.getConfig().setCheckNullable(new AllObjectsAreNullable());
		}
		switch (cfg.annotationsBehavior) {
		case GENERATE_ALL:
			builder.getConfig().setAnnotationsFilter(new AcceptAllAnnotations());
			break;
		case IGNORE_ALL_EXCEPT_EXPLICIT_PACKAGES:
			builder.getConfig().setAnnotationsFilter(new AcceptAllAnnotationsFromPackages(cfg.annotationPackages));
		default:
			break;
		}
		if (cfg.ignoreDefaultProfiles){
			builder.getConfig().getAnnotationsProcessingConfig().clear();
		}
		for (String s:cfg.annotationProfiles){
			File f=new File(s);
			if (f.isAbsolute()){
				try {
					FileInputStream str = new FileInputStream(f);
					builder.getConfig().getAnnotationsProcessingConfig().append(str);
					str.close();
				} catch (FileNotFoundException e) {
					cfg.log.log(e.getMessage());
				} catch (IOException e) {
					cfg.log.log(e.getMessage());
					
				}
			}
			else{
				builder.getClass().getClassLoader().getResourceAsStream("/"+s+".xml");
			}
		}
		for (ITypeModel m : gather) {
			add(m);
		}
		return flush();
	}
}
