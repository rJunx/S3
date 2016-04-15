package company;

import com.common.S3Task;

public class S3CreateCustomerTask extends S3Task {
	private String userID;
	private int userPoint;
	private int userCash;
	
	public S3CreateCustomerTask(String uuid, String id, int point, int cash) {
		super(uuid);
		// TODO Auto-generated constructor stub
		userID = id;
		userPoint = point;
		userCash = cash;
	}
	
	protected String getSQLStatement() {
		return String.format("INSERT INTO S3T_Customer VALUES('%s', %d, %d)", userID, userPoint, userCash);
	}
}
