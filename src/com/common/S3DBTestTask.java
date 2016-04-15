package com.common;

public class S3DBTestTask extends S3Task {

	public S3DBTestTask(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}
	
	protected String getSQLStatement() {
		return "select * from S3TEST";
	}

	/*
	@Override
	protected void _run( S3Server server ) throws SQLException {
		ResultSet rs = server.getDBMan().doQuery( "select * from S3TEST" );
		if ( rs != null ) {
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println( "No. of columes : " + rsmd.getColumnCount() );
			System.out.println( "Column name of 1st column : " + rsmd.getColumnName(1) );
			System.out.println( "Column name of 2st column : " + rsmd.getColumnName(2) );
			rs.close();
		} else {
			System.out.println( "Empty ResultSet " );
		}

	}*/
}
