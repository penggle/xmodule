package com.penglecode.xmodule.java8.examples.newfeatures;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Example {

	public static void testBase641() throws Exception {
		String str1 = "http://www.yiibai.com/java8/java8_base64.html";
		System.out.println(Base64.getEncoder().encodeToString(str1.getBytes("UTF-8")));
	}
	
	public static void testBase642() throws Exception {
		String iconPath = "C:/Users/pengle/Desktop/images/icon.jpg";
		byte[] iconBytes = Files.readAllBytes(Paths.get(iconPath));
		String iconBase64Str = Base64.getEncoder().encodeToString(iconBytes);
		System.out.println(iconBase64Str);
		System.out.println(String.format("data:image/jpeg;base64,%s", iconBase64Str));
	}
	
	public static void main(String[] args) throws Exception {
		//testBase641();
		testBase642();
	}

}
