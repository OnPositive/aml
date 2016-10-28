package org.aml.apigurus.convert;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.aml.apimodel.impl.ApiImpl;
import org.aml.registry.model.ApiDescription;
import org.aml.registry.operations.StoreRegistry;
import org.aml.swagger.reader.SwaggerReader;
import org.aml.typesystem.yamlwriter.RamlWriter;
import org.apache.commons.io.FileUtils;
import org.apigurus.ApiVersion;
import org.apigurus.LoadGurus;
import org.apigurus.Registry;
import org.apigurus.RegistryEntry;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

public class Converter implements Supplier<org.aml.registry.model.Registry> {

	protected String targetPath;
	protected boolean dryRun;
	protected String filter;

	public Converter(String targetPath) {
		this.targetPath = targetPath;
	}

	static HashMap<String, String> orgToIcon = new HashMap<>();

	static {
		orgToIcon.put("citrixonline.com", "citrix.com");
		orgToIcon.put("bbci.co.uk", "bbc.com");

	}

	HttpRequestFactory createRequestFactory = new NetHttpTransport().createRequestFactory();
	HashSet<String> checked = new HashSet<>();

	@Override
	public org.aml.registry.model.Registry get() {
		org.aml.registry.model.Registry result = new org.aml.registry.model.Registry();
		result.setName("API Gurus RAML mirror");
		Registry r = new LoadGurus().get();
		System.out.println(r.getRegistry().size());
		ArrayList<RegistryEntry> acceptet = new ArrayList<RegistryEntry>();
		int count=0;
		int sc = 0;
		for (String e : r.getRegistry().keySet()) {
			if (e.startsWith("windows.net")) {
				continue;
			}
			if (e.startsWith("azure.com")) {
				continue;
			}
			if (e.startsWith("googleapis.com")) {
				continue;
			}
			if (this.filter != null) {
				if (e.indexOf(filter) == -1) {
					continue;
				}
			}
			acceptet.add(r.getRegistry().get(e));
			Map<String, ApiVersion> registry = r.getRegistry().get(e).getVersions().getRegistry();
			for (ApiVersion v : registry.values()) {
				String swaggerUrl = v.getSwaggerUrl();

				ApiDescription ds = new ApiDescription();
				String org = e;
				String suffix = "";
				count++;
				int colon = e.indexOf(':');
				if (colon != -1) {
					suffix = e.substring(colon + 1);
					org = org.substring(0, colon);
				}
				String icon = org;
				try {
					boolean containsKey = orgToIcon.containsKey(icon);
					{
						icon = containsKey ? orgToIcon.get(icon) : icon;
						if (checked.contains(icon)) {
							ds.setIcon("http://favicon.yandex.net/favicon/" + icon);
						} else {
							HttpRequest buildGetRequest = createRequestFactory
									.buildGetRequest(new GenericUrl("http://favicon.yandex.net/favicon/" + icon));
							HttpResponse execute = buildGetRequest.execute();
							BufferedImage read = ImageIO.read(execute.getContent());

							if (read.getWidth() <= 1) {
								icon = "www." + icon;
								buildGetRequest = createRequestFactory
										.buildGetRequest(new GenericUrl("http://favicon.yandex.net/favicon/" + icon));
								execute = buildGetRequest.execute();
								read = ImageIO.read(execute.getContent());
								if (read.getWidth() <= 1) {
									System.err.println(icon);
									ds.setIcon("http://favicon.yandex.net/favicon/" + "raml.org");
								} else {
									System.out.println(icon);
									orgToIcon.put(icon, icon);
									checked.add(icon);
									ds.setIcon("http://favicon.yandex.net/favicon/" + icon);
								}
							} else {
								System.out.println(icon);
								orgToIcon.put(icon, icon);
								checked.add(icon);
								ds.setIcon("http://favicon.yandex.net/favicon/" + icon);
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				ds.setName(v.getInfo().getTitle());
				String version = v.getInfo().getVersion();
				ds.setVersion(version);
				ds.setDescription(v.getInfo().getDescription());

				String org2 = Character.toUpperCase(org.charAt(0)) + org.substring(1);
				if (org2.indexOf('.') != -1) {
					org2 = org2.substring(0, org2.indexOf('.'));
				}
				ds.setOrg(org2);
				version = version.replace('.', '_').replace('/', '.').replace(' ', '_').replace(':', '_');
				String nm = org2.toLowerCase() + suffix + version + ".raml";
				
				try {
					HttpRequest buildGetRequest = new NetHttpTransport().createRequestFactory()
							.buildGetRequest(new GenericUrl(swaggerUrl));
					String str = buildGetRequest.execute().parseAsString();
					try {
						ApiImpl rs = new SwaggerReader().read(str);

						if (rs != null) {
							sc += 1;
						}
						else{
							System.err.println("Error:"+e);
							continue;
						}
						String store = new RamlWriter().store(rs);
						if (store.indexOf("!!binary")!=-1){
							System.out.println("A");
						}
						if (!this.dryRun){
							result.getApis().add(ds);
							ds.setLocation("https://raw.githubusercontent.com/apiregistry/misc/master/" + nm);			
							FileUtils.writeStringToFile(new File(targetPath, nm), store);
						}
						System.out.println("Converted:"+sc+" of:"+count);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} catch (IOException ex) {
					throw new IllegalStateException(e);
				}

			}
		}
		try {
			if (!this.dryRun) {
				FileUtils.write(new File(targetPath, "registry.json"), new StoreRegistry().apply(result));
			}
		} catch (Exception e1) {
			throw new IllegalStateException(e1);
		}
		return result;
	}

	public static void main(String[] args) {
		Converter converter = new Converter("C:\\Users\\Павел\\git\\misc");
		//converter.filter="nytimes";
		//converter.filter="wiki";
		//converter.dryRun=true;
		org.aml.registry.model.Registry vv = converter.get();

	}

}
