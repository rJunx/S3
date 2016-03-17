package com.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class S3ServerDriver {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// TODO Auto-generated method stub
		System.setProperty( "java.rmi.server.hostname", "127.0.0.1" );
		S3Server server = new S3Server();
		Naming.rebind("RMIS3Server", server);
		
		server.start();
		
		System.clearProperty( "java.rmi.server.hostname" );
	}

}
