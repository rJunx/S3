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
public class S3SupplyController {
	private S3ServerIF server;
	private String uuid;
	
	public S3SupplyController(String cUUID, S3ServerIF s){
		server = s;
		uuid = cUUID;
	}
	
	public void create(String supplierID, String prodID, int quantity, java.sql.Date date) throws RemoteException, SQLException{
		List<Object> values = new ArrayList<Object>();
		values.add(supplierID);
		values.add(prodID);
		values.add(quantity);
		values.add(date);
		
		server.doTask(uuid, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_SUPPLY, S3TableOPType.INSERT, values, null);
	}
	
	public void onGetSupplyByDate(java.sql.Date date){
		
	}
	
	
}
