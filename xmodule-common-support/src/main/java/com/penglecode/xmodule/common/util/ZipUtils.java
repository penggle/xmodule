package com.penglecode.xmodule.common.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 * 
 * @author 	pengpeng
 * @date 	2020年5月3日 下午5:13:47
 */
public class ZipUtils {

	/**
	 * 创建压缩文件输出流
	 * @param out
	 * @param crc32		- 使用crc3校验?
	 * @return
	 */
	public static ZipOutputStream createZipFile(OutputStream out, boolean crc32) {
		if(crc32) {
			// 对输出文件做CRC32校验
	        CheckedOutputStream cos = new CheckedOutputStream(out, new CRC32());
	        return new ZipOutputStream(cos);
		} else {
			return new ZipOutputStream(out);
		}
	}
	
	/**
	 * 添加文件到压缩文件中
	 * @param zip
	 * @param fileBytes
	 * @param fileName
	 * @throws Exception
	 */
	public static void addEntryFile(ZipOutputStream zip, byte[] fileBytes, String fileName) throws Exception {
		ZipEntry zipEntry = new ZipEntry(fileName);
		zip.putNextEntry(zipEntry);
		zip.write(fileBytes);
        zip.flush();
        zip.closeEntry();
	}
	
	/**
	 * 添加文件到压缩文件中
	 * (注意zip.close()需要在具体程序中手动关闭，否则会出现压缩文件中添加的各个文件为空文件的问题)
	 * @param zip
	 * @param fileInput
	 * @param fileName
	 * @throws Exception
	 */
	public static void addEntryFile(ZipOutputStream zip, InputStream fileInput, String fileName) throws Exception {
		ZipEntry zipEntry = new ZipEntry(fileName);
		zip.putNextEntry(zipEntry);
		byte[] buffer = new byte[1024];
		int len = 0 ;
        // 读取文件的内容,打包到zip文件
        while ((len = fileInput.read(buffer)) != -1) {
        	zip.write(buffer, 0, len);
        }
        zip.flush();
        zip.closeEntry();
	}
	
}
