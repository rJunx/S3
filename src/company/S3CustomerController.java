package company;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.server.S3ServerIF;

public class S3CustomerController {

	private S3ServerIF server;
	private String uuid;
	
	public S3CustomerController(String cUUID, S3ServerIF s) {
		// TODO Auto-generated constructor stub
		server = s;
		uuid = cUUID;
	}
	
	public void update(String ID, String key, Object value) throws RemoteException, SQLException {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(S3Const.TABLE_CUSTOMER_ID, ID);
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(key, value);
		
		server.doTask(uuid, S3Const.CLASS_TASK_NAME, S3Const.TABLE_CUSTOMER, S3TableOPType.UPDATE, values, conditions);
	}
	
	public void updateCash(String ID, double value) throws RemoteException, SQLException {
		update(ID, S3Const.TABLE_CUSTOMER_CASH, value);
	}
	
	public void updatePoint(String ID, int value) throws RemoteException, SQLException {
		update(ID, S3Const.TABLE_CUSTOMER_POINT, value);
	}
	
	public void create(String userID, int point, double cash) throws RemoteException, SQLException {
		List<Object> values = new ArrayList<Object>();
		values.add(userID);
		values.add(point);
		values.add(cash);
		
		server.doTask(uuid, S3Const.CLASS_TASK_NAME, S3Const.TABLE_CUSTOMER, S3TableOPType.INSERT, values, null);
	}
	
	public void onGetCustomerInfoByID(String ID) throws RemoteException, SQLException {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(S3Const.TABLE_CUSTOMER_ID, ID);
		
		server.doTask(uuid, S3Const.CLASS_TASK_NAME, S3Const.TABLE_CUSTOMER, S3TableOPType.SELECT, null, m);
	}
}