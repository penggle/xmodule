package com.penglecode.xmodule.common.web.support;

import org.springframework.web.multipart.MultipartFile;

/**
 * 通用小文件上传助手
 * 
 * @author 	pengpeng
 * @date	2019年5月18日 下午2:06:47
 */
public interface XUploadFileHelper {

	/**
	 * 创建重名的文件名
	 * @param originalFileName			- 原始文件名,例如 abc.jpg
	 * @return	返回重命名后的文件名, 例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 */
	public String createRenamedFileName(String originalFileName);
	
	/**
	 * 临时转储
	 * 
	 * 将上传的文件传送到一个临时存储场所(例如一个本地目录等等)
	 * @param uploadFile				- 上传文件对象
	 * @param tempFileSavePath			- 上传的临时文件的相对路径,例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 * @return 返回转储结果
	 */
	public TransferResult tempTransfer(MultipartFile uploadFile, String tempFileSavePath);
	
	/**
	 * 永久转储
	 * 
	 * 将上传的临时文件传送到一个永久存储场所(例如一个本地目录、多媒体云存储等等)
	 * @param tempFileSavePath			- 上传的临时文件的相对路径,例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 * @param fileStorePath				- 永久存储的相对路径,例如/img/usericon
	 * @return 返回转储结果
	 */
	public TransferResult storeTransfer(String tempFileSavePath, String fileStorePath);
	
	/**
	 * 删除临时文件
	 * @param fileSavePath		- 上传的临时存储文件的相对路径
	 */
	public void tempRemove(String fileSavePath);
	
	/**
	 * 删除永久文件
	 * @param fileSavePath		- 上传的永久存储文件的相对路径
	 */
	public void storeRemove(String fileSavePath);
	
	
	public static class TransferResult {
		
		/** 文件相对保存路径 */
		private String fileSavePath;
		
		/** 文件保存真实路径 */
		private String fileSaveLocation;
		
		/** 文件HTTP访问URL */
		private String fileHttpLocation;

		public TransferResult() {
			super();
		}

		public TransferResult(String fileSavePath, String fileSaveLocation, String fileHttpLocation) {
			super();
			this.fileSavePath = fileSavePath;
			this.fileSaveLocation = fileSaveLocation;
			this.fileHttpLocation = fileHttpLocation;
		}

		public String getFileSavePath() {
			return fileSavePath;
		}

		public void setFileSavePath(String fileSavePath) {
			this.fileSavePath = fileSavePath;
		}

		public String getFileSaveLocation() {
			return fileSaveLocation;
		}

		public void setFileSaveLocation(String fileSaveLocation) {
			this.fileSaveLocation = fileSaveLocation;
		}

		public String getFileHttpLocation() {
			return fileHttpLocation;
		}

		public void setFileHttpLocation(String fileHttpLocation) {
			this.fileHttpLocation = fileHttpLocation;
		}

	}
	
}
