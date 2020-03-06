package com.penglecode.xmodule.ssltlscerts.examples;

import java.nio.file.Path;
import java.nio.file.Paths;

public class X509CertificateExample {

	public static void main(String[] args) throws Exception {
		Path certsPath = Paths.get("d:/temp/grpc-demo/certs");
		String caSubjectCN = "GrpcDemo";
		String serverSubjectCN = "GrpcDemoServer";
		String clientSubjectCN = "GrpcDemoClient";
		X509CertificateGenerator generator = new X509CertificateGenerator(certsPath, caSubjectCN, serverSubjectCN, clientSubjectCN);
		generator.generateAll();
	}

}
