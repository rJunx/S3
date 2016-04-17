package com.common;

import java.util.ArrayList;

import com.server.S3ServerIF;

public class S3TestTask implements S3TaskIF {
	private ArrayList<Integer> ret = new ArrayList<Integer>();
	
	public S3TestTask(int a, int b) {
		// TODO Auto-generated constructor stub
		System.out.println( a + " " + b );
		ret.add(a);
		ret.add(b);
	}

	@Override
	public void run(S3ServerIF server) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return ret;
	}
}
