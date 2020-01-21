package com.penglecode.xmodule.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
/**
 * GZip压缩工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月16日 下午3:55:55
 * @version  	1.0
 */
public class GZipUtils {

	public static byte[] compress(byte[] data) throws IOException {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        GZIPOutputStream gos = new GZIPOutputStream(baos);// 压缩
        gos.write(data, 0, data.length);  
        gos.finish();
        byte[] output = baos.toByteArray();  
        baos.flush();  
        baos.close();
        return output;  
    } 
	
}
