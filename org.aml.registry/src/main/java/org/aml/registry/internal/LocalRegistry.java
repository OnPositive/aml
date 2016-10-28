package org.aml.registry.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class LocalRegistry {
	
	protected File root;
	
	protected HashMap<String, File>locationToFile=new HashMap<String, File>();

	public LocalRegistry(String file) {
		this.root=new File(file);
	}

	public File getRoot() {
		return root;
	}

	public void setRoot(File root) {
		this.root = root;
	}
	
	public File getLocalFileFor(String url){
		URL url2;
		try {
			url2 = new URL(url);
			String path=url2.getPath();
			String hst=url2.getHost();
			File hostFile=new File(root,hst);
			File localFile=new File(hostFile,path);			
			return localFile;
		} catch (MalformedURLException e) {
			throw new IllegalStateException();
		}		
	}
}
