package com.penglecode.xmodule.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.consts.GlobalConstants;

/**
 * 基于commons-net的FTP支持实现的FTP工具类
 * 
 * @author pengpeng
 * @date 2019年12月29日 下午9:37:56
 */
public class FTPUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(FTPUtils.class);
	
	/**
	 * 默认的FTP客户端配置自定义
	 */
	private static final Consumer<FTPClient> DEFAULT_FTPCLIENT_CONFIG = (ftpClient) -> {
		ftpClient.setBufferSize(1024 * 1024); //设置bufferSize
		ftpClient.setConnectTimeout(6000);
		ftpClient.setDefaultTimeout(0);
		ftpClient.setDataTimeout(0); //数据传输永不超时
	};
	
	/**
	 * 创建FTP客户端
	 * 
	 * @param ftpHost	- FTP服务器host地址
	 * @param ftpPort	- FTP服务器端口，默认21
	 * @param username	- FTP服务器登录用户名
	 * @param password	- FTP服务器登录密码
	 * @return
	 */
	public static FTPClient createFtpClient(String ftpHost, Integer ftpPort, String username, String password) {
		return createFtpClient(ftpHost, ftpPort, username, password, DEFAULT_FTPCLIENT_CONFIG);
	}

	/**
	 * 创建FTP客户端
	 * 
	 * @param ftpHost	- FTP服务器host地址
	 * @param ftpPort	- FTP服务器端口，默认21
	 * @param username	- FTP服务器登录用户名
	 * @param password	- FTP服务器登录密码
	 * @param customizer - FTP客户端配置自定义
	 * @return
	 */
	public static FTPClient createFtpClient(String ftpHost, Integer ftpPort, String username, String password, Consumer<FTPClient> customizer) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.setControlEncoding(GlobalConstants.DEFAULT_CHARSET);
			customizer.accept(ftpClient);
			ftpClient.connect(ftpHost, ftpPort); // 连接ftp服务器
			boolean success = ftpClient.login(username, password);// 登录ftp服务器
			Assert.isTrue(success, "登录FTP服务器出错!");
			int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
			Assert.isTrue(FTPReply.isPositiveCompletion(replyCode), "登录FTP服务器出错!");
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new FTPAccessException(e.getMessage(), e);
		}
		return ftpClient;
	}
	
	/**
	 * 上传文件到FTP服务器上的指定位置
	 * 
	 * @param ftpHost	- FTP服务器host地址
	 * @param ftpPort	- FTP服务器端口，默认21
	 * @param username	- FTP服务器登录用户名
	 * @param password	- FTP服务器登录密码
	 * @param inputStream			- 输入文件流
	 * @param remoteFullFileName	- 存储到远端的全路径文件名
	 * @return
	 */
	public static boolean uploadFile(String ftpHost, Integer ftpPort, String username, String password, InputStream inputStream, String remoteFullFileName) {
		return uploadFile(createFtpClient(ftpHost, ftpPort, username, password), inputStream, remoteFullFileName, true);
	}
	
	/**
	 * 上传文件到FTP服务器上的指定位置
	 * 
	 * @param ftpClient				- FTP客户端
	 * @param inputStream			- 输入文件流
	 * @param remoteFullFileName	- 存储到远端的全路径文件名
	 * @param releaseClientFinally 	- 是否释放ftpClient
	 * @return
	 */
	public static boolean uploadFile(FTPClient ftpClient, InputStream inputStream, String remoteFullFileName, boolean releaseClientFinally) {
		try (InputStream in = inputStream) {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); //设置上传文件的类型为二进制类型
			String fileDir = FileUtils.getFileDirectory(remoteFullFileName);
			createDirecrotyIfNecessary(ftpClient, fileDir); //如果目录不存在创建目录
			Assert.state(ftpClient.changeWorkingDirectory(fileDir), String.format("切换远程FTP目录失败: %s", fileDir));
			String fileName = FileUtils.getFileName(remoteFullFileName);
			return ftpClient.storeFile(fileName, in); //此处有可能上传错误,如果是553错误那么应该是已经存在目录了，但是这个目录没有写的权限
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new FTPAccessException(e.getMessage(), e);
		} finally {
			if(releaseClientFinally) {
				releaseFtpClient(ftpClient);
			}
		}
	}
	
	/**
	 * 上传文件到FTP服务器上的指定位置
	 * 
	 * @param ftpClient				- FTP客户端
	 * @param localFullFileName		- 本地待上传全路径文件名
	 * @param remoteFullFileName	- 存储到远端的全路径文件名
	 * @param releaseClientFinally 	- 是否释放ftpClient
	 * @return
	 */
	public static boolean uploadFile(FTPClient ftpClient, String localFullFileName, String remoteFullFileName, boolean releaseClientFinally) {
		try {
			return uploadFile(ftpClient, new FileInputStream(new File(localFullFileName)), remoteFullFileName, releaseClientFinally);
		} catch (IOException e) {
			throw new FTPAccessException(e.getMessage(), e);
		}
	}
	
	/**
	 * 将FTP服务器上的文件进行移动
	 * (注：此操作仅仅针对单个文件，而且是移动不是复制!!!)
	 * @param ftpClient					- FTP客户端
	 * @param srcRemoteFullFileName		- (源)远端文件的全路径文件名
	 * @param destRemoteFullFileName	- (终)远端文件的全路径文件名
	 * @param releaseClientFinally		- 是否释放ftpClient
	 * @return
	 */
	public static boolean moveFile(FTPClient ftpClient, String srcRemoteFullFileName, String destRemoteFullFileName, boolean releaseClientFinally) {
		try {
			String srcRemoteFilePath = FileUtils.getFileDirectory(srcRemoteFullFileName);
			String srcRemoteFileName = FileUtils.getFileName(srcRemoteFullFileName);
			
			Assert.state(ftpClient.changeWorkingDirectory(srcRemoteFilePath), String.format("切换远程FTP目录失败: %s", srcRemoteFilePath));
			
			FTPFile[] ftpFiles = ftpClient.listFiles();
			boolean srcRemoteFileExists = false;
			if(!ArrayUtils.isEmpty(ftpFiles)) {
				for(FTPFile file : ftpFiles) {
					if(file.getName().equals(srcRemoteFileName)) {
						srcRemoteFileExists = true;
					}
				}
			}
			Assert.state(srcRemoteFileExists, String.format("远端待移动文件不存在：%s", srcRemoteFullFileName));
			
			String destRemoteFilePath = FileUtils.getFileDirectory(destRemoteFullFileName);
			createDirecrotyIfNecessary(ftpClient, destRemoteFilePath);
			return ftpClient.rename(srcRemoteFullFileName, destRemoteFullFileName);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new FTPAccessException(e.getMessage(), e);
		}
	}

	/**
	 * 将FTP服务器上的文件进行移动
	 * (注：此操作仅仅针对单个文件，而且是移动不是复制!!!)
	 * @param ftpHost	- FTP服务器host地址
	 * @param ftpPort	- FTP服务器端口，默认21
	 * @param username	- FTP服务器登录用户名
	 * @param password	- FTP服务器登录密码
	 * @param srcRemoteFullFileName		- (源)远端文件的全路径文件名
	 * @param destRemoteFullFileName	- (终)远端文件的全路径文件名
	 * @return
	 */
	public static boolean moveFile(String ftpHost, Integer ftpPort, String username, String password, String srcRemoteFullFileName, String destRemoteFullFileName) {
		return moveFile(createFtpClient(ftpHost, ftpPort, username, password), srcRemoteFullFileName, destRemoteFullFileName, true);
	}

	/**
	 * 创建多层文件目录，如果FTP服务器已存在该目录，则不创建，如果无，则创建
	 * 
	 * @param ftpClient		- FTP客户端
	 * @param path			- 待创建的多层文件目录
	 * @return
	 * @throws IOException
	 */
	protected static void createDirecrotyIfNecessary(FTPClient ftpClient, String path) throws IOException {
		String directory = StringUtils.stripEnd(path, "/");
		if(!ftpClient.changeWorkingDirectory(directory)) {
			String parentPath = path.substring(0, path.lastIndexOf('/'));
			if(!StringUtils.isEmpty(parentPath)) {
				createDirecrotyIfNecessary(ftpClient, parentPath);
			}
			boolean success = ftpClient.makeDirectory(directory);
			if(!success) {
				LOGGER.error(">>> 创建目录失败：{}", directory);
			}
		}
	}

	/**
	 * 下载FTP服务器上的文件到本地
	 * 
	 * @param ftpHost	- FTP服务器host地址
	 * @param ftpPort	- FTP服务器端口，默认21
	 * @param username	- FTP服务器登录用户名
	 * @param password	- FTP服务器登录密码
	 * @param remoteFullFileName	- 远端待下载的全路径文件名
	 * @param localFullFileName		- 本地待存储的全路径文件名
	 * @return
	 */
	public static boolean downloadFile(String ftpHost, Integer ftpPort, String username, String password, String remoteFullFileName, String localFullFileName) {
		return downloadFile(createFtpClient(ftpHost, ftpPort, username, password), remoteFullFileName, localFullFileName, true);
	}
	
	/**
	 * 下载FTP服务器上的文件到本地
	 * @param ftpClient				- FTP客户端
	 * @param remoteFullFileName	- 远端待下载的全路径文件名
	 * @param localFullFileName		- 本地待存储的全路径文件名
	 * @param releaseClientFinally	- 是否释放ftpClient
	 * @return
	 */
	public static boolean downloadFile(FTPClient ftpClient, String remoteFullFileName, String localFullFileName, boolean releaseClientFinally) {
		try {
			String filePath = FileUtils.getFileDirectory(remoteFullFileName);
			String fileName = FileUtils.getFileName(remoteFullFileName);
			if(ftpClient.changeWorkingDirectory(filePath)) {
				FTPFile[] ftpFiles = ftpClient.listFiles();
				for (FTPFile file : ftpFiles) {
					if (fileName.equalsIgnoreCase(file.getName())) {
						try (FileOutputStream os = new FileOutputStream(new File(localFullFileName))){
							return ftpClient.retrieveFile(file.getName(), os);
						}
					}
				}
			}
			return false;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new FTPAccessException(e.getMessage(), e);
		} finally {
			if(releaseClientFinally) {
				releaseFtpClient(ftpClient);
			}
		}
	}
	
	/**
	 * 下载FTP服务器上的文件到指定文件输出流
	 * 
	 * @param ftpHost	- FTP服务器host地址
	 * @param ftpPort	- FTP服务器端口，默认21
	 * @param username	- FTP服务器登录用户名
	 * @param password	- FTP服务器登录密码
	 * @param remoteFullFileName	- 远端待下载的全路径文件名
	 * @param outputStream			- 文件输出流
	 * @return
	 */
	public static boolean downloadFile(String ftpHost, Integer ftpPort, String username, String password, String remoteFullFileName, OutputStream outputStream) {
		return downloadFile(createFtpClient(ftpHost, ftpPort, username, password), remoteFullFileName, outputStream, true);
	}
	
	/**
	 * 下载FTP服务器上的文件到指定文件输出流
	 * @param ftpClient				- FTP客户端
	 * @param remoteFullFileName	- 远端待下载的全路径文件名
	 * @param outputStream			- 文件输出流
	 * @param releaseClientFinally	- 是否释放ftpClient
	 * @return
	 */
	public static boolean downloadFile(FTPClient ftpClient, String remoteFullFileName, OutputStream outputStream, boolean releaseClientFinally) {
		try {
			String filePath = FileUtils.getFileDirectory(remoteFullFileName);
			String fileName = FileUtils.getFileName(remoteFullFileName);
			if(ftpClient.changeWorkingDirectory(filePath)) {
				FTPFile[] ftpFiles = ftpClient.listFiles();
				for (FTPFile file : ftpFiles) {
					if (fileName.equalsIgnoreCase(file.getName())) {
						return ftpClient.retrieveFile(file.getName(), outputStream);
					}
				}
			}
			return false;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new FTPAccessException(e.getMessage(), e);
		} finally {
			if(releaseClientFinally) {
				releaseFtpClient(ftpClient);
			}
		}
	}
	
	/**
	 * 下载FTP服务器上的文件到本地
	 * 
	 * @param ftpHost	- FTP服务器host地址
	 * @param ftpPort	- FTP服务器端口，默认21
	 * @param username	- FTP服务器登录用户名
	 * @param password	- FTP服务器登录密码
	 * @param remoteFullFileName	- 远端待删除的全路径文件名
	 * @return
	 */
	public static boolean deleteFile(String ftpHost, Integer ftpPort, String username, String password, String remoteFullFileName) {
		return deleteFile(createFtpClient(ftpHost, ftpPort, username, password), remoteFullFileName, true);
	}

	/**
	 * 删除FTP服务器上的文件
	 * @param ftpClient				- FTP客户端
	 * @param remoteFullFileName	- 远端待删除的全路径文件名
	 * @param releaseClientFinally	- 是否释放ftpClient
	 * @return
	 */
	public static boolean deleteFile(FTPClient ftpClient, String remoteFullFileName, boolean releaseClientFinally) {
		try {
			String filePath = FileUtils.getFileDirectory(remoteFullFileName);
			String fileName = FileUtils.getFileName(remoteFullFileName);
			if(ftpClient.changeWorkingDirectory(filePath)) { //切换FTP目录
				return ftpClient.deleteFile(fileName);
			}
			return false;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new FTPAccessException(e.getMessage(), e);
		} finally {
			if(releaseClientFinally) {
				releaseFtpClient(ftpClient);
			}
		}
	}
	
	/**
	 * 释放FTP客户端
	 * @param ftpClient
	 */
	public static void releaseFtpClient(FTPClient ftpClient) {
		try {
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		} catch (IOException ex) {}
	}
	
	public static class FTPAccessException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public FTPAccessException() {
			super();
		}

		public FTPAccessException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public FTPAccessException(String message, Throwable cause) {
			super(message, cause);
		}

		public FTPAccessException(String message) {
			super(message);
		}

		public FTPAccessException(Throwable cause) {
			super(cause);
		}
		
	}
}