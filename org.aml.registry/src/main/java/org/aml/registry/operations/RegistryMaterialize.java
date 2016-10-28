package org.aml.registry.operations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import org.aml.registry.internal.HTTPUtil;
import org.aml.registry.internal.LocalRegistry;
import org.aml.registry.model.ApiDescription;
import org.aml.registry.model.Registry;

public class RegistryMaterialize implements Consumer<Registry>{
	
	private LocalRegistry localRegistry;
	
	static class ParseInfo{
		public ParseInfo(ApiDescription d2, Future<String> readStringAsync) {
			this.d=d2;
			this.location=readStringAsync;
		}
		ApiDescription d;
		Future<String>location;
		
		public void materialize(LocalRegistry localRegistry) throws IOException {
			try {
				String str=location.get();
				File localFileFor = localRegistry.getLocalFileFor(d.getLocation());
				localFileFor.getParentFile().mkdirs();
				Files.write(localFileFor.toPath(), Collections.singletonList(str));
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} catch (ExecutionException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public RegistryMaterialize(String file){
		localRegistry=new LocalRegistry(file);
	}
	public RegistryMaterialize(LocalRegistry reg){
		localRegistry=reg;
	}
	
	public void accept(Registry t) {
		long t0=System.currentTimeMillis();
		ArrayList<ParseInfo>pi=new ArrayList<RegistryMaterialize.ParseInfo>();
		for (ApiDescription d:t.getApis()){
			String locaiton=d.getLocation();
			try {
				Future<String> readStringAsync = HTTPUtil.readStringAsync(locaiton);
				ParseInfo info=new ParseInfo(d,readStringAsync);
				pi.add(info);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (ParseInfo p:pi){
			try {
				p.materialize(localRegistry);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		long t1=System.currentTimeMillis();
		System.out.println("Items resolved:"+(t.getApis().size()+t.getLibraries().size()));
		System.out.println("Total time:"+(t1-t0)+"ms");
	}
}
