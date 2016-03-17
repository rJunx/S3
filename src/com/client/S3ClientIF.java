package com.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface S3ClientIF extends Remote {	
	void revMsg( String msg ) throws RemoteException;
}
