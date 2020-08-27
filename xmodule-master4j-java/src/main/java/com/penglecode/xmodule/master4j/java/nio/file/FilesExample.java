package com.penglecode.xmodule.master4j.java.nio.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * 文件操作
 * 
 * @author 	pengpeng
 * @date	2018年12月28日 下午1:53:25
 */
public class FilesExample {

	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

	public static void lines() throws Exception {
		String projectClassPath = FilesExample.class.getResource("/").getPath();
		Path path = Paths.get(projectClassPath.substring(1), "messages.properties");
		Files.lines(path, StandardCharsets.UTF_8).forEach(System.out::println);
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

	public static void createTempFileAndWrite() throws IOException {
		Path filePath = getOrCreate(Paths.get(TEMP_DIR,"system.properties"));
		List<String> lines = new ArrayList<>();
		Properties properties = System.getProperties();
		for(Map.Entry<Object,Object> entry : properties.entrySet()) {
			lines.add(entry.getKey().toString() + "=" + entry.getValue().toString());
		}
		Files.write(filePath, lines, StandardCharsets.UTF_8);
	}

	public static void createTempFileAndRead() throws IOException {
		Path filePath = getOrCreate(Paths.get(TEMP_DIR,"system.properties"));
		Stream<String> lineStream = Files.lines(filePath, StandardCharsets.UTF_8);
		lineStream.forEach(System.out::println);
	}

	protected static Path getOrCreate(Path filePath) throws IOException {
		if(!filePath.toFile().exists()) {
			filePath = Files.createFile(filePath);
		}
		return filePath;
	}
	
	public static void main(String[] args) throws Exception {
		//lines();
		//list();
		walk();
	}

}
