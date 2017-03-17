package org.aml.examples;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;

public class App extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		return Collections.singleton(Resource.class);
	}
}
