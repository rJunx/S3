package com.common;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.client.S3ClientIF;
import com.server.S3Server;
import com.server.S3ServerIF;

public class S3Task implements S3TaskIF {
	protected String uuid = null;
	
	public S3Task( String uuid ) {
		this.uuid = uuid;
	}
	
	@Override
	public void run(S3ClientIF client, S3ServerIF server) throws RemoteException {
		// TODO Auto-generated method stub		
		try {
			_run( (S3Server)server );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.revMsg( "run succeed......" );
	}
	
	protected void _run( S3Server server ) throws SQLException {
		
	}
}
