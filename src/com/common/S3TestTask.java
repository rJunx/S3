package com.common;

public class S3TestTask extends S3Task {
	
	public S3TestTask(String uuid, int a, int b) {
		super(uuid);
		// TODO Auto-generated constructor stub
		
		System.out.println( a + " " + b );
	}

	/*
	@Override
	protected void _run( S3Server server ) {
		
	}*/
}
