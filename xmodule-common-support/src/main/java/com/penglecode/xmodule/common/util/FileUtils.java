package com.penglecode.xmodule.common.util;

import org.springframework.util.Assert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件操作工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月20日 上午12:03:21
 * @version  	1.0
 */
public class FileUtils {

	/**
     * 默认的标准推荐使用的文件路径分隔符
     */
    public static final String DEFAULT_STANDARD_FILE_DELIMITER = "/";

    /**
     * 默认的不推荐使用的文件路径分隔符
     */
    public static final String DEFAULT_AGAINST_FILE_DELIMITER = "\\";
    
    /**
     * 协议正则pattern
     */
    public static final Pattern PROTOCOL_PATTERN = Pattern.compile("^([a-zA-Z0-9]+://).+");
    
    /**
     * 默认文件copy缓冲区大小
     */
    public static final int FILE_COPY_BUFFER_SIZE = 20 * 1024 * 1024;
	
    /**
     * 返回文本文件的编码格式
     * @param fullFilePath
     * @return
     */
	public static String getTextFileEncoding(String fullFilePath) {
		String code;
		try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(fullFilePath))) {
			int p = (in.read() << 8) + in.read();
			code = null;
			switch (p) {
			    case 0xefbb:
			        code = "UTF-8";
			        break;
			    case 0xfffe:
			        code = "Unicode";
			        break;
			    case 0xfeff:
			        code = "UTF-16BE";
			        break;
			    default:
			        code = "GBK";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return code;
	}
    
    /**
     * <p>纠正不标准的文件路径分隔符
     * 如：\,\\,\\\,//,/// -> /</p>
     *
     * @param path
     * @return
     */
    public static String normalizePath(String path){
        if (!StringUtils.isEmpty(path)) {
        	Matcher matcher = PROTOCOL_PATTERN.matcher(path);
        	String protocol = null;
        	if(matcher.find()) {
        		protocol = matcher.group(1);
        	}
        	
        	if(!StringUtils.isEmpty(protocol)) {
        		path = path.substring(protocol.length());
        	}
        	// 将一个或多个“\”转化成“/”
        	path = path.replaceAll("\\\\+", "/");
            // 将多个“/”转化成一个“/”
        	path = path.replaceAll("/{2,}", "/");
        	if(!StringUtils.isEmpty(protocol)) {
        		path = protocol + path;
        	}
        }
        return path;
    }
    
    /**
     * @see #java.nio.file.Paths.get(...)
     * @param first
     * @param more
     * @return
     */
    public static String joinPaths(String first, String... more) {
    	Matcher matcher = PROTOCOL_PATTERN.matcher(first);
    	String protocol = null;
    	if(matcher.find()) {
    		protocol = matcher.group(1);
    	}
    	
    	if(!StringUtils.isEmpty(protocol)) {
    		first = first.substring(protocol.length());
    	}
    	
    	Path path = Paths.get(first, more);
    	String result = normalizePath(path.toString());
    	
    	if(!StringUtils.isEmpty(protocol)) {
    		result = protocol + result;
    	}
    	
    	return result;
    }
    
    /**
     * <p>获取文件格式,小写,例如: txt、jpg等</p>
     *
     * @param imageFileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        Assert.hasText(fileName, "Parameter 'fileName' can not be empty!");
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }
    
    /**
     * <p>根据文件路径获取文件名称</p>
     * 
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
    	Assert.hasText(filePath, "Parameter 'filePath' can not be empty!");
    	filePath = normalizePath(filePath);
    	return filePath.substring(filePath.lastIndexOf(DEFAULT_STANDARD_FILE_DELIMITER) + 1);
    }
    
    /**
     * <p>获取文件大小,单位字节</p>
     *
     * @param fileFullPath
     * @return
     * @throws IOException
     */
    public static int getFileSize(String fileFullPath) throws IOException {
    	Assert.hasText(fileFullPath, "Parameter 'fileFullPath' can not be empty!");
        int size = 0;
        fileFullPath = normalizePath(fileFullPath);
        File file = new File(fileFullPath);
        if (file.exists() && !isDirectory(fileFullPath)) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
            if (fis != null) {
                fis.close();
            }
        }
        return size;
    }
    
    /**
     * <p>获取系统临时目录</p>
     * 
     * @return
     */
    public static String getTempDirectoryPath() {
        String path = System.getProperty("java.io.tmpdir");
        if(!StringUtils.isEmpty(path)){
        	return normalizePath(path);
        }
        return path;
    }
    
    /**
     * <p>获取用户目录</p>
     * 
     * @return
     */
    public static String getUserDirectoryPath() {
        String path = System.getProperty("user.home");
        if(!StringUtils.isEmpty(path)){
        	return normalizePath(path);
        }
        return path;
    }
    
    /**
     * <p>根据文件路径获取File对象</p>
     * 
     * @param fullFilePath
     * @return
     */
    public static File getFile(String filePath) {
    	Assert.hasText(filePath, "Parameter 'filePath' can not be empty!");
    	filePath = normalizePath(filePath);
        return new File(filePath);
    }
    
    /**
     * <p>创建文件目录如果需要创建</p>
     *
     * @param filePath
     * @throws Exception
     * @return true-创建了新目录;false-没有创建新目录
     */
    public static boolean mkDirIfNecessary(String filePath) {
        filePath = normalizePath(filePath);
        File dirFile = new File(getFileDirectory(filePath));
        if (!dirFile.exists()) {
            dirFile.mkdirs();
            return true;
        }
        return false;
    }
    
    /**
     * <p>根据文件路径判断该路径表示的是文件还是目录</p>
     *
     * @param filePath
     * @return
     */
    public static boolean isDirectory(String filePath) {
        return Files.isDirectory(Paths.get(filePath));
    }

    /**
     * <p>根据文件路径获取其目录</p>
     *
     * @param filePath
     * @return
     */
    public static String getFileDirectory(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            filePath = normalizePath(filePath);
            Path path = Paths.get(filePath);
            if (Files.isDirectory(path)) {
                return filePath;
            } else {
            	String fileName = path.getFileName().toString();
            	if(fileName == null || fileName.indexOf('.') == -1) {
            		return filePath;
            	} else {
            		return filePath.substring(0, filePath.lastIndexOf(DEFAULT_STANDARD_FILE_DELIMITER));
            	}
            }
        }
        return filePath;
    }
    
    /**
     * <p>重命名文件名</p>
     *
     * @param originalName - 原文件名
     * @param renameAll    - true-舍弃原文件名完全做随机重新命名;false-在原文件名后面做随机重命名
     * @param appendStr    - 加在文件名后的追加后缀,e.g. ${originalName}_${appendStr}.jpg
     * @return
     * @throws Exception
     */
    public static String renameFileName(String originalName, boolean renameAll, String appendStr) {
    	Assert.hasText(originalName, "Parameter 'originalName' can not be empty!");
        String suffix = originalName.substring(originalName.lastIndexOf('.') + 1);
        String fileName = originalName.substring(0, originalName.lastIndexOf('.'));
        String randomName = UUID.randomUUID().toString().replace("-", "");
        String finalName = renameAll ? randomName : fileName + "_" + randomName.substring(0, 8);
        if (!StringUtils.isEmpty(appendStr)) {
            return String.format("%s_%s.%s", finalName, appendStr, suffix);
        } else {
            return String.format("%s.%s", finalName, suffix);
        }
    }
    
    /**
     * <p>文件复制</p>
     *
     * @param srcFullFileName  - 源文件名
     * @param destFullFileName - 目标文件名
     * @throws Exception
     */
    public static void copyFile(String srcFullFileName, String destFullFileName) throws IOException {
    	Assert.hasText(srcFullFileName, "Parameter 'srcFullFileName' can not be empty!");
    	Assert.hasText(destFullFileName, "Parameter 'destFullFileName' can not be empty!");
    	copyFile(getFile(srcFullFileName), getFile(destFullFileName));
    }
    
    /**
     * <p>文件复制</p>
     * 
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
    	com.google.common.io.Files.copy(srcFile, destFile);
    }
    
    /**
     * 格式化文件大小
     * @param fileSize - 单位byte
     * @return
     */
    public static String formatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#");
        if (fileSize < 0) {
            return "不限";
        } else if (fileSize < 1024) {
            return fileSize + "B";
        } else if (fileSize < 1024 * 1024) {
            return df.format(fileSize / (1024 * 1.0)) + "KB";
        } else if (fileSize < 1024 * 1024 * 1024) {
            return df.format(fileSize / (1024 * 1024 * 1.0)) + "MB";
        } else {
            return df.format(fileSize / (1024 * 1024 * 1024 * 1.0)) + "GB";
        }
    }
    
    /**
     * <p>删除文件</p>
     *
     * @param fullPath
     * @return
     */
    public static boolean deleteFile(String fullPath) {
        File file = getFile(fullPath);
        if(file.exists()){
        	return file.delete();
        }
        return false;
    }
    
    /**
     * 删除目录(包括其中的文件)
     * @param fullPath
     * @return
     */
    public static void deleteDirectory(String dir) {
    	deleteDirectory(new File(dir));
    }
    
    /**
     * 删除目录(包括其中的文件)
     * @param fullPath
     * @return
     */
    public static void deleteDirectory(File dir) {
    	if(dir != null && dir.isDirectory()) {
    		File[] files = dir.listFiles();
    		if(files != null && files.length > 0) {
    			for(File file : files) {
    				if(file.isDirectory()) {
    					deleteDirectory(file);
    				} else {
    					file.delete();
    				}
    			}
    		}
    		dir.delete();
    	}
    }
    
    /**
     * <p>尽最大努力删除文件,删除失败不抛出异常</p>
     *
     * @param fullPath
     * @return
     */
    public static void deleteFileQuietly(String fullPath) {
        try {
			File file = getFile(fullPath);
			if(file.exists()){
				file.delete();
			}
		} catch (Exception e) {
		}
    }
    
    /**
     * 如果文件不存在，则创建空文件
     * @param fullPath
     */
    public static void createIfNotExists(String fullPath) {
    	createIfNotExists(new File(fullPath));
    }
    
    /**
     * 如果文件不存在，则创建空文件
     * @param file
     */
    public static void createIfNotExists(File file) {
    	try {
    		if(file != null && !file.exists()) {
    			Files.createFile(Paths.get(file.getPath()));
    		}
    	} catch (Exception e) {
    	}
    }
    
    /**
     * 创建文件的输入流
     * @param file
     * @return
     */
    public static InputStream createInputStream(File file) {
    	try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
     * 创建文件的输出流
     * @param file
     * @return
     */
    public static OutputStream createOutputStream(File file) {
    	try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
    }
    
}