package org.aml.registry.internal;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

public class HTTPUtil {

	static HttpRequestFactory createRequestFactory = new NetHttpTransport().createRequestFactory();
	
	static Executor exect=Executors.newFixedThreadPool(100);
	static{
		
	}
	
	public static String readString(String url) throws IOException {
		HttpRequest buildGetRequest;	
		buildGetRequest = createRequestFactory.buildGetRequest(new GenericUrl(url));
		String str=buildGetRequest.execute().parseAsString();
		return str;
	}
	
	public static Future<String> readStringAsync(String url) throws IOException {
		HttpRequest buildGetRequest;	
		buildGetRequest = createRequestFactory.buildGetRequest(new GenericUrl(url));
		buildGetRequest.setNumberOfRetries(5);
		final Future<HttpResponse>rs=buildGetRequest.executeAsync(exect);
		Future<String>ts=new Future<String>() {

			public boolean cancel(boolean mayInterruptIfRunning) {
				return rs.cancel(mayInterruptIfRunning);
			}

			public boolean isCancelled() {
				return rs.isCancelled();
			}

			public boolean isDone() {
				return rs.isDone();
			}

			public String get() throws InterruptedException, ExecutionException {
				try {
					return rs.get().parseAsString();
				} catch (IOException e) {
					throw new IllegalStateException();
				}
			}

			public String get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				try {
					return rs.get(timeout, unit).parseAsString();
				} catch (IOException e) {
					throw new IllegalStateException();
				}
			}
		};
		return ts;
	}
}
