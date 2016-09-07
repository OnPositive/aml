package org.aml.java2raml;

import java.util.Collection;

import org.aml.typesystem.ITypeModel;
import org.junit.Test;

import junit.framework.TestCase;

public class CompleteTest extends TestCase{

	@Test
	public void test0(){
		String file=CompleteTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		Config cfg=new Config();
		cfg.pathToLookForClasses=file;
		cfg.packageNamesToLook.add(CompleteTest.class.getPackage().getName());
		cfg.classMasksToIgnore.add(".*Test");
		cfg.ignoreUnreferencedAnnotationDeclarations=true;
		ClassLoaderBasedCollector m=new ClassLoaderBasedCollector(CompleteTest.class.getClassLoader());
		Collection<ITypeModel> gather = m.gather(cfg);
		TestCase.assertEquals(gather.size(),11);
	}
	
	@Test
	public void test1(){
		String file=CompleteTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		Config cfg=new Config();
		cfg.pathToLookForClasses=file;
		cfg.packageNamesToLook.add(CompleteTest.class.getPackage().getName());
		cfg.classMasksToIgnore.add(".*Test");
		String str=new Java2Raml().processConfigToString(CompleteTest.class.getClassLoader(),cfg);
		BasicTest.compare(str, "/t11.raml");
	}
}