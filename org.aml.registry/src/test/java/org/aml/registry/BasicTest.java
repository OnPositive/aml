package org.aml.registry;

import org.aml.registry.internal.CommitterInfo;
import org.aml.registry.model.Registry;
import org.aml.registry.operations.BuildStat;
import org.aml.registry.operations.LoadRegistry;
import org.aml.registry.operations.PublishRegistry;
import org.aml.registry.operations.ResolveRegistry;
import org.eclipse.egit.github.core.RepositoryId;
import org.junit.Test;

public class BasicTest {

	@Test
	public void test() {
		Registry apply = new ResolveRegistry().apply(
				new LoadRegistry("https://raw.githubusercontent.com/apiregistry/registry/master/registry.json")
						.get());
		new PublishRegistry(
				new CommitterInfo("petrochenko-pavel-a", "life150482", "pavel@onpositive.com", "Petrochenko Pavel"),
				new RepositoryId("apiregistry", "registry")).accept(apply);
		System.out.println(new BuildStat().apply(apply));
	}

}
