package com.shuttlemap.web.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ResourceUtil {
	public static final String loadResource(String path) throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		URLConnection conn = url.openConnection();
		byte []buf = new byte[conn.getContentLength()];
		InputStream in = conn.getInputStream();
		try {
			int len = in.read(buf);
			return new String(buf, 0, len, "UTF-8");
		} finally {
			in.close();
		}
	}
}
