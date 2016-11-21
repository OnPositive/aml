package org.aml.swagger.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.aml.registry.model.RegistryManager;
import org.aml.swagger.reader.WriteApis;
import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;

import junit.framework.TestCase;

public class Swagger2RamlTest extends TestCase{

	public static void extractFolder(String zipFile, String extractFolder) {
		try {
			int BUFFER = 2048;
			File file = new File(zipFile);
			ZipFile zip = new ZipFile(file);
			String newPath = extractFolder;
			new File(newPath).mkdir();
			Enumeration<?> zipFileEntries = zip.entries();
			while (zipFileEntries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				File destFile = new File(newPath, currentEntry);
				File destinationParent = destFile.getParentFile();
				destinationParent.mkdirs();
				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					byte data[] = new byte[BUFFER];
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}

			}
			zip.close();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void test0(){
		if (System.getenv().get("SKIP_HEAVY")!=null){
			return;
		}
		TemporaryFolder tempSwaggerApis=new TemporaryFolder();
		TemporaryFolder tempRAMLApis=new TemporaryFolder();
		try{
		File fs=File.createTempFile("ddd", "tmp");		
		FileUtils.copyInputStreamToFile(RegistryManager.getInstance().getClass().getResourceAsStream("/azure-specs.zip"), fs);
		tempSwaggerApis.create();
		tempRAMLApis.create();
		extractFolder(fs.getAbsolutePath(), tempSwaggerApis.getRoot().getAbsolutePath());
		WriteApis.write(tempSwaggerApis.getRoot().getAbsolutePath(), tempRAMLApis.getRoot().getAbsolutePath());
		tempSwaggerApis.delete();
		tempRAMLApis.delete();
		}catch (Exception e) {
			throw new IllegalStateException();
		}
	}
}
