package org.aml.registry.model;

import java.io.File;
import java.io.StringReader;

import org.aml.registry.internal.LocalRegistry;
import org.aml.registry.internal.ZipUtil;
import org.aml.registry.operations.AquireRegistryFromZip;
import org.apache.commons.io.FileUtils;

import com.github.fge.jackson.JacksonUtils;

public class RegistryManager {

	private static final String DEFAULT = "20.11.16_20-15";

	private final static RegistryManager instance = new RegistryManager();

	protected File managerFolder;

	public RegistryManager() {
		managerFolder = new File(System.getProperty("user.home"), ".api-registry-releases");
	}
	
	public Registry getDefault(){
		return aquireRelease(DEFAULT);
	}

	public Registry aquireRelease(String releaseId) {
		
		File releaseFolder = new File(managerFolder, releaseId);
		if (releaseFolder.exists()) {
			for (File f : releaseFolder.listFiles()) {
				if (f.getName().endsWith(".json")) {
					try {
						String str = FileUtils.readFileToString(f);
						Registry readValue = JacksonUtils.getReader().forType(Registry.class)
								.readValue(new StringReader(str));
						LocalRegistry rs=new LocalRegistry(releaseFolder.getAbsolutePath());
						readValue.items().forEach(x -> {
							x.setOriginalLocation("https://"+x.getLocation().replace('\\', '/'));
							x.setLocation(new File(releaseFolder, x.getLocation()).getAbsolutePath().replace('\\', '/'));
							x.setLocalRegistry(rs);
						});
						return readValue;
					} catch (Exception e) {
						throw new IllegalStateException(e);
					}
				}
			}
		} else {
			if (releaseId.equals(DEFAULT)){
				try{
				File fs=File.createTempFile("ddd", "tmp");
				
				FileUtils.copyInputStreamToFile(RegistryManager.getInstance().getClass().getResourceAsStream("/registry20.11.16_20-15.zip"), fs);
				System.out.println("Starting to extract...");
				ZipUtil.extractFolder(fs.getAbsolutePath(), releaseFolder.getAbsolutePath());
				System.out.println("Extracted");
				for (File f:releaseFolder.listFiles()){
					if (f.getName().endsWith(".json")){
						String str=FileUtils.readFileToString(f);
						Registry readValue = JacksonUtils.getReader().forType(Registry.class)
								.readValue(new StringReader(str));
						readValue.items().forEach(x->{
							x.setOriginalLocation("https://"+x.getLocation().replace('\\', '/'));
							x.setLocation(new File(releaseFolder,x.getLocation()).getAbsolutePath().replace('\\', '/'));
						});
						return readValue;
					}
				}
				}catch (Exception e) {
					throw new IllegalStateException();
				}
			}
			AquireRegistryFromZip aquireRegistryFromZip = new AquireRegistryFromZip(
					"https://github.com/apiregistry/registry/raw/gh-pages/releases/registry" + releaseId + ".zip",
					releaseFolder.getAbsolutePath());
			Registry registry = aquireRegistryFromZip.get();
			LocalRegistry rs=new LocalRegistry(releaseFolder.getAbsolutePath());
			registry.items().forEach(x->x.setLocalRegistry(rs));
			return registry;
		}
		return null;
	}

	public static RegistryManager getInstance() {
		return instance;
	}
}
