package org.aml.registry;

import org.aml.apimodel.TopLevelModel;
import org.aml.registry.internal.CommitterInfo;
import org.aml.registry.model.Overlays;
import org.aml.registry.model.Registry;
import org.aml.registry.model.RegistryManager;
import org.aml.registry.operations.BuildStat;
import org.aml.registry.operations.LoadRegistry;
import org.aml.registry.operations.PublishRegistry;
import org.aml.registry.operations.ResolveRegistry;
import org.aml.typesystem.AbstractType;
import org.eclipse.egit.github.core.RepositoryId;
import org.junit.Test;

import junit.framework.TestCase;

public class BasicTest {

	protected LoadRegistry loadRegistry = new LoadRegistry(
			//"https://raw.githubusercontent.com/apiregistry/registry/master/oldApis.raml");
			"https://raw.githubusercontent.com/apiregistry/registry/master/registry.json");

	@Test
	public void test() {
		String user = System.getenv().get("GIT_USER");
		String password = System.getenv().get("GIT_PASSWORD");
		Registry apply = new ResolveRegistry().apply(loadRegistry.get());
		if (user != null && password != null) {
			new PublishRegistry(new CommitterInfo(user, password, "pavel@onpositive.com", "Petrochenko Pavel"),
					new RepositoryId("apiregistry", "registry")).accept(apply);
		}
		System.out.println(new BuildStat().apply(apply));
	}
	
	@Test
	public void test1() {
		Overlays overlays = RegistryManager.getInstance().getDefault().getOverlays("APIs.guru");
		TestCase.assertTrue(!overlays.getOverlaysFor().isEmpty());
	}
	
	@Test
	public void test2() {
		TopLevelModel overlays = RegistryManager.getInstance().getDefault().getOverlayed("APIs.guru", "https://raw.githubusercontent.com/OnPositive/aml/master/raml2java.raml");
		AbstractType type = overlays.types().getType("Metrics");
		TestCase.assertNotNull(overlays);
	}
}
