package com.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface S3ClientIF extends Remote {	
	void receiveMsg( String msg ) throws RemoteException;
	void receiveData( int taskType, Object data ) throws RemoteException;
	void receiveBordercastData( int taskType, Object data ) throws RemoteException;
	void sendTask(String className, Object... args) throws RemoteException;
}
