package com.common;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.client.S3ClientIF;
import com.server.S3ServerIF;


public interface S3TaskIF {
	public void run( S3ClientIF client, S3ServerIF server ) throws RemoteException, SQLException;
}
