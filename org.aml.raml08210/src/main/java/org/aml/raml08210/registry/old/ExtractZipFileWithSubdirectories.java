package org.aml.raml08210.registry.old;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

public class ExtractZipFileWithSubdirectories {
	
	public static void extractZipFromUrl(String srcUrl,File destination) throws IOException{
		HttpRequest buildGetRequest = LoadOldRegistry.createRequestFactory.buildGetRequest(new GenericUrl(srcUrl));
		File tempFile=File.createTempFile("ramlStore", "zip");
		buildGetRequest.execute().download(new FileOutputStream(tempFile));
		extractZip(tempFile.getAbsolutePath(), destination);
	}
	
	public static void extractZip(String srcFile,File destination) {
		
		// create a directory with the same name to which the contents will be extracted
		File temp = destination;
		temp.mkdir();
		
		ZipFile zipFile = null;
		
		try {
			
			zipFile = new ZipFile(srcFile);
			
			// get an enumeration of the ZIP file entries
			Enumeration<? extends ZipEntry> e = zipFile.entries();
			
			while (e.hasMoreElements()) {
				
				ZipEntry entry = e.nextElement();
				
				File destinationPath = new File(destination, entry.getName());
				 
				//create parent directories
				destinationPath.getParentFile().mkdirs();
				
				// if the entry is a file extract it
				if (entry.isDirectory()) {
					continue;
				}
				else {				
					System.out.println("Extracting file: " + destinationPath);
					
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

					int b;
					byte buffer[] = new byte[1024];

					FileOutputStream fos = new FileOutputStream(destinationPath);
					
					BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

					while ((b = bis.read(buffer, 0, 1024)) != -1) {
						bos.write(buffer, 0, b);
					}
					
					bos.close();
					bis.close();
					
				}
				
			}
			
		}
		catch (IOException ioe) {
			throw new IllegalStateException("Error opening zip file" + ioe);
		}
		 finally {
			 try {
				 if (zipFile!=null) {
					 zipFile.close();
				 }
			 }
			 catch (IOException ioe) {
				 throw new IllegalStateException("Error while closing zip file" + ioe);
			 }
		 }		
	}

}