package org.aml.raml08210.registry.old;

import java.io.IOException;
import java.io.StringReader;
import java.util.function.Supplier;

import com.github.fge.jackson.JacksonUtils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

public class LoadOldRegistry implements Supplier<OldRegistry> {

	public static HttpRequestFactory createRequestFactory= new NetHttpTransport().createRequestFactory();

	public OldRegistry get() {
		HttpRequest buildGetRequest;
		try {
			createRequestFactory = new NetHttpTransport().createRequestFactory();
			buildGetRequest = createRequestFactory.buildGetRequest(new GenericUrl(
					"https://raw.githubusercontent.com/apiregistry/registry/754c41132fcd52a709f4f910aa0d99b0ec2cd890/registry.json"));
			String str = buildGetRequest.execute().parseAsString();
			OldRegistry readValue = JacksonUtils.getReader().forType(OldRegistry.class)
					.readValue(new StringReader(str));
			return readValue;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
