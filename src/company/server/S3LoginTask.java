package company.server;

import java.util.HashMap;
import java.util.Map;

import com.common.S3TaskIF;
import com.server.S3ServerIF;

import company.S3Const;
import company.S3TableOPType;
import company.S3UserType;

public class S3LoginTask implements S3TaskIF {
	S3TableControlTask selectTask = null;
	
	public S3LoginTask(S3UserType userType, String userID) {
		// TODO Auto-generated constructor stub
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(S3Const.TABLE_USER_ID, userID);
		
		String tbName = (userType == S3UserType.CUSTOMER)? S3Const.TABLE_CUSTOMER : S3Const.TABLE_STAFF;
		
		selectTask = new S3TableControlTask(tbName, S3TableOPType.SELECT, null, conditions);
	}

	@Override
	public void run(S3ServerIF server) throws Exception {
		// TODO Auto-generated method stub
		selectTask.run(server);
	}

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return selectTask.getResult();
	}

}
