package com.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.UUID;

import com.server.S3ServerIF;

public class S3Client extends UnicastRemoteObject implements S3ClientIF, Runnable {
	private static final long serialVersionUID = 1L;
	private S3ServerIF server;
	private String name = null;
	private UUID id = null;
	private Scanner scanner = new Scanner( System.in );
	
	protected S3Client( String name, S3ServerIF server ) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		
		this.server = server;
		this.name = name;
		this.id = UUID.randomUUID();
		
		server.registerClient( getUUID(), this );
	}
	
	protected void finalize() {
		try {
			scanner.close();
			server.unregisterClient( getUUID() );
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getUUID() {
		return id.toString();
	}

	@Override
	public void revMsg(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println( msg );
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		String msg;
		
		while ( true ) {
			msg = scanner.nextLine();
			/*
			try {
				server.broadcastMessage(msg);
			} catch ( RemoteException e ) {
				e.printStackTrace();
			} finally {
				//scanner.close();
			}*/
			
			if ( msg.equals("Test") ) {
				try {
					server.doTask(getUUID(), "S3TestTask", 123, 435);
				} catch ( RemoteException e ) {
					e.printStackTrace();
				}
			} else if( msg.equals("DBTest") ) {
				try {
					server.doTask(getUUID(), "S3DBTestTask" );
				} catch ( RemoteException e ) {
					e.printStackTrace();
				}
			} 
		}
	}

}
