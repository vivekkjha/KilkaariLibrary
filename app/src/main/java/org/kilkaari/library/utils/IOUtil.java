package org.kilkaari.library.utils;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
	public static String readInputStream(InputStream is) throws IOException {
		StringBuffer stream = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = is.read(b)) != -1;) {
			stream.append(new String(b, 0, n));
		}
		return stream.toString();
	}
}
