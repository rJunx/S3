package com.client;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import com.server.S3ServerIF;
import company.client.S3Application;

public class S3Client extends UnicastRemoteObject implements S3ClientIF, Runnable {
	private static final long serialVersionUID = 1L;
	private S3ServerIF server;
	private String name = null;
	private UUID id = null;
	private S3Application app = null;
	
	public S3Client( String name, S3ServerIF server ) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		
		this.server = server;
		this.name = name;
		this.id = UUID.randomUUID();
		
		server.registerClient( getUUID(), this );
		app = new S3Application(getUUID(), server);
	}
	
	protected void finalize() {
		try {
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
	public void receiveMsg(String msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println( msg );
	}
	
	public void receiveData( int taskType, Object data ) throws Exception {
		// TODO Auto-generated method stub
		app.onReceiveData(taskType, data);
	}
	
	public void receiveBordercastData( int taskType, Object data ) throws Exception {
		app.onReceiveBordercastData(taskType, data);
	}
	
	public void sendTask(String className, Object... args) {
		try {
			server.getClass().getDeclaredMethod("doTask", String.class, String.class, Object[].class).invoke(server, getUUID(), className, args);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			app.run();
			server.unregisterClient(getUUID());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
