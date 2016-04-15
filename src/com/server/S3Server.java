package com.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;

import com.client.S3ClientIF;
import com.common.S3TaskIF;


public class S3Server extends UnicastRemoteObject implements S3ServerIF {
	private static final long serialVersionUID = 1L;
	//private ArrayList<S3ClientIF> clients;
	
	private S3DBMan dbMan = null;
	private HashMap<String, S3ClientIF> clients;
	
	public S3Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		//clients = new ArrayList<S3ClientIF>();
		
		clients = new HashMap<String, S3ClientIF>();
		
		dbMan = new S3DBMan();
	}
	
	public void shutdown() {
		dbMan.close();
	}
	
	public void start() {
		dbMan.init();
		dbMan.connect();
	}

	@Override
	public void registerClient(String uuid, S3ClientIF client) throws RemoteException {
		// TODO Auto-generated method stub
		//clients.add( client );
		
		clients.put( uuid, client );
		System.out.println( "Add Client " + uuid );
	}

	@Override
	public void broadcastMessage(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		/*
		for ( int i = 0; i < clients.size(); i++ ) {
			clients.get( i ).revMsg( msg );
		}*/
	}
	
	public S3DBMan getDBMan() {
		return dbMan;
	}

	@Override
	public void doTask(String UUID, String className, Object... args) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		try {
			Class<?> taskClass = Class.forName("com.common." + className);
			Constructor<?>[] tcCons = taskClass.getConstructors();
			
			Object[] params = new Object[1 + args.length];
			params[0] = UUID;
			for ( int i = 0; i < args.length; i++ ) {
				params[i + 1] = args[i];
			}
			
			S3TaskIF task = (S3TaskIF)tcCons[0].newInstance( params );
			
			S3ClientIF client = clients.get(UUID);
			if (client != null) {
				task.run(client, this);
			}
		} catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void unregisterClient(String uuid) throws RemoteException {
		// TODO Auto-generated method stub
		clients.remove(uuid);
		
		assert ( clients.get(uuid) == null );
		System.out.println( "Remove Client " + uuid );
	}
}
