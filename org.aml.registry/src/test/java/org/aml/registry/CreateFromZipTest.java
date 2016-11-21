package org.aml.registry;

import org.aml.apimodel.TopLevelModel;
import org.aml.registry.model.Registry;
import org.aml.registry.model.RegistryManager;

import junit.framework.TestCase;

public class CreateFromZipTest extends TestCase{

	public void test0(){
		Registry registry = RegistryManager.getInstance().getDefault();		
		System.out.println("Registry aquired");
		registry.items().forEach(x->{
			TopLevelModel mod=x.resolve();
			TestCase.assertTrue(mod!=null);
		});
		TestCase.assertTrue(registry.items().size()>400);
	}
}
