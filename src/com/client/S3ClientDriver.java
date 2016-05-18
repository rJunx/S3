package com.client;

import java.rmi.Naming;

import com.server.S3ServerIF;

public class S3ClientDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String serverURL = "rmi://localhost/RMIS3Server";
		S3ServerIF server = (S3ServerIF)Naming.lookup( serverURL );
		new Thread(new S3Client( "", server )).start();
	}

}
