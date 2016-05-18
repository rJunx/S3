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

public class S3SupplierController {
	private S3ServerIF server;
	private String uuid;
	
	public S3SupplierController(String cUUID, S3ServerIF s){
		server = s;
		uuid = cUUID;
	}
	
	public void update(String supplierID, int taskType, String key, Object value) throws RemoteException, SQLException {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(S3Const.TABLE_SUPPLIER_ID, supplierID);
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(key, value);
		
		server.doTask(uuid, taskType, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_SUPPLIER, S3TableOPType.UPDATE, values, conditions);
	}
	
	public void create(String supplier, String email) throws RemoteException, SQLException{
		List<Object> values = new ArrayList<Object>();
		values.add(supplier);
		values.add(email);
		
		server.doTask(uuid, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3TableOPType.INSERT, null);
	}
	
	public void onGetSupplierByID(String ID, int taskType){
		
	}
	
	public void onGetAllSupplier(){
		
	}
}
