package com.penglecode.xmodule.common.web.support;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.FileUtils;

/**
 * 默认的基于本机Nginx + 本机Servlet容器组建的最简文件服务器的文件服务
 * 
 * 
 * @author 	pengpeng
 * @date	2018年4月18日 上午8:29:47
 */
public class DefaultSmartFileServerService extends CommonSmartFileServerService {

	@Override
	public String getFileServerBaseDirectory() {
		return GlobalConstants.GLOBAL_APP_CONFIG.getLocalFileServer().getBaseDirectory();
	}
	
	@Override
	public String getServerStoreFileLocation(String serverStoreFilePath, AccessStrategy accessStrategy) {
		String fileServerRootDir = getFileServerBaseDirectory();
		return FileUtils.normalizePath(fileServerRootDir + "/" + accessStrategy.forPath(serverStoreFilePath));
	}

	@Override
	public Resource getServerStoreFileResource(String serverStoreFilePath, AccessStrategy accessStrategy) {
		return new FileSystemResource(new File(getServerStoreFileLocation(serverStoreFilePath, accessStrategy)));
	}

	@Override
	public TransferResult transferServerStoreFile(String localTempFilePath, String serverStoreFilePath,
			boolean deleteTempFile, AccessStrategy accessStrategy) throws IOException {
		String fileServerRootDir = getFileServerBaseDirectory();
		String srcFullFileName = getLocalTempFileLocation(localTempFilePath, accessStrategy);
		String storeUploadFileName = localTempFilePath.replace(GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH, serverStoreFilePath);
		String destFullFileName = FileUtils.normalizePath(fileServerRootDir + "/" + accessStrategy.forPath(storeUploadFileName));
		FileUtils.mkDirIfNecessary(destFullFileName);
		FileUtils.copyFile(srcFullFileName, destFullFileName);
		if(deleteTempFile) {
			FileUtils.deleteFileQuietly(srcFullFileName);
		}
		return new TransferResult(storeUploadFileName, destFullFileName, FileUtils.normalizePath(GlobalConstants.GLOBAL_APP_CONFIG.getFileServerUrl() + storeUploadFileName));
	}

	@Override
	public void downloadServerStoreFile(String serverStoreFilePath, OutputStream outputStream,
			AccessStrategy accessStrategy) throws IOException {
		File storeFile = new File(getServerStoreFileLocation(serverStoreFilePath, accessStrategy));
		Files.copy(storeFile.toPath(), outputStream);
	}

	@Override
	public void removeServerStoreFile(String serverStoreFilePath, AccessStrategy accessStrategy) {
		String destFullFileName = getServerStoreFileLocation(serverStoreFilePath, accessStrategy);
		FileUtils.deleteFileQuietly(destFullFileName);
	}

}