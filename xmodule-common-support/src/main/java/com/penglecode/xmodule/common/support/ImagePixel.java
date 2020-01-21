package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.util.StringUtils;

public class ImagePixel {

	private Integer width;
	
	private Integer height;

	public ImagePixel() {
		super();
	}

	public ImagePixel(Integer width, Integer height) {
		super();
		this.width = width;
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
	/**
	 * 通过像素表达式来创建,例如 600x600 => ImagePixel(600, 600), 250x* => ImagePixel(250, null)
	 * @param pixelLimitExpression
	 * @return
	 */
	public static ImagePixel createImagePixel(String pixelExpression) {
		String regex = "(\\d+|\\*)x(\\d+|\\*)";
		if(!StringUtils.isEmpty(pixelExpression) && pixelExpression.matches(regex)){
			String[] pixelPair = pixelExpression.trim().split("x");
			ImagePixel pixel = new ImagePixel();
			if(!"*".equals(pixelPair[0])){
				pixel.setWidth(Integer.valueOf(pixelPair[0]));
			}
			if(!"*".equals(pixelPair[1])){
				pixel.setHeight(Integer.valueOf(pixelPair[1]));
			}
			return pixel;
		}
		return null;
	}

	public String toString() {
		return "ImagePixel [width=" + width + ", height=" + height + "]";
	}
	
}
