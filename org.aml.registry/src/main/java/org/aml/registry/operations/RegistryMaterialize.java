package org.aml.registry.operations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.aml.registry.internal.HTTPUtil;
import org.aml.registry.internal.LocalRegistry;
import org.aml.registry.model.ApiDescription;
import org.aml.registry.model.ItemDescription;
import org.aml.registry.model.LibraryDescription;
import org.aml.registry.model.Registry;

public class RegistryMaterialize implements Function<Registry,Registry>{
	
	private LocalRegistry localRegistry;
	private boolean clean;
	
	static class ParseInfo{
		public ParseInfo(ItemDescription d2, Future<String> readStringAsync) {
			this.d=d2;
			this.location=readStringAsync;
		}
		ItemDescription d;
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

	public RegistryMaterialize(String file, boolean clean){
		localRegistry=new LocalRegistry(file);
		this.clean=clean;
	}
	public RegistryMaterialize(LocalRegistry reg){
		localRegistry=reg;
	}
	
	public Registry apply(Registry t) {
		long t0=System.currentTimeMillis();
		ArrayList<ParseInfo>pi=new ArrayList<RegistryMaterialize.ParseInfo>();
		for (ApiDescription d:t.getApis()){
			process(pi, d);
		}
		for (LibraryDescription d:t.getLibraries()){
			process(pi, d);
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
		Registry rs=new Registry();
		for (ApiDescription d:t.getApis()){
			ApiDescription clone = (ApiDescription) d.clone();
			clone.setLocation(localRegistry.getLocalFileFor(clone.getLocation()).getAbsolutePath());
			rs.getApis().add(clone);
		}
		for (LibraryDescription d:t.getLibraries()){
			LibraryDescription clone = (LibraryDescription) d.clone();
			clone.setLocation(localRegistry.getLocalFileFor(clone.getLocation()).getAbsolutePath());
			rs.getLibraries().add(clone);
		}
		return t;
	}
	public void process(ArrayList<ParseInfo> pi, ItemDescription d) {
		String locaiton=d.getLocation();
		try {
			File localFileFor = localRegistry.getLocalFileFor(locaiton);
			if (!localFileFor.exists()||clean){
				Future<String> readStringAsync = HTTPUtil.readStringAsync(locaiton);
				ParseInfo info=new ParseInfo(d,readStringAsync);
				pi.add(info);		
			}				
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	public LocalRegistry getLocalRegistry() {
		return localRegistry;
	}
}
