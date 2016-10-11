package org.aml.raml2java;

import java.io.StringWriter;

import org.aml.typesystem.AbstractType;
import org.aml.typesystem.acbuilder.AcScheme;
import org.raml.v2.internal.utils.StreamUtils;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JStatement;

public abstract class GenericAcAdapterWriter {

	public GenericAcAdapterWriter(String templatePath) {
		super();
		this.template = StreamUtils.toString(GenericAcAdapterWriter.class.getResourceAsStream(templatePath));
	}

	protected String template;
	
	
	public String generateAdapter(String packageName, AcScheme scheme,String targetName,JavaWriter writer){
		String template=this.template.replace((CharSequence)"{typeName}", targetName);		
		template=template.replace((CharSequence)"{packageName}", packageName);
		template = doReader(scheme, writer, template);
		template = doWriter(scheme, writer, template);
		return template;		
	}


	private String doReader(AcScheme scheme, JavaWriter writer, String template) {
		JBlock bl=generateAc(scheme,writer);		
		StringWriter w = new StringWriter();
		JFormatter f = new JFormatter(w);
		for (int i=0;i<4;i++){
			f.i();
		}
		bl.generate(f);
		template=template.replace((CharSequence)"{acCode}", w.toString());
		return template;
	}


	protected String doWriter(AcScheme scheme, JavaWriter writer, String template) {
		StringWriter w;
		JFormatter f;
		JBlock wr=generateWriter(scheme,writer);		
		w = new StringWriter();
		f = new JFormatter(w);
		for (int i=0;i<4;i++){
			f.i();
		}
		wr.generate(f);
		template=template.replace((CharSequence)"{writeCode}", w.toString());
		return template;
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
					f.p("gson.toJson("+ge+","+ge+".getClass(), out);");
					f.nl();
				}
			});
		}
		return bl;
	}

	protected String getName(AbstractType target,JavaWriter writer) {
		return target.name();
	}
	protected String getJavaTypeName(AbstractType target,JavaWriter writer) {
		return writer.getType(target).name();
	}
	protected abstract JBlock generateAc(AcScheme scheme,JavaWriter writer);

}
