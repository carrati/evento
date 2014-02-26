package com.evento.utils;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class UnzipFile {
	
	@SuppressWarnings("rawtypes")
	public static boolean unzip(String from, String to) {
		try {
			String inFilename = from;
			ZipFile zf = new ZipFile(inFilename);

			Enumeration entries = zf.entries();
			if (!entries.hasMoreElements())
				return false;

			ZipEntry entry = (ZipEntry) entries.nextElement();
			InputStream is = zf.getInputStream(entry);
			String outFilename = to;
			OutputStream out = new FileOutputStream(outFilename);

			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);

			out.close();
			is.close();
			zf.close();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}

		return false;
	}
	
	public static boolean ungzip(String from, String to) {
		FileInputStream fis = null;
		GZIPInputStream gz = null;
		OutputStream out = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(new File(from));
			gz = new GZIPInputStream(fis);

			fos = new FileOutputStream(to, false);
			out = new BufferedOutputStream(fos);

			int data = -1;
			while (true) {
				data = gz.read();
				if (data == -1) {
					break;
				}
				out.write(data);
			}
			out.close();
			fos.close();
			gz.close();
			fis.close();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}

		return false;
	}

	public static void main(String[] args) {
		unzip("/var/emconta/xmls/testeLivraria.zip", "/var/emconta/xmls/testeLivraria.xml");
	}
}
