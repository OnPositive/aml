package org.aml.registry.operations;

import java.io.File;
import java.io.StringReader;
import java.util.function.Supplier;

import org.aml.registry.internal.HTTPUtil;
import org.aml.registry.internal.ZipUtil;
import org.aml.registry.model.Registry;
import org.apache.commons.io.FileUtils;

import com.github.fge.jackson.JacksonUtils;

public class AquireRegistryFromZip implements Supplier<Registry> {

	protected String url;
	protected String path;

	public AquireRegistryFromZip(String url, String path) {
		super();
		this.url = url;
		this.path = path;
		new File(path).mkdirs();
	}

	@Override
	public Registry get() {
		try{
		File fs=File.createTempFile("ddd", "tmp");
		HTTPUtil.readToFile(url, fs);
		ZipUtil.extractFolder(fs.getAbsolutePath(), path);
		for (File f:new File(path).listFiles()){
			if (f.getName().endsWith(".json")){
				String str=FileUtils.readFileToString(f);
				Registry readValue = JacksonUtils.getReader().forType(Registry.class)
						.readValue(new StringReader(str));
				readValue.items().forEach(x->{
					x.setLocation(new File(path,x.getLocation()).getAbsolutePath());
				});
				return readValue;
			}
		}
		return null;
		}catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
