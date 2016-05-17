package com.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface S3ClientIF extends Remote {	
	void receiveMsg( String msg ) throws Exception;
	void receiveData( int taskType, Object data ) throws Exception;
	void receiveBordercastData( int taskType, Object data ) throws Exception;
	void sendTask(String className, Object... args) throws Exception;
}
