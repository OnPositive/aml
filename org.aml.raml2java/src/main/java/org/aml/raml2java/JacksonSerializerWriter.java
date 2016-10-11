package org.aml.raml2java;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.acbuilder.AcScheme;
import org.aml.typesystem.acbuilder.CompositeAcElement;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JStatement;

public class JacksonSerializerWriter extends GenericAcAdapterWriter{

	
	public JacksonSerializerWriter() {
		super("/jacksonSerializer.tpl");
	}

	
	protected void contribute(CompositeAcElement ac,JBlock block,AbstractType target,JavaWriter writer){
		
	}
	
	protected JBlock generateWriter(AcScheme scheme, JavaWriter writer) {
		JBlock bl=new JBlock();
		for (AbstractType tp:scheme.getSchemes().keySet()){
			String name = getName(tp, writer);
			String ge="value.get"+Character.toUpperCase(name.charAt(0))+name.substring(1)+"()";
			JBlock th=bl._if(JExpr.direct(ge+"!=null"))._then();
			th.add(new JStatement() {
				
				@Override
				public void state(JFormatter f) {
					f.p("gen.writeObject("+ge+");");
					f.nl();
				}
			});
		}
		return bl;
	}

	@Override
	protected JBlock generateAc(AcScheme scheme,JavaWriter writer) {
		JBlock impl=new JBlock();		
		return impl;
	}
}