package org.aml.examples;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;
import org.raml.issues.resource.ProjectsResourceImpl;

public class App extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		return Collections.singleton(ProjectsResourceImpl.class);
	}
}
