package org.aml.registry.operations;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.function.Consumer;

import org.aml.registry.internal.CommitterInfo;
import org.aml.registry.internal.PublishHelper;
import org.aml.registry.internal.ZipUtil;
import org.aml.registry.model.ItemDescription;
import org.aml.registry.model.Registry;
import org.aml.registry.usages.IndexBuilder;
import org.eclipse.egit.github.core.RepositoryId;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class PublishRegistry implements Consumer<Registry>{

	private CommitterInfo info;
	private RepositoryId id;

	public PublishRegistry(CommitterInfo info,RepositoryId id) {
		this.info=info;
		this.id=id;
	}
	
	public void accept(Registry t) {
		new PublishHelper(info,"Updating registry",id).commitFileFromString("", "registry-resolved.json", new StoreRegistry().apply(t));
		String property = System.getProperty("user.home");
		File fs=new File(property,".aml_registry");
		RegistryMaterialize registryMaterialize = new RegistryMaterialize(fs.getAbsolutePath(),false);
		Registry apply = registryMaterialize.apply(t);
		IndexBuilder indexBuilder = new IndexBuilder(registryMaterialize.getLocalRegistry());
		String format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()).replace(' ', '_').replace(':','-');
		File file = new File(fs.getParent(),"registry"+format+".zip");
		String absolutePath = file.getAbsolutePath();
		ZipUtil.zipDir(absolutePath, fs.getAbsolutePath());
		
		new PublishHelper(info,"Publishing zipped registry",id).commitFile("releases/", file.getParent(),  file.getName());
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
		String buildIndex = w.toString();
		new PublishHelper(info,"Updating usages",id).commitFileFromString("", "registry-usages.json",  buildIndex);		
	}
}
