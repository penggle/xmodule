package com.penglecode.xmodule.ssltlscerts.util;

import java.io.File;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * SSLSocketFactory的静态工厂类
 * 
 * @author 	pengpeng
 * @date	2019年11月20日 下午2:53:46
 */
public class SSLSocketFactoryUtils {

	/**
	 * 默认内存临时KeyStore密码
	 */
	private static final String DEFAULT_KEYSTORE_PASSWORD = "";
	
	/**
	 * 默认的SSL/TLS版本
	 */
	private static final String DEFAULT_TLS_VERSION = "TLSv1.2";
	
	public static SSLSocketFactory createSSLSocketFactory(File caCertPemFile, File clientCertPemFile,
			File clientKeyPemFile)
			throws Exception {
		return createSSLSocketFactory(caCertPemFile, clientCertPemFile, clientKeyPemFile, DEFAULT_KEYSTORE_PASSWORD, DEFAULT_TLS_VERSION);
	}
	
	/**
	 * 创建SSLSocketFactory
	 * 
	 * @param caCertPemFile			- CA证书的PEM格式存储文件
	 * @param clientCertPemFile		- 客户端证书的PEM格式存储文件
	 * @param clientKeyPemFile		- 客户端私钥的PEM格式存储文件
	 * @param password				- KeyStore密码(由于是临时内存创建KeyStore,密码即定即用,形同虚设)
	 * @param tlsVersion			- SSL/TLS版本
	 * @return
	 * @throws Exception
	 */
	public static SSLSocketFactory createSSLSocketFactory(File caCertPemFile, File clientCertPemFile,
			File clientKeyPemFile, String password, String tlsVersion)
			throws Exception {
		
		X509Certificate caCert = X509CertificateUtils.readCertificateFromPemFile(caCertPemFile);
		X509Certificate clientCert = X509CertificateUtils.readCertificateFromPemFile(clientCertPemFile);
		PrivateKey clientKey = X509CertificateUtils.readPrivateKeyFromPemFile(clientKeyPemFile);
		
		/**
		 * 在内存中临时创建CA证书的KeyStore
		 */
        KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        caKeyStore.load(null, null);
        caKeyStore.setCertificateEntry("ca-certificate", caCert);
        
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(caKeyStore);
        
        /**
         * 客户端密钥和证书被发送到服务器，以便服务器可以对客户端进行身份验证
         */
        KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        clientKeyStore.load(null, null);
        clientKeyStore.setCertificateEntry("certificate", clientCert);
        clientKeyStore.setKeyEntry("private-key", clientKey, password.toCharArray(), new Certificate[]{clientCert});

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, password.toCharArray());
 
        /**
         * 创建SSLContext
         */
        SSLContext context = SSLContext.getInstance(tlsVersion);
        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        /**
         * Return the newly created socket factory object
         */
        return context.getSocketFactory();
	}

}
