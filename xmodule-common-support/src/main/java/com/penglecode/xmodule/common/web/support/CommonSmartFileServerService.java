package com.penglecode.xmodule.common.web.support;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDate;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.FileUtils;

/**
 * 公共的小型文件服务器文件服务基类
 * 
 * 该类将刚上传的文件临时保存在Servlet容器所在机器（以下称为本机）
 * 上传过后的临时文件若要通过HTTP协议暴露给前端，则HTTP请求实际走的是Servlet容器，此时请注意Nginx的负载均衡配置一定要是ip_hash
 * 否则会出现刚上传的文件却找不到的问题
 * 
 * @author 	pengpeng
 * @date	2018年4月18日 上午8:29:47
 */
public abstract class CommonSmartFileServerService implements SmartFileServerService, InitializingBean {

	private String localTempBaseDirectory;
	
	@Override
	public String getLocalTempBaseDirectory() {
		Assert.hasText(localTempBaseDirectory, "Property 'localTempBaseDirectory' is not initialized yet!");
		return localTempBaseDirectory;
	}

	@Override
	public String createLocalTempFilePath(String originalFileName, RenameStrategy renameStrategy) {
		//重命名上传后的文件名
        String renamedFileName = renameStrategy.rename(originalFileName);
        String tempFileSavePath = GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH + "/" + DateTimeUtils.formatNow("yyyyMMdd") + "/" + renamedFileName;
        return FileUtils.normalizePath(tempFileSavePath);
	}

	@Override
	public String getLocalTempFileLocation(String localTempFilePath, AccessStrategy accessStrategy) {
		return FileUtils.normalizePath(getLocalTempBaseDirectory() + "/" + localTempFilePath);
	}

	@Override
	public Resource getLocalTempFileResource(String localTempFilePath, AccessStrategy accessStrategy) {
		return new FileSystemResource(new File(getLocalTempFileLocation(localTempFilePath, accessStrategy)));
	}

	@Override
	public TransferResult transferLocalTempFile(MultipartFile uploadFile, String localTempFilePath,
			AccessStrategy accessStrategy) throws IOException {
		String destFullFileName = getLocalTempFileLocation(localTempFilePath, accessStrategy);
		FileUtils.mkDirIfNecessary(destFullFileName);
		uploadFile.transferTo(new File(destFullFileName));
		return new TransferResult(localTempFilePath, destFullFileName, FileUtils.normalizePath(GlobalConstants.GLOBAL_APP_CONFIG.getWebServerUrl() + localTempFilePath));
	}

	@Override
	public void downloadLocalTempFile(String localTempFilePath, OutputStream outputStream,
			AccessStrategy accessStrategy) throws IOException {
		File tempFile = new File(getLocalTempFileLocation(localTempFilePath, accessStrategy));
		Files.copy(tempFile.toPath(), outputStream);
	}

	@Override
	public void removeLocalTempFile(String localTempFilePath, AccessStrategy accessStrategy) {
		String destFullFileName = getLocalTempFileLocation(localTempFilePath, accessStrategy);
		FileUtils.deleteFileQuietly(destFullFileName);
	}

	@Scheduled(cron="0 0 2 * * ?")
	public void schedulingCleanTempUploads() {
		String tempSavePath = GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH;
		File tempSaveDir = new File(FileUtils.normalizePath(localTempBaseDirectory + "/" + tempSavePath));
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
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String localTempLocation = GlobalConstants.GLOBAL_APP_CONFIG.getLocalTempBaseDirectory();
		Resource localTempResource = ApplicationConstants.DEFAULT_RESOURCE_PATTERN_RESOLVER.get().getResource(localTempLocation);
		this.localTempBaseDirectory = FileUtils.normalizePath(localTempResource.getFile().getPath());
	}

}