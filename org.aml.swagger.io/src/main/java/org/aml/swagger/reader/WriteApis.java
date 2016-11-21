package org.aml.swagger.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.aml.apimodel.Api;
import org.aml.apimodel.impl.ApiImpl;
import org.aml.registry.model.ApiDescription;
import org.aml.registry.model.Registry;
import org.aml.registry.operations.StoreRegistry;
import org.aml.typesystem.yamlwriter.RamlWriter;
import org.apache.commons.io.FileUtils;

public class WriteApis {

	public static void main(String[] args) {
		ArrayList<Api> parse = new DirectoryLister().parse(new File("C:\\Users\\Павел\\git\\azure-rest-api-specs"));
		File fl=new File("C:\\Users\\Павел\\git\\microsoft");
		Registry rs=new Registry();
		rs.setName("Misrosoft Azure APIS");
		for (Api a:parse){
			if (a==null){
				continue;
			}
			String store = new RamlWriter().store((ApiImpl) a);
			String child = a.title()+a.getVersion()+".raml";
			child=child.replace(' ', '_');
			File f=new File(fl,child);
			try {
				FileUtils.write(f, store);
				ApiDescription e = new ApiDescription();
				e.setIcon("https://favicon.yandex.net/favicon/azure.microsoft.com");
				e.setName(a.title());
				e.setVersion(a.version());
				e.setOrg("Microsoft");
				e.setLocation("https://raw.githubusercontent.com/apiregistry/microsoft/master/"+child);
				rs.getApis().add(e);
			} catch (IOException e) {
				throw new IllegalStateException();	
			}
		}
		File f=new File(fl,"registry.json");
		try {
			FileUtils.write(f, new StoreRegistry().apply(rs));
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}	
}