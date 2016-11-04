package org.aml.registry.operations;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.aml.registry.model.ItemDescription;
import org.aml.registry.model.Registry;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

public class IconEnchant implements Function<Registry, Registry> {

	HttpRequestFactory createRequestFactory = new NetHttpTransport().createRequestFactory();

	@Override
	public Registry apply(Registry t) {
		HashMap<String, String> orgToIcon = new HashMap<>();
		HashSet<String> checked = new HashSet<>();
		for (ItemDescription d : t.items()) {
			String org = d.getOrg();
			if (org == null || d.getIcon() != null) {
				continue;
			}
			String icon = org.toLowerCase()+".com";
			boolean suc = tryOrg(orgToIcon, checked, d, icon);
			if (!suc){
				icon=org.toLowerCase()+".org";
				suc = tryOrg(orgToIcon, checked, d, icon);
			}
		}
		return t;
	}

	public boolean tryOrg(HashMap<String, String> orgToIcon, HashSet<String> checked, ItemDescription d, String icon) {
		try {
			boolean containsKey = orgToIcon.containsKey(icon);
			{
				icon = containsKey ? orgToIcon.get(icon) : icon;
				if (checked.contains(icon)) {
					d.setIcon("http://favicon.yandex.net/favicon/" + icon);
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
							d.setIcon("http://favicon.yandex.net/favicon/" + "raml.org");
							return false;
						} else {
							System.out.println(icon);
							orgToIcon.put(icon, icon);
							checked.add(icon);
							d.setIcon("http://favicon.yandex.net/favicon/" + icon);
						}
					} else {
						System.out.println(icon);
						orgToIcon.put(icon, icon);
						checked.add(icon);
						d.setIcon("http://favicon.yandex.net/favicon/" + icon);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

}
