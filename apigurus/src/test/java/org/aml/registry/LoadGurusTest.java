package org.aml.registry;

import org.aml.apigurus.convert.Converter;
import org.junit.rules.TemporaryFolder;

import junit.framework.TestCase;

public class LoadGurusTest extends TestCase {

	public void test0() {
		TemporaryFolder temporaryFolder = new TemporaryFolder();
		try {
			temporaryFolder.create();
			Converter converter = new Converter(temporaryFolder.getRoot().getAbsolutePath());
			converter.get();
			TestCase.assertTrue(converter.getSucccessCount()>=129);
			temporaryFolder.delete();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
