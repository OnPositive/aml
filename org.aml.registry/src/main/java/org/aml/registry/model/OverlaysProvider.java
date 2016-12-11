package org.aml.registry.model;

import java.io.IOException;

import org.aml.registry.internal.HTTPUtil;

import com.github.fge.jackson.JacksonUtils;

public class OverlaysProvider {

	public static Overlays getOverlaysFor(String url){
		url=url.replace('/', '_');
		url=url.replace(':', '_');
		try {
			String str=HTTPUtil.readString("http://1-dot-adept-turbine-152120.appspot.com/ramlregistry/"+url);
			Overlays result=JacksonUtils.getReader().forType(Overlays.class).readValue(str);
			return result;
		} catch (IOException e) {
			return null;
		}
	}
}
