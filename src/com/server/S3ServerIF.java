package com.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;

import com.client.S3ClientIF;

public interface S3ServerIF extends Remote {
	void registerClient( String uuid, S3ClientIF client ) throws RemoteException;
	void unregisterClient( String uuid ) throws RemoteException;
	
	void broadcastMessage( Map<?, ?> args ) throws RemoteException;
	
	void doTask( String UUID, String className, Object... args ) throws RemoteException, SQLException;
	void doTask( String UUID, int taskType, String className, Object... args ) throws RemoteException, SQLException;
}
