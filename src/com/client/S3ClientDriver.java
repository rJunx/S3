package com.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.server.S3ServerIF;

public class S3ClientDriver {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		String serverURL = "rmi://localhost/RMIS3Server";
		S3ServerIF server = (S3ServerIF)Naming.lookup( serverURL );
		new Thread(new S3Client( "", server )).start();
	}

}
