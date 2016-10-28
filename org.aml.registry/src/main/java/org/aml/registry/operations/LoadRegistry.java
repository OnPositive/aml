package org.aml.registry.operations;

import java.io.IOException;
import java.io.StringReader;
import java.util.function.Supplier;

import org.aml.registry.model.Registry;

import com.github.fge.jackson.JacksonUtils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;

public class LoadRegistry implements Supplier<Registry>{

	protected String registryUrl;

	public LoadRegistry(String registryUrl) {
		super();
		this.registryUrl = registryUrl;
	}

	public Registry get() {
		HttpRequest buildGetRequest;
		try {
			buildGetRequest = new NetHttpTransport().createRequestFactory().buildGetRequest(new GenericUrl(registryUrl));
			String str=buildGetRequest.execute().parseAsString();
			Registry readValue = JacksonUtils.getReader().forType(Registry.class)
					.readValue(new StringReader(str));
			return readValue;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}