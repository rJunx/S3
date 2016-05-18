package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.server.S3ServerIF;

import company.S3Const;
import company.S3TableOPType;

public class S3StaffController {

	private S3ServerIF server;
	private String uuid;
	
	public S3StaffController(String cUUID, S3ServerIF s) {
		// TODO Auto-generated constructor stub
		server = s;
		uuid = cUUID;
	}
	
	public void onGetStaffInfoByID(String ID, int taskType) throws RemoteException, SQLException {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(S3Const.TABLE_USER_ID, ID);
		
		server.doTask(uuid, taskType, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_STAFF, S3TableOPType.SELECT, null, m);
	}
	
	public void create(String ID, int type)  throws RemoteException, SQLException  {
		List<Object> values = new ArrayList<Object>();
		values.add(ID);
		values.add(type);
		server.doTask(uuid, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_STAFF, S3TableOPType.INSERT, values, null);
	}
}
