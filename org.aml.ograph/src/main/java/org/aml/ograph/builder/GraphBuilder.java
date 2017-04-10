package org.aml.ograph.builder;

import java.io.InputStream;
import java.util.HashSet;

import org.aml.apimodel.Action;
import org.aml.apimodel.Api;
import org.aml.graphmodel.IAPIModule;
import org.aml.typesystem.AbstractType;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.raml.v2.api.loader.DefaultResourceLoader;

public class GraphBuilder {

	public static IAPIModule build(String string) {
		InputStream resourceAsStream = GraphBuilder.class.getResourceAsStream(string);
		Api build = (Api) new TopLevelRamlModelBuilder().build(resourceAsStream, new DefaultResourceLoader(),
				GraphBuilder.class.getResource(string).toExternalForm());
		build.allMethods().forEach(x -> {
			AbstractType tp = getResultType(x);
			
			
			x.parameters().forEach(p->{
				AbstractType typeModel = p.getTypeModel();
				System.out.println(typeModel.name());
					
			});
			
		});
		return null;
	}

	public static AbstractType getResultType(Action x) {
		HashSet<AbstractType> tp = new HashSet<>();
		x.responses().forEach(r -> {
			if (r.code().charAt(0) == '2') {
				r.body().forEach(b -> {
					AbstractType t = b.getTypeModel();
					if (t.isEffectivelyEmptyType()) {
						t = t.superType();
					}
					if (!t.name().isEmpty()) {
						// it looks like a resource representation in some form
						tp.add(t);
					}
				});
			}
		});
		if (tp.size() == 1) {
			return tp.iterator().next();
		}
		return null;
	}

}
