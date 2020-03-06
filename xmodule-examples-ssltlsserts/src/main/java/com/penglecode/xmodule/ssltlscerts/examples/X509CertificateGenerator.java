package com.penglecode.xmodule.ssltlscerts.examples;

import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.X500Name;

import com.penglecode.xmodule.ssltlscerts.util.X509CertificateUtils;

/**
 * EMQTT X509Certificate证书生成器
 * 
 * @author 	pengpeng
 * @date	2019年11月20日 下午1:43:44
 */
public class X509CertificateGenerator {

	/**
	 * 证书统一生成保存目录
	 */
	private final Path certsPath;
	
	private String caKeyFileName = "root-ca.key";
	
	private String caCertFileName = "root-ca.pem";
	
	private String serverKeyFileName = "server.key";
	
	private String serverCertFileName = "server.pem";
	
	private String clientKeyFileName = "client.key";
	
	private String clientCertFileName = "client.pem";
	
	/**
	 * CA证书的有效期
	 */
	private int caCertValidityDays = 365 * 10;
	
	/**
	 * server证书的有效期
	 */
	private int serverCertValidityDays = 365 * 10;
	
	/**
	 * client证书的有效期
	 */
	private int clientCertValidityDays = 365;
	
	/**
	 * 除了CN之外的Subject
	 */
	private String noCNSubjects = "OU=CertusNet,O=IOT,C=CN,ST=JiangSu,L=NanJing";
	
	/**
	 * CA证书的CN (名称)，一般填IP地址、域名、或者名称
	 */
	private final String caSubjectCN;
	
	/**
	 * server证书的CN (名称)，一般填IP地址、域名、或者名称
	 */
	private final String serverSubjectCN;
	
	/**
	 * client证书的CN (名称)，一般填IP地址、域名、或者名称
	 */
	private final String clientSubjectCN;
	
	public X509CertificateGenerator(Path certsPath, String caSubjectCN, String serverSubjectCN,
			String clientSubjectCN) {
		super();
		this.certsPath = certsPath;
		this.caSubjectCN = caSubjectCN;
		this.serverSubjectCN = serverSubjectCN;
		this.clientSubjectCN = clientSubjectCN;
	}

	/**
	 * 创建自签名的根证书，根证书全局用一份，后面服务端、客户端的证书都由它来签名生成
	 * @throws Exception
	 */
	protected void createCACert() throws Exception {
		//创建根证书的秘钥对
		KeyPair keyPair = X509CertificateUtils.generateKeyPair(X509CertificateUtils.DEFAULT_KEY_SIZE);
		PrivateKey privateKey = keyPair.getPrivate();
		System.out.println("<---------------------生成根证书的签名私钥(PrivateKey)--------------------->");
		System.out.println(privateKey);
		//保存根证书的私钥
		X509CertificateUtils.writePrivateKeyToPemFile(privateKey, certsPath.resolve(caKeyFileName).toFile());
		
		X500Name subject = new X500Name("CN=" + caSubjectCN + "," + noCNSubjects);
		X509Certificate cert = X509CertificateUtils.generateSelfSignedCertificate(subject, keyPair, caCertValidityDays);
		System.out.println("<---------------------生成根证书(Certificate)--------------------->");
		System.out.println(cert);
		//保存根证书
		X509CertificateUtils.writeCertificateToPemFile(cert, certsPath.resolve(caCertFileName).toFile());
	}
	
	/**
	 * 创建服务端的证书
	 * @throws Exception
	 */
	protected void createServerCert() throws Exception {
		//创建证书的秘钥对
		KeyPair keyPair = X509CertificateUtils.generateKeyPair(X509CertificateUtils.DEFAULT_KEY_SIZE);
		PrivateKey privateKey = keyPair.getPrivate();
		System.out.println("<---------------------生成服务端证书的签名私钥(PrivateKey)--------------------->");
		System.out.println(privateKey);
		//保存证书的私钥
		X509CertificateUtils.writePrivateKeyToPemFile(privateKey, certsPath.resolve(serverKeyFileName).toFile());
		
		PrivateKey caPrivateKey = X509CertificateUtils.readPrivateKeyFromPemFile(certsPath.resolve(caKeyFileName).toFile());
		PublicKey caPublicKey = X509CertificateUtils.generatePublicKeyByPrivateKey(caPrivateKey);
		
		KeyPair issuerKeyPair = new KeyPair(caPublicKey, caPrivateKey);
		X500Name subject = new X500Name("CN=" + serverSubjectCN + "," + noCNSubjects);
		X500Name issuer = new X500Name("CN=" + caSubjectCN + "," + noCNSubjects);
		
		//生成证书
		X509Certificate cert = X509CertificateUtils.generateCertificate(subject, keyPair.getPublic(), issuer, issuerKeyPair, serverCertValidityDays);
		System.out.println("<---------------------生成服务端证书(Certificate)--------------------->");
		System.out.println(cert);
		//保存证书
		X509CertificateUtils.writeCertificateToPemFile(cert, certsPath.resolve(serverCertFileName).toFile());
	}
	
	protected void createClientCert() throws Exception {
		//创建证书的秘钥对
		KeyPair keyPair = X509CertificateUtils.generateKeyPair(X509CertificateUtils.DEFAULT_KEY_SIZE);
		PrivateKey privateKey = keyPair.getPrivate();
		System.out.println("<---------------------生成客户端证书的签名私钥(PrivateKey)--------------------->");
		System.out.println(privateKey);
		//保存证书的私钥
		X509CertificateUtils.writePrivateKeyToPemFile(privateKey, certsPath.resolve(clientKeyFileName).toFile());
		
		PrivateKey caPrivateKey = X509CertificateUtils.readPrivateKeyFromPemFile(certsPath.resolve(caKeyFileName).toFile());
		PublicKey caPublicKey = X509CertificateUtils.generatePublicKeyByPrivateKey(caPrivateKey);
		
		KeyPair issuerKeyPair = new KeyPair(caPublicKey, caPrivateKey);
		X500Name subject = new X500Name("CN=" + clientSubjectCN + "," + noCNSubjects);
		X500Name issuer = new X500Name("CN=" + caSubjectCN + "," + noCNSubjects);
		
		//生成证书
		X509Certificate cert = X509CertificateUtils.generateCertificate(subject, keyPair.getPublic(), issuer, issuerKeyPair, clientCertValidityDays);
		System.out.println("<---------------------生成客户端证书(Certificate)--------------------->");
		System.out.println(cert);
		//保存证书
		X509CertificateUtils.writeCertificateToPemFile(cert, certsPath.resolve(clientCertFileName).toFile());
	}
	
	public void generateAll() throws Exception {
		createCACert();
		createServerCert();
		createClientCert();
	}
	
	public String getCaKeyFileName() {
		return caKeyFileName;
	}

	public void setCaKeyFileName(String caKeyFileName) {
		this.caKeyFileName = caKeyFileName;
	}

	public String getCaCertFileName() {
		return caCertFileName;
	}

	public void setCaCertFileName(String caCertFileName) {
		this.caCertFileName = caCertFileName;
	}

	public String getServerKeyFileName() {
		return serverKeyFileName;
	}

	public void setServerKeyFileName(String serverKeyFileName) {
		this.serverKeyFileName = serverKeyFileName;
	}

	public String getServerCertFileName() {
		return serverCertFileName;
	}

	public void setServerCertFileName(String serverCertFileName) {
		this.serverCertFileName = serverCertFileName;
	}

	public String getClientKeyFileName() {
		return clientKeyFileName;
	}

	public void setClientKeyFileName(String clientKeyFileName) {
		this.clientKeyFileName = clientKeyFileName;
	}

	public String getClientCertFileName() {
		return clientCertFileName;
	}

	public void setClientCertFileName(String clientCertFileName) {
		this.clientCertFileName = clientCertFileName;
	}

	public Path getCertsPath() {
		return certsPath;
	}

	public String getCaSubjectCN() {
		return caSubjectCN;
	}

	public String getServerSubjectCN() {
		return serverSubjectCN;
	}

	public String getClientSubjectCN() {
		return clientSubjectCN;
	}
	
}
