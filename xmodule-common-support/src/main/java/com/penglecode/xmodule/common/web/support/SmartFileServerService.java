package com.penglecode.xmodule.common.web.support;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.penglecode.xmodule.common.util.UUIDUtils;

/**
 * 小型文件服务器文件服务
 * 
 * @author 	pengpeng
 * @date	2019年5月18日 下午2:06:47
 */
public interface SmartFileServerService {

	/**
	 * 获取文件服务器的存储目录
	 * @return
	 */
	public String getFileServerBaseDirectory();
	
	/**
	 * 获取上传文件的本地临时存储目录
	 * @return
	 */
	public String getLocalTempBaseDirectory();
	
	/**
	 * 创建本地临时存储文件路径
	 * @param originalFileName			- 原始文件名,例如 abc.jpg
	 * @param renameStrategy			- 重命名策略
	 * @return	返回重命名后的文件名, 例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 */
	public String createLocalTempFilePath(String originalFileName, RenameStrategy renameStrategy);
	
	/**
	 * 获取临时文件的实际储存路径
	 * @param localTempFilePath			- 上传的本地临时文件的相对路径,例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 * @return
	 */
	public String getLocalTempFileLocation(String localTempFilePath, AccessStrategy accessStrategy);
	
	/**
	 * 获取临时文件的文件Resource
	 * @param localTempFilePath			- 上传的本地临时文件的相对路径,例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 * @return
	 */
	public Resource getLocalTempFileResource(String localTempFilePath, AccessStrategy accessStrategy);
	
	/**
	 * 将上传的MultipartFile转储为本地临时文件
	 * @param uploadFile				- 上传文件对象
	 * @param localTempFilePath			- 上传的本地临时文件的相对路径,例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 * @return 返回转储结果
	 */
	public TransferResult transferLocalTempFile(MultipartFile uploadFile, String localTempFilePath, AccessStrategy accessStrategy) throws IOException;
	
	/**
	 * 下载刚上传的本地临时文件
	 * @param localTempFilePath			- 上传的临时文件的相对路径,例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 * @param outputStream				- 下载输出流,例如#HttpServletResponse.getOutputStream()
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 */
	public void downloadLocalTempFile(String localTempFilePath, OutputStream outputStream, AccessStrategy accessStrategy) throws IOException;
	
	/**
	 * 获取文件服务器上永久储存的文件实际路径
	 * @param serverStoreFilePath		- 上传的永久文件的相对路径,例如 /file/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.xlsx
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 * @return
	 */
	public String getServerStoreFileLocation(String serverStoreFilePath, AccessStrategy accessStrategy);
	
	/**
	 * 获取文件服务器上永久储存的文件Resource
	 * @param serverStoreFilePath		- 上传的永久文件的相对路径,例如 /file/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.xlsx
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 * @return
	 */
	public Resource getServerStoreFileResource(String serverStoreFilePath, AccessStrategy accessStrategy);
	
	/**
	 * 转储本地临时文件到文件服务器上
	 * 
	 * @param localTempFilePath			- 上传的本地临时文件的相对路径,例如 /upload/temp/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.jpg
	 * @param serverStoreFilePath		- 永久存储的相对路径,例如/img/usericon
	 * @param deleteTempFile			- 是否删除临时文件?
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 * @return 返回转储结果
	 */
	public TransferResult transferServerStoreFile(String localTempFilePath, String serverStoreFilePath, boolean deleteTempFile, AccessStrategy accessStrategy) throws IOException;
	
	/**
	 * 下载文件服务器上永久文件
	 * @param serverStoreFilePath		- 上传的永久文件的相对路径,例如 /excel/20180122/4996a3ed7b6c4beda26c64ee7a506fbc.xlsx
	 * @param outputStream				- 下载输出流,例如#HttpServletResponse.getOutputStream()
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 */
	public void downloadServerStoreFile(String serverStoreFilePath, OutputStream outputStream, AccessStrategy accessStrategy) throws IOException;
	
	/**
	 * 删除临时文件
	 * @param localTempFilePath			- 上传的本地临时存储文件的相对路径
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 */
	public void removeLocalTempFile(String localTempFilePath, AccessStrategy accessStrategy);
	
	/**
	 * 删除永久文件
	 * @param serverStoreFilePath		- 上传的永久存储文件的相对路径
	 * @param accessStrategy			- 访问策略，public-上传文件可Http匿名访问，protected-上传文件需要已认证才能Http访问
	 */
	public void removeServerStoreFile(String serverStoreFilePath, AccessStrategy accessStrategy);
	
	public static enum RenameStrategy {
		
		UUID(){
			@Override
			public String rename(String originalFileName) {
				return UUIDUtils.uuid() + originalFileName.substring(originalFileName.lastIndexOf('.'));
			}
		}, ORIGINAL_FILENAME_AND_UUID(){
			@Override
			public String rename(String originalFileName) {
				int index = originalFileName.lastIndexOf('.');
				return originalFileName.substring(0, index) + "#" + UUIDUtils.uuid() + originalFileName.substring(index);
			}
		};
		
		public abstract String rename(String originalFileName);
		
	}
	
	public static enum AccessStrategy {
		
		//公共资源，匿名可访问
		PUBLIC,
		//受保护的资源，认证可访问
		PROTECTED;
		
		public String forPath(String filePath) {
			return "/" + this.name().toLowerCase() + filePath;
		}
		
	}
	
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
