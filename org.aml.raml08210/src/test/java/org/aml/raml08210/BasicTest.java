package org.aml.raml08210;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.aml.raml08210.registry.old.LoadOldRegistry;
import org.aml.raml08210.registry.old.OldApi;
import org.aml.raml08210.registry.old.OldRegistry;
import org.aml.raml08210.registry.old.RepoRegistry;
import org.aml.registry.model.Registry;
import org.aml.registry.operations.IconEnchant;
import org.apache.commons.io.FileUtils;

import com.google.api.client.http.GenericUrl;

import junit.framework.TestCase;

public class BasicTest extends TestCase {
	
	

	public void test0() {
		RepoRegistry rs=new RepoRegistry();
		OldRegistry oldRegistry = new LoadOldRegistry().get();
		for (Map<String,OldApi>a:oldRegistry.getApis()){
			a.values().forEach(x->{rs.append(x);});
		}
		Registry load = rs.toNewRegistry();
		load.items().forEach(x->{
			String location=x.getLocation();
			location=location.substring("https://github.com/raml-apis/".length());
			x.setIcon(null);
			location="https://raw.githubusercontent.com/raml-apis/"+x.getOrg()+"/"+location;
			try {
				LoadOldRegistry.createRequestFactory.buildGetRequest(new GenericUrl(location)).execute().parseAsString();
			} catch (IOException e) {
				throw new IllegalStateException();
			}
			x.setLocation(location);			
		});
		String str=new IconEnchant().apply(load).asString();
		try {
			System.out.println(str);
			FileUtils.writeStringToFile(new File("C:/work/aml/org.aml.raml08210/src/test/resources/old.json"), str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
