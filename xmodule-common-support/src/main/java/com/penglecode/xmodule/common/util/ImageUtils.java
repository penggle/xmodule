package com.penglecode.xmodule.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.util.Assert;

import com.google.common.io.Closeables;
import com.penglecode.xmodule.common.support.ImagePixel;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 基于simpleimage的图片处理类
 * 
 * @author 	pengpeng
 * @date   		2017年2月17日 上午10:26:55
 * @version 	1.0
 */
public class ImageUtils {

	/**
	 * 获取图片的像素
	 * @param imgFile
	 * @return
	 */
	public static ImagePixel getImagePixel(String fullImgPath) {
		return getImagePixel(new File(fullImgPath));
	}
	
	/**
	 * 获取图片的像素
	 * @param imgFile
	 * @return
	 */
	public static ImagePixel getImagePixel(File imgFile) {
		try {
			return getImagePixel(new FileInputStream(imgFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取图片的像素
	 * @param imgFile
	 * @return
	 */
	public static ImagePixel getImagePixel(InputStream in) {
		try {
			BufferedImage bi = ImageIO.read(in);
			return new ImagePixel(bi.getWidth(), bi.getHeight());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Closeables.closeQuietly(in);
		}
	}
	
	/**
	 * 只支持等比缩小(不可放大)
	 * @param srcImgFilePath
	 * @param destImgFilePath
	 * @param targetWidth
	 * @param targetHeight
	 */
	public static void scaleImage(String srcImgFilePath, String destImgFilePath, int targetWidth, int targetHeight) {
		scaleImage(new File(srcImgFilePath), new File(destImgFilePath), targetWidth, targetHeight);
	}
	
	/**
	 * 只支持等比缩小(不可放大)
	 * @param srcImgFilePath
	 * @param destImgFilePath
	 * @param targetWidth
	 * @param targetHeight
	 */
	public static void scaleImage(File srcImgFile, File destImgFile,  int targetWidth, int targetHeight) {
		Assert.isTrue(targetWidth > 0, "Parameter 'targetWidth' must be > 0!");
		Assert.isTrue(targetHeight > 0, "Parameter 'targetHeight' must be > 0!");
		try {
			Thumbnails.of(srcImgFile).size(targetWidth, targetHeight).toFile(destImgFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		scaleImage("d:/iphone7.jpg", "d:/iphone7-scale.jpg", 300, 300);
	}

}
