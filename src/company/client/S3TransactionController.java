package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.server.S3ServerIF;

import company.S3Const;
import company.S3TableOPType;

public class S3TransactionController {
	private S3ServerIF server;
	private String uuid;

	
	public S3TransactionController(String trUUID, S3ServerIF s){
		server = s;
		uuid = trUUID;
	}
	
	public void createTransaction(String TransID, double cost, String date_time, String cust_id) throws RemoteException, SQLException{
		List<Object> values = new ArrayList<Object>();
		values.add(TransID);
		values.add(cost);
		values.add(date_time);
		values.add(cust_id);
		
		server.doTask(uuid, S3Const.CLASS_TASK_NAME, S3Const.TABLE_TRANSACTION, S3TableOPType.INSERT, values, null);
	}
	
	public void getTransactionByDate(java.sql.Date date, int taskType) throws RemoteException, SQLException{
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put(S3Const.TABLE_TRANSACTION_DATE, date);
		
		server.doTask(uuid, taskType, S3Const.CLASS_TASK_NAME, S3Const.TABLE_TRANSACTION, S3TableOPType.SELECT, null, condition);
	}
	
	
}
