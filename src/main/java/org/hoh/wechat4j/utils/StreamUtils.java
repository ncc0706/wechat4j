package org.hoh.wechat4j.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StreamUtils {
	private static final String DEFAULT_CHARSET = "UTF-8";

	public static final String streamToString(InputStream stream) throws UnsupportedEncodingException, IOException {
		StringBuffer buffer = new StringBuffer();
		if (stream == null) {
			return null;
		} else {
			byte[] b = new byte[4096];
			for (int n; (n = stream.read(b)) != -1;) {
				buffer.append(new String(b, 0, n, DEFAULT_CHARSET));
			}
		}

		return buffer.toString();
	}
}
