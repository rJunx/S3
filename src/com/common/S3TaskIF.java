package com.common;

import com.server.S3ServerIF;


public interface S3TaskIF {
	public void run( S3ServerIF server ) throws Exception;
	public Object getResult();
}
