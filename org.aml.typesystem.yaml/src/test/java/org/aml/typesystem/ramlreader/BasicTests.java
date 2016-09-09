package org.aml.typesystem.ramlreader;

import org.aml.typesystem.raml.model.TopLevelRaml;
import org.junit.Test;
import org.raml.v2.internal.impl.RamlBuilder;
import org.raml.v2.internal.utils.StreamUtils;
import org.raml.yagi.framework.nodes.Node;

import junit.framework.TestCase;

public class BasicTests extends TestCase{

	@Test
	public void test() {
		String string = StreamUtils.toString(BasicTests.class.getResourceAsStream("/t1.raml"));
		Node build = new RamlBuilder().build(string);
		TopLevelRaml raml = new TopLevelRamlModelBuilder().build(build);
	}

}
