package org.aml.registry.usages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.aml.registry.internal.LocalRegistry;
import org.aml.registry.model.ItemDescription;
import org.aml.registry.model.Registry;
import org.aml.registry.operations.RegistryMaterialize;
import org.aml.typesystem.ramlreader.TopLevelRamlImpl;
import org.aml.typesystem.ramlreader.TopLevelRamlModelBuilder;
import org.apache.commons.io.FileUtils;
import org.raml.v2.api.loader.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class IndexBuilder {

	protected LocalRegistry registry;
	protected UsageIndex index = new UsageIndex();

	public UsageIndex getIndex() {
		return index;
	}

	public IndexBuilder(LocalRegistry registry) {
		super();
		this.registry = registry;
	}

	public void append(String url){
		String readFileToString=null;
		try {
			readFileToString = FileUtils.readFileToString(registry.getLocalFileFor(url));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		ResourceLoader resourceLoader = new ResourceLoader() {
			
			public InputStream fetchResource(String resourceName) {
				return registry.get(resourceName);
			}
		};
		TopLevelRamlModelBuilder builder=new TopLevelRamlModelBuilder();
		TopLevelRamlImpl build = builder.build(readFileToString,resourceLoader, url);
		UsageCollector usageCollector = new UsageCollector();
		usageCollector.visit(build);
		LinkedHashMap<String, ArrayList<String>> results = usageCollector.getResults();
		index.append(url,results);
		
	}
	public static String buildIndex(Registry t) {
		String property = System.getProperty("user.home");
		File fs=new File(property,".aml_registry");
		return buildIndex(t,fs.getAbsolutePath());
	}
	public static String buildIndex(Registry t, String localRegistry) {
		
		RegistryMaterialize registryMaterialize = new RegistryMaterialize(localRegistry,true);
		Registry apply = registryMaterialize.apply(t);
		IndexBuilder indexBuilder = new IndexBuilder(registryMaterialize.getLocalRegistry());
		for (ItemDescription d : apply.items()) {
			indexBuilder.append(d.getLocation());
		}
		StringWriter w = new StringWriter();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			objectMapper.writeValue(w, indexBuilder.getIndex());			
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		String string = w.toString();
		return string;
	}
}
