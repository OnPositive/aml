package org.aml.registry.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static void zipDir(String zipFileName, String dir)  {
		try{
		File dirObj = new File(dir);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		System.out.println("Creating : " + zipFileName);
		addDir(new File(dir),dirObj, out);
		out.close();
		}catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private static void addDir(File root, File dirObj, ZipOutputStream out) throws IOException {
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(root,files[i], out);
				continue;
			}
			FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
			System.out.println(" Adding: " + files[i].getAbsolutePath());
			String substring = files[i].getAbsolutePath().substring(root.getAbsolutePath().length()+1);
			out.putNextEntry(new ZipEntry(substring));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}

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
}
