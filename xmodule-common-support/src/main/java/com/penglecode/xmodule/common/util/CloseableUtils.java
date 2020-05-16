package com.penglecode.xmodule.common.util;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtils {

	public static void closeQuietly(Closeable closeable) {
		if(closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {}
		}
	}
	
}
