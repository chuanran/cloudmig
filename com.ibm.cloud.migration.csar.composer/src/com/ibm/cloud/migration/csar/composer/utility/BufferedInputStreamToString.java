package com.ibm.cloud.migration.csar.composer.utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BufferedInputStreamToString {
	
	public static String convertInputStreamToString(BufferedInputStream bis) throws IOException{
		String value = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] b = new byte[512];
		int c;
		while ((c = bis.read(b)) != -1) {
					os.write(b, 0, c);
		}
		value = new String(os.toByteArray());
		return value == null ? value : value.trim();
	}
}
