package com.penglecode.xmodule.common.web.support;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.GlobalAppConfig.FtpFileServer;
import com.penglecode.xmodule.common.util.FTPUtils;
import com.penglecode.xmodule.common.util.FileUtils;

/**
 * 基于独立文件服务器(例如vsftpd + Nginx/Apache搭建的文件服务器)的通用小文件上传助手
 * 
 * vsftpd + Nginx搭建文件服务器: https://www.jianshu.com/p/af2be63b796e
 * 
 * @author 	pengpeng
 * @date	2018年4月18日 上午8:29:47
 */
public class FtpSmartFileServerService extends CommonSmartFileServerService {

	@Override
	public String getFileServerBaseDirectory() {
		return GlobalConstants.GLOBAL_APP_CONFIG.getFtpFileServer().getBaseDirectory();
	}
	
	@Override
	public String getServerStoreFileLocation(String serverStoreFilePath, AccessStrategy accessStrategy) {
		String fileServerRootDir = getFileServerBaseDirectory();
		return FileUtils.normalizePath(fileServerRootDir + "/" + accessStrategy.forPath(serverStoreFilePath));
	}

	@Override
	public Resource getServerStoreFileResource(String serverStoreFilePath, AccessStrategy accessStrategy) {
		throw new UnsupportedOperationException("Not Supported Yet");
	}

	@Override
	public TransferResult transferServerStoreFile(String localTempFilePath, String serverStoreFilePath,
			boolean deleteTempFile, AccessStrategy accessStrategy) throws IOException {
		FtpFileServer ftpFileServer = GlobalConstants.GLOBAL_APP_CONFIG.getFtpFileServer();
		String fileServerRootDir = getFileServerBaseDirectory();
		String srcLocalFullFileName = getLocalTempFileLocation(localTempFilePath, accessStrategy);
		String storeUploadFileName = localTempFilePath.replace(GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH, serverStoreFilePath);
		String destRemoteFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + accessStrategy.forPath(storeUploadFileName));
		
		boolean success = FTPUtils.uploadFile(ftpFileServer.getHost(), ftpFileServer.getPort(), ftpFileServer.getUsername(), ftpFileServer.getPassword(), new FileInputStream(srcLocalFullFileName), destRemoteFullFileName);
		Assert.state(success, "FTP移动文件失败!");
		if(deleteTempFile) {
			try {
				FileUtils.deleteFileQuietly(srcLocalFullFileName);
			} catch (Exception e) {}
		}
		return new TransferResult(storeUploadFileName, destRemoteFullFileName, FileUtils.normalizePath(GlobalConstants.GLOBAL_APP_CONFIG.getFileServerUrl() + storeUploadFileName));
	}

	@Override
	public void downloadServerStoreFile(String serverStoreFilePath, OutputStream outputStream,
			AccessStrategy accessStrategy) throws IOException {
		FtpFileServer ftpFileServer = GlobalConstants.GLOBAL_APP_CONFIG.getFtpFileServer();
		String fileServerRootDir = getFileServerBaseDirectory();
		String destFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + accessStrategy.forPath(serverStoreFilePath));
		boolean success = FTPUtils.downloadFile(ftpFileServer.getHost(), ftpFileServer.getPort(), ftpFileServer.getUsername(), ftpFileServer.getPassword(), destFullFileName, outputStream);
		Assert.state(success, "FTP下载文件失败!");
	}

	@Override
	public void removeServerStoreFile(String serverStoreFilePath, AccessStrategy accessStrategy) {
		FtpFileServer ftpFileServer = GlobalConstants.GLOBAL_APP_CONFIG.getFtpFileServer();
		String fileServerRootDir = getFileServerBaseDirectory();
		String remoteFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + accessStrategy.forPath(serverStoreFilePath));
		FTPUtils.deleteFile(ftpFileServer.getHost(), ftpFileServer.getPort(), ftpFileServer.getUsername(), ftpFileServer.getPassword(), remoteFullFileName);
	}
	
}
