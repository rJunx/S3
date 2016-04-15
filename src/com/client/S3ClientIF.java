package com.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface S3ClientIF extends Remote {	
	void revMsg( String msg ) throws RemoteException;
	void revData( String classType, List data ) throws RemoteException;
	void sendTask(String className, Object... args) throws RemoteException;
}
