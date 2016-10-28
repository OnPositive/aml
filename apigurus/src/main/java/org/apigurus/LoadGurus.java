package org.apigurus;

import java.io.IOException;
import java.io.StringReader;
import java.util.function.Supplier;


import com.github.fge.jackson.JacksonUtils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;

public class LoadGurus implements Supplier<Registry>{

	public Registry get() {
		HttpRequest buildGetRequest;
		try {
			buildGetRequest = new NetHttpTransport().createRequestFactory().buildGetRequest(new GenericUrl("https://api.apis.guru/v2/list.json"));
			String str=buildGetRequest.execute().parseAsString();
			Registry readValue = JacksonUtils.getReader().forType(Registry.class)
					.readValue(new StringReader(str));
			return readValue;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
