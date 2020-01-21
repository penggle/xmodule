package com.penglecode.xmodule.common.web.support;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationFileUploadException;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;

/**
 * 默认的基于本机Nginx + 本机Servlet容器组建的最简文件服务器
 * 
 * 
 * @author 	pengpeng
 * @date	2018年4月18日 上午8:29:47
 */
public class DefaultXUploadFileTransfer implements XUploadFileHelper {

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
			String fileServerDir = GlobalConstants.GLOBAL_APP_CONFIG.getLocalFileServer().getBaseDirectory();
			String destFullFileName = FileUtils.normalizePath(fileServerDir + "/" + tempFileSavePath);
			FileUtils.mkDirIfNecessary(destFullFileName);
			uploadFile.transferTo(new File(destFullFileName));
			return new TransferResult(tempFileSavePath, destFullFileName, FileUtils.normalizePath(GlobalConstants.GLOBAL_APP_CONFIG.getFileServerUrl() + tempFileSavePath));
		} catch (Exception e) {
			throw new ApplicationFileUploadException(e.getMessage(), e);
		}
	}

	@Override
	public TransferResult storeTransfer(String tempFileSavePath, String fileStorePath) {
		try {
			String fileServerDir = GlobalConstants.GLOBAL_APP_CONFIG.getLocalFileServer().getBaseDirectory();
			String srcFullFileName = FileUtils.normalizePath(fileServerDir + "/" + tempFileSavePath);
			String storeUploadFileName = tempFileSavePath.replace(GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH, fileStorePath);
			String fileServerRootDir = GlobalConstants.GLOBAL_APP_CONFIG.getLocalFileServer().getBaseDirectory();
			String destFullFileName = FileUtils.normalizePath(fileServerRootDir + storeUploadFileName);
			FileUtils.mkDirIfNecessary(destFullFileName);
			FileUtils.copyFile(srcFullFileName, destFullFileName);
			return new TransferResult(storeUploadFileName, destFullFileName, FileUtils.normalizePath(GlobalConstants.GLOBAL_APP_CONFIG.getFileServerUrl() + storeUploadFileName));
		} catch (Exception e) {
			throw new ApplicationFileUploadException(e.getMessage(), e);
		}
	}

	@Override
	public void tempRemove(String fileSavePath) {
		String fileServerDir = GlobalConstants.GLOBAL_APP_CONFIG.getLocalFileServer().getBaseDirectory();
		String destFullFileName = FileUtils.normalizePath(fileServerDir + "/" + fileSavePath);
		FileUtils.deleteFileQuietly(destFullFileName);
	}

	@Override
	public void storeRemove(String fileSavePath) {
		String fileServerDir = GlobalConstants.GLOBAL_APP_CONFIG.getLocalFileServer().getBaseDirectory();
		String destFullFileName = FileUtils.normalizePath(fileServerDir + "/" + fileSavePath);
		FileUtils.deleteFileQuietly(destFullFileName);
	}
	
	@Scheduled(cron="0 0 2 * * ?")
	public void schedulingCleanTempUploads() {
		String fileServerDir = GlobalConstants.GLOBAL_APP_CONFIG.getLocalFileServer().getBaseDirectory();
		String tempSavePath = GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH;
		File tempSaveDir = new File(FileUtils.normalizePath(fileServerDir + "/" + tempSavePath));
		LocalDate today = LocalDate.now();
		if(tempSaveDir.exists() && tempSaveDir.isDirectory()) {
			tempSaveDir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					boolean matched = false;
					String filename = file.getName();
					if(file.isDirectory() && filename.matches("\\d{8}")) {
						try {
							LocalDate tempDate = DateTimeUtils.parse2DateTime(filename, "yyyyMMdd").toLocalDate();
							if(tempDate.isBefore(today)) {
								matched = true;
								FileUtils.deleteDirectory(file);
							}
						} catch (Exception e) {
						}
					}
					return matched;
				}
			});
		}
	}
	
}