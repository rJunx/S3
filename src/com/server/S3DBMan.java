package com.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class S3DBMan {
	private String driver = "oracle.jdbc.OracleDriver";
	private String url = null;
	private String user = null;
	private String password = null;
	private Connection con = null;
	
	public void init() {
		Properties prop = new Properties();
		try {
			System.out.println( System.getProperty("user.dir") );
			FileInputStream fis = new FileInputStream("DBConfig.txt");
			prop.load(fis);
			
			url = "jdbc:oracle:thin:@" + prop.getProperty("db_ip") + ":" + prop.getProperty("db_port") 
					+ ":" + prop.getProperty("db_sic");
			user = prop.getProperty("db_user");
			password = prop.getProperty("db_password");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connect() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection( url, user, password );
			System.out.println( con.toString() );
			
			System.out.println( "DB link succeed......" );
			
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}
	
	public ResultSet doQuery( String sql ) {
		if ( con == null ) {
			System.out.println( "Invalid Connection......" );
			return null;
		}
		
		try {
			System.out.println( "doQuery " + sql );
			PreparedStatement pstm = con.prepareStatement(sql);
			return pstm.executeQuery();
			
		} catch ( SQLException e ) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void close() {
		try {
			if ( con != null && (!con.isClosed()) ) {
				try {
					con.close();
				} catch ( SQLException e ) {
					
				}
			}
		} catch ( SQLException e ) {
			
		}
	}
}
