package org.aml.registry.operations;

import java.util.function.Consumer;

import org.aml.registry.internal.CommitterInfo;
import org.aml.registry.internal.PublishHelper;
import org.aml.registry.model.Registry;
import org.aml.registry.usages.IndexBuilder;
import org.eclipse.egit.github.core.RepositoryId;

public class PublishRegistry implements Consumer<Registry>{

	private CommitterInfo info;
	private RepositoryId id;

	public PublishRegistry(CommitterInfo info,RepositoryId id) {
		this.info=info;
		this.id=id;
	}
	
	public void accept(Registry t) {
		new PublishHelper(info,"Updating registry",id).commitFileFromString("", "registry-resolved.json", new StoreRegistry().apply(t));		
		String buildIndex = IndexBuilder.buildIndex(t);
		new PublishHelper(info,"Updating usages",id).commitFileFromString("", "registry-usages.json",  buildIndex);
	}
}
