package aml.server;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RestApp extends Application{
//scp -r ./target/org.aml.server.war jetty@176.9.34.20:/opt/jetty/webapps
	public RestApp() {
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		return Collections.singleton(Resource.class);
	}
}
