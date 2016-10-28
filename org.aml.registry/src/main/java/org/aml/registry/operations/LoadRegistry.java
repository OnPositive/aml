package org.aml.registry.operations;

import java.io.IOException;
import java.io.StringReader;
import java.util.function.Supplier;

import org.aml.registry.internal.HTTPUtil;
import org.aml.registry.model.Registry;

import com.github.fge.jackson.JacksonUtils;

public class LoadRegistry implements Supplier<Registry>{

	protected String registryUrl;
	

	public LoadRegistry(String registryUrl) {
		super();
		this.registryUrl = registryUrl;
	}

	public Registry get() {
		
		try {
			String str = HTTPUtil.readString(registryUrl);
			Registry readValue = JacksonUtils.getReader().forType(Registry.class)
					.readValue(new StringReader(str));
			return readValue;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	
}
