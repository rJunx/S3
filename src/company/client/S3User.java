package company.client;

import java.util.Map;

import company.S3Const;
import company.S3UserType;

abstract public class S3User {
	protected String id = null;
	
	public S3User(String userID) {
		// TODO Auto-generated constructor stub
		id = userID;
	}
	
	public S3User(Map<?, ?> data) {
		id = (String)data.get(S3Const.TABLE_USER_ID);
	}
	
	public String getID() { return id; }
	
	abstract void update();
	
	
	public static S3UserType checkUserType(String userID) {
		if (userID.charAt(0) == 'c') {
			return S3UserType.CUSTOMER;
		} else if (userID.charAt(0) == 's') {
			return S3UserType.STAFF;
		} else {
			return S3UserType.UNKNOWN;
		}
	}
}
