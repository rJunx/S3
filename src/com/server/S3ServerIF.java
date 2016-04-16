package com.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.client.S3ClientIF;

import company.S3TaskMsg;

public interface S3ServerIF extends Remote {
	void registerClient( String uuid, S3ClientIF client ) throws RemoteException;
	void unregisterClient( String uuid ) throws RemoteException;
	
	void broadcastMessage( String msg ) throws RemoteException;
	
	void doTask( String UUID, String className, Object... args ) throws RemoteException, SQLException;
	void doTask( String UUID, int taskType, String className, Object... args ) throws RemoteException, SQLException;
}
