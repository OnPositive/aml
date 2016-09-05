package org.aml.typesystem.ramlwriter;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.BuiltIns;
import org.aml.typesystem.TypeOps;
import org.aml.typesystem.yamlwriter.RamlWriter;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class WriterTest {

	static class CLS{
		public String name;
		public boolean isAttr;
	}
	
	@Test
	public void test() {
		AbstractType deriveObjectType = TypeOps.deriveObjectType("Person");
		deriveObjectType.declareProperty("name", BuiltIns.STRING,false);
		deriveObjectType.declareProperty("lastName", BuiltIns.STRING,false);
		deriveObjectType.declareProperty("age", BuiltIns.INTEGER,false);		
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);		
		RamlWriter w=new RamlWriter();
		String result=w.store(deriveObjectType);
		System.out.println(result);
	}

}
