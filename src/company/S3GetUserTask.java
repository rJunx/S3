package company;

import com.common.S3Task;

public class S3GetUserTask extends S3Task {
	private S3UserType userType;
	private String userID;
	static private String SQLStatement = "select * from %s where id = '%s'";
	
	public S3GetUserTask(String uuid, S3UserType type, String id) {
		super(uuid);
		// TODO Auto-generated constructor stub
		userType = type;
		userID = id;
	}
	
	protected String getSQLStatement() {
		String accountTB = (userType == S3UserType.CUSTOMER)? "S3T_Customer" : "S3T_STAFF";
		return String.format(SQLStatement, accountTB, userID);
	}

	/*
	@Override
	protected ResultSet _run( S3Server server ) throws SQLException {
		String accountTB = (userType == S3UserType.CUSTOMER)? "S3T_Customer" : "S3T_STAFF";
		ResultSetMetaData rsmd = null;
		
		return server.getDBMan().doQuery( String.format(SQLStatement, accountTB, userID) );
	}*/
}
