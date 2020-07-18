package com.penglecode.xmodule.ssltlscerts.util;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.util.Assert;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * SSL/TLS认证X509 CA证书生成工具类
 * 
 * 基于RSA非对称加密算法
 * 
 * @author 	pengpeng
 * @date	2019年11月16日 上午9:26:09
 */
public class X509CertificateUtils {
	
	/** 默认的Provider: BouncyCastleProvider */
	public static final String DEFAULT_PROVIDER = "BC";
	
	static {
        if (Security.getProvider(DEFAULT_PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }
	
	/** 默认的公私秘钥对生成长度(越长越安全，RSA要求不能小于512), 2048 */
	public static final int DEFAULT_KEY_SIZE = 2048;
	
	/** 默认的公私秘钥对生成算法, RSA */
	public static final String DEFAULT_KEY_ALGORITHM = "RSA";
	
	/** 默认的证书类型, X.509 */
	public static final String DEFAULT_CERT_TYPE = "X.509";
	
	/** 默认的证书有效期(天数) 365 */
	public static final int DEFAULT_CERT_VALIDITY_DAYS = 365;
	
	/** 默认的证书签名算法 */
	public static final String DEFAULT_CERT_SIGN_ALGO = "SHA256withRSA";
	
	/**
	 * 生成公私秘钥对
	 * @param keySize			- 私钥长度，越长越安全，RSA要求不能小于512，e.g. 2048
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static KeyPair generateKeyPair(Integer keySize) throws GeneralSecurityException {
		Assert.isTrue(keySize >= 512, "RSA key size must be no less than 512");
		//产生公私钥对
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(DEFAULT_KEY_ALGORITHM, DEFAULT_PROVIDER);
        kpg.initialize(DEFAULT_KEY_SIZE);
        return kpg.generateKeyPair();
	}
	
	/**
	 * 据私钥生成公钥
	 * @param privateKey	- 私钥对象
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static PublicKey generatePublicKeyByPrivateKey(PrivateKey privateKey) throws GeneralSecurityException {
		Assert.notNull(privateKey, "Parameter 'privateKey' must be required!");
		KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_KEY_ALGORITHM, DEFAULT_PROVIDER);
		BCRSAPrivateCrtKey privKey = (BCRSAPrivateCrtKey) privateKey;
		RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(privKey.getModulus(), privKey.getPublicExponent());
		return keyFactory.generatePublic(pubSpec);
	}
	
	/**
	 * 将私钥以PEM格式写到指定文件中
	 * @param privateKey		- 私钥对象
	 * @param keyPemFile		- 私钥保存文件
	 */
	public static void writePrivateKeyToPemFile(PrivateKey privateKey, File keyPemFile) throws IOException {
		Assert.notNull(privateKey, "Parameter 'privateKey' must be required!");
		Assert.notNull(keyPemFile, "Parameter 'keyPemFile' must be required!");
		try (PemWriter writer = new PemWriter(new FileWriter(keyPemFile))) {
	        writer.writeObject(new PemObject("PRIVATE KEY", privateKey.getEncoded()));
	    }
	}
	
	/**
	 * 从指定PEM格式的私钥文件中读出私钥对象
	 * @param keyPemFile
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static PrivateKey readPrivateKeyFromPemFile(File keyPemFile) throws IOException, GeneralSecurityException {
		Assert.notNull(keyPemFile, "Parameter 'keyPemFile' must be required!");
		try (PemReader reader = new PemReader(new FileReader(keyPemFile))) {
			PemObject pemObject = reader.readPemObject();
			byte[] pemContent = pemObject.getContent();
			KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_KEY_ALGORITHM, DEFAULT_PROVIDER);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemContent);
	        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
	        return privateKey;
	    }
	}
	
	/**
	 * 生成签名证书
	 * (注：如果subject.equals(issuer) && subjectPublicKey.equals(issuerKeyPair.getPublic())成立则认为是自签名)
	 * 
	 * @param subject				- 证书所有者信息描述，e.g. CN=127.0.0.1,OU=CloudIOT,O=CertusNet,L=NanJing,S=JiangSu,C=CN
	 * @param subjectPublicKey		- 证书所有者公钥
	 * @param issuer				- 证书发行者信息描述，e.g. CN=127.0.0.1,OU=CloudIOT,O=CertusNet,L=NanJing,S=JiangSu,C=CN
	 * @param issuerKeyPair			- 证书发行者秘钥对
	 * @param validityDays			- 证书有效期天数，e.g. 3650即10年
	 * @return						- 返回生成的CA证书
	 * @throws IOException, GeneralSecurityException, OperatorCreationException 
	 */
	public static X509Certificate generateCertificate(X500Name subject, PublicKey subjectPublicKey, X500Name issuer, KeyPair issuerKeyPair, Integer validityDays) throws IOException, GeneralSecurityException, OperatorCreationException {
		validityDays = validityDays == null ? DEFAULT_CERT_VALIDITY_DAYS : validityDays;
		Assert.notNull(subject, "Parameter 'subject' must be required!");
		Assert.notNull(subjectPublicKey, "Parameter 'subjectPublicKey' must be required!");
		Assert.notNull(issuer, "Parameter 'issuer' must be required!");
		Assert.notNull(issuerKeyPair, "Parameter 'issuerKeyPair' must be required!");
		Assert.isTrue(validityDays > 0, "Certificate validity (days) must be more then 0");
		LocalDateTime nowTime = LocalDateTime.now();
		long nowTimestamp = nowTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); //当前时间戳
        BigInteger certSerialNumber = new BigInteger(Long.toString(nowTimestamp)); //以当前时间戳生成证书序列号
        
        Date validityStart = Date.from(nowTime.atZone(ZoneId.systemDefault()).toInstant()); //证书有效期开始时间
        Date validityEnd = Date.from(nowTime.plusDays(validityDays).atZone(ZoneId.systemDefault()).toInstant()); //证书有效期结束时间
        
        boolean selfSign = subject.equals(issuer) && subjectPublicKey.equals(issuerKeyPair.getPublic()); //是否是自签名?
        
        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(issuer, certSerialNumber, validityStart, validityEnd, subject, subjectPublicKey);
        
        certBuilder.addExtension(Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(subjectPublicKey));
        certBuilder.addExtension(Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(issuerKeyPair.getPublic()));
        certBuilder.addExtension(Extension.basicConstraints, false, new BasicConstraints(selfSign));
        certBuilder.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.dataEncipherment | KeyUsage.keyCertSign | KeyUsage.cRLSign));
        
        ContentSigner contentSigner = new JcaContentSignerBuilder(DEFAULT_CERT_SIGN_ALGO).setProvider(DEFAULT_PROVIDER).build(issuerKeyPair.getPrivate());
        X509CertificateHolder certificateHolder = certBuilder.build(contentSigner);

        X509Certificate cert = new JcaX509CertificateConverter().setProvider(DEFAULT_PROVIDER).getCertificate(certificateHolder);
        
        cert.checkValidity(new Date()); //立即检测一次证书有效性
        cert.verify(issuerKeyPair.getPublic()); //立即使用公钥验证一下
        return cert;
	}
	
	/**
	 * 生成自签名证书
	 * 
	 * @param subject				- 证书所有者信息描述，e.g. CN=127.0.0.1,OU=CloudIOT,O=CertusNet,L=NanJing,S=JiangSu,C=CN
	 * @param keyPair				- 自签名证书秘钥对
	 * @param validityDays			- 证书有效期天数，e.g. 3650即10年
	 * @return						- 返回生成的CA证书
	 * @throws IOException, GeneralSecurityException, OperatorCreationException 
	 */
	public static X509Certificate generateSelfSignedCertificate(X500Name subject, KeyPair keyPair, Integer validityDays) throws IOException, GeneralSecurityException, OperatorCreationException {
		return generateCertificate(subject, keyPair.getPublic(), subject, keyPair, validityDays);
	}
	
	/**
	 * 将证书以PEM格式写入文件
	 * @param cert				- 证书对象
	 * @param certPemFile		- 证书保存文件
	 */
	public static void writeCertificateToPemFile(X509Certificate cert, File certPemFile) throws IOException, CertificateEncodingException {
		Assert.notNull(cert, "Parameter 'cert' must be required!");
		Assert.notNull(certPemFile, "Parameter 'certPemFile' must be required!");
		try (PemWriter writer = new PemWriter(new FileWriter(certPemFile))) {
	        writer.writeObject(new PemObject("CERTIFICATE", cert.getEncoded()));
	    }
	}
	
	/**
	 * 从指定PEM格式的证书文件中读出证书对象
	 * @param certPemFile		- 证书保存文件
	 * @return 					- 返回证书对象
	 * @throws CertificateException 
	 */
	public static X509Certificate readCertificateFromPemFile(File certPemFile) throws IOException, CertificateException {
		Assert.notNull(certPemFile, "Parameter 'certPemFile' must be required!");
		try (PemReader reader = new PemReader(new FileReader(certPemFile))) {
			PemObject pemObject = reader.readPemObject();
			CertificateFactory certificateFactory = CertificateFactory.getInstance(DEFAULT_CERT_TYPE);
			return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(pemObject.getContent()));
	    }
	}
	
}