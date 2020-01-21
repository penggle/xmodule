package com.penglecode.xmodule.common.web.support;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationFileUploadException;
import com.penglecode.xmodule.common.support.GlobalAppConfig.FtpFileServer;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.FTPUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;

/**
 * 基于独立文件服务器(例如vsftpd + Nginx/Apache搭建的文件服务器)的通用小文件上传助手
 * 
 * vsftpd + Nginx搭建文件服务器: https://www.jianshu.com/p/af2be63b796e
 * 
 * @author 	pengpeng
 * @date	2018年4月18日 上午8:29:47
 */
public class FtpXUploadFileHelper implements XUploadFileHelper {

	@Override
	public String createRenamedFileName(String originalFileName) {
		//重命名上传后的文件名
        String renamedFileName = UUIDUtils.uuid() + originalFileName.substring(originalFileName.lastIndexOf('.'));
        String tempFileSavePath = GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH + "/" + DateTimeUtils.formatNow("yyyyMMdd") + "/" + renamedFileName;
        return FileUtils.normalizePath(tempFileSavePath);
	}
	
	@Override
	public TransferResult tempTransfer(MultipartFile uploadFile, String tempFileSavePath)  {
		try {
			FtpFileServer ftpFileServer = GlobalConstants.GLOBAL_APP_CONFIG.getFtpFileServer();
			String fileServerRootDir = ftpFileServer.getBaseDirectory();
			String destFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + tempFileSavePath);
			boolean success = FTPUtils.uploadFile(ftpFileServer.getHost(), ftpFileServer.getPort(), ftpFileServer.getUsername(), ftpFileServer.getPassword(), uploadFile.getInputStream(), destFullFileName);
			Assert.state(success, "FTP上传文件失败!");
			return new TransferResult(tempFileSavePath, destFullFileName, FileUtils.normalizePath(GlobalConstants.GLOBAL_APP_CONFIG.getFileServerUrl() + tempFileSavePath));
		} catch (Exception e) {
			throw new ApplicationFileUploadException(e.getMessage(), e);
		}
	}

	@Override
	public TransferResult storeTransfer(String tempFileSavePath, String fileStorePath) {
		try {
			FtpFileServer ftpFileServer = GlobalConstants.GLOBAL_APP_CONFIG.getFtpFileServer();
			String fileServerRootDir = ftpFileServer.getBaseDirectory();
			String srcRemoteFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + tempFileSavePath);
			String storeUploadFileName = tempFileSavePath.replace(GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH, fileStorePath);
			String destRemoteFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + storeUploadFileName);
			boolean success = FTPUtils.moveFile(ftpFileServer.getHost(), ftpFileServer.getPort(), ftpFileServer.getUsername(), ftpFileServer.getPassword(), srcRemoteFullFileName, destRemoteFullFileName);
			Assert.state(success, "FTP移动文件失败!");
			return new TransferResult(storeUploadFileName, destRemoteFullFileName, FileUtils.normalizePath(GlobalConstants.GLOBAL_APP_CONFIG.getFileServerUrl() + storeUploadFileName));
		} catch (Exception e) {
			throw new ApplicationFileUploadException(e.getMessage(), e);
		}
	}

	@Override
	public void tempRemove(String fileSavePath) {
		removeRemoteFtpFile(fileSavePath);
	}

	@Override
	public void storeRemove(String fileSavePath) {
		removeRemoteFtpFile(fileSavePath);
	}
	
	protected void removeRemoteFtpFile(String fileSavePath) {
		FtpFileServer ftpFileServer = GlobalConstants.GLOBAL_APP_CONFIG.getFtpFileServer();
		String fileServerRootDir = ftpFileServer.getBaseDirectory();
		String remoteFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + fileSavePath);
		FTPUtils.deleteFile(ftpFileServer.getHost(), ftpFileServer.getPort(), ftpFileServer.getUsername(), ftpFileServer.getPassword(), remoteFullFileName);
	}
	
}
