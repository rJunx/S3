package com.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class S3ServerDriver {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// TODO Auto-generated method stub

		S3Server server = new S3Server();
		Naming.rebind("RMIS3Server", server);
		
		server.start();
	}

}
