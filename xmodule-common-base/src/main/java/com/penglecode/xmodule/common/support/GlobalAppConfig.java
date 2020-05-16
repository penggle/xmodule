package com.penglecode.xmodule.common.support;

/**
 * 全局应用配置
 * 
 * @author 	pengpeng
 * @date	2019年6月11日 上午10:56:48
 */
public class GlobalAppConfig {

	/** 业务ID */
	private Integer businessId = 1;
	
	/** 某个业务的某个实例ID */
	private Integer instanceId = 1;
	
	/** 整个应用的web服务url(例如基于Nginx)，例如: http://127.0.0.1 */
	private String webServerUrl;
	
	/** 整个应用的静态文件服务url(例如基于Nginx)，例如: http://127.0.0.1/static */
	private String fileServerUrl;
	
	/** 上传文件的本地临时存储目录,默认为Servlet应用的webapp的根: / */
	private String localTempBaseDirectory = "/";
	
	/** 基于本地文件服务器(单机模式: 本机上Nginx + 本机上Servlet容器), 用于#DefaultXUploadFileHelper的实现 */
	private LocalFileServer localFileServer;
	
	/** 基于独立文件服务器(例如vsftpd + Nginx/Apache搭建的文件服务器), 用于#FtpXUploadFileHelper的实现 */
	private FtpFileServer ftpFileServer;
	
	public Integer getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	public String getWebServerUrl() {
		return webServerUrl;
	}

	public void setWebServerUrl(String webServerUrl) {
		this.webServerUrl = webServerUrl;
	}

	public String getFileServerUrl() {
		return fileServerUrl;
	}

	public void setFileServerUrl(String fileServerUrl) {
		this.fileServerUrl = fileServerUrl;
	}

	public String getLocalTempBaseDirectory() {
		return localTempBaseDirectory;
	}

	public void setLocalTempBaseDirectory(String localTempBaseDirectory) {
		this.localTempBaseDirectory = localTempBaseDirectory;
	}

	public LocalFileServer getLocalFileServer() {
		return localFileServer;
	}

	public void setLocalFileServer(LocalFileServer localFileServer) {
		this.localFileServer = localFileServer;
	}

	public FtpFileServer getFtpFileServer() {
		return ftpFileServer;
	}

	public void setFtpFileServer(FtpFileServer ftpFileServer) {
		this.ftpFileServer = ftpFileServer;
	}

	/**
	 * 本地文件服务器配置
	 * 
	 * @author 	pengpeng
	 * @date	2019年12月29日 下午9:19:47
	 */
	public static class LocalFileServer {
		
		/**
		 * 文件存储的基础目录
		 */
		private String baseDirectory;

		public String getBaseDirectory() {
			return baseDirectory;
		}

		public void setBaseDirectory(String baseDirectory) {
			this.baseDirectory = baseDirectory;
		}
		
	}
	
	/**
	 * 基于单独的传统FTP文件服务器，例如vsftpd + Nginx搭建的文件服务器
	 * 
	 * @author 	pengpeng
	 * @date	2019年12月29日 下午9:24:55
	 */
	public static class FtpFileServer extends LocalFileServer {
		
		/**
		 * FTP服务器的host
		 */
		private String host;
		
		/**
		 * FTP服务器的端口号
		 */
		private int port = 21;
		
		/**
		 * FTP服务器登录用户名
		 */
		private String username;
		
		/**
		 * FTP服务器登录密码
		 */
		private String password;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
	}
	
}
