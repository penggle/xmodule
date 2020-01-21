package com.penglecode.xmodule.java8.examples.newfeatures;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件操作
 * 
 * @author 	pengpeng
 * @date	2018年12月28日 下午1:53:25
 */
public class FilesExample {

	public static void lines() throws Exception {
		String projectClassPath = FilesExample.class.getResource("/").getPath();
		Path path = Paths.get(projectClassPath.substring(1), "messages.properties");
		Files.lines(path, Charset.forName("UTF-8")).forEach(System.out::println);
	}
	
	public static void list() throws Exception {
		String projectClassPath = FilesExample.class.getResource("/").getPath();
		String[] currentPackages = FilesExample.class.getPackage().getName().split("\\.");
		Path path = Paths.get(projectClassPath.substring(1), currentPackages);
		Files.list(path).forEach(System.out::println);
	}
	
	public static void walk() throws Exception {
		String projectClassPath = FilesExample.class.getResource("/").getPath();
		Path path = Paths.get(projectClassPath.substring(1));
		Files.walk(path).forEach(System.out::println);
	}
	
	public static void main(String[] args) throws Exception {
		//lines();
		//list();
		walk();
	}

}
