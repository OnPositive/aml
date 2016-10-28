package org.aml.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.aml.apimodel.impl.ApiImpl;
import org.aml.swagger.reader.SwaggerReader;
import org.apigurus.ApiVersion;
import org.apigurus.LoadGurus;
import org.apigurus.Registry;
import org.apigurus.RegistryEntry;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

public class LoadGurusTest {

	public static void main(String[] args) {
		Registry r=new LoadGurus().get();
		System.out.println(r.getRegistry().size());
		ArrayList<RegistryEntry>acceptet=new ArrayList<RegistryEntry>();
		int sc=0;
		for (String e:r.getRegistry().keySet()){
			if (e.startsWith("windows.net")){
				continue;
			}
			if (e.startsWith("azure.com")){
				continue;
			}
			if (e.startsWith("googleapis.com")){
				continue;
			}
			acceptet.add(r.getRegistry().get(e));
			Map<String, ApiVersion> registry = r.getRegistry().get(e).getVersions().getRegistry();
			for (ApiVersion v:registry.values()){
				String swaggerUrl = v.getSwaggerUrl();
				HttpRequest buildGetRequest;
				try {
					HttpRequestFactory createRequestFactory = new NetHttpTransport().createRequestFactory();
					buildGetRequest = createRequestFactory.buildGetRequest(new GenericUrl(swaggerUrl));
					String str=buildGetRequest.execute().parseAsString();
					try{
					ApiImpl rs=new SwaggerReader().read(str);
					if (rs!=null){
						sc+=1;	
					}
					}catch (Exception ex) {
						ex.printStackTrace();
					}
				} catch (IOException ex) {
					throw new IllegalStateException(e);
				}
				
			}
		}
		System.out.println(acceptet.size());
		System.out.println("Success count:"+sc);
	}

}
