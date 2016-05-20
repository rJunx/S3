package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	
	public void createTransaction(String TransID, double cost, java.sql.Date date, String cust_id) throws RemoteException, SQLException{
		List<Object> values = new ArrayList<Object>();
		values.add(TransID);
		values.add(cost);
		values.add(date);
		values.add(cust_id);
		
		server.doTask(uuid, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_TRANSACTION, S3TableOPType.INSERT, values, null);
	}
	
	public void getTransactionByDate(java.sql.Date date, int taskType) throws RemoteException, SQLException{
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put(S3Const.TABLE_TRANSACTION_DATE, date);
		
		server.doTask(uuid, taskType, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_TRANSACTION, S3TableOPType.SELECT, null, condition);
	}
	
	public void postGetAllTransaction(int taskType) throws RemoteException, SQLException{
		server.doTask(uuid, taskType, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_TRANSACTION, S3TableOPType.SELECT, null, null);
	}
	
	public void purchase(int taskType, S3Customer customer, Map<S3Product, Integer> productInCart) throws RemoteException, SQLException{
		//generate transaction only on given customer ID
		Map<String, Object> transaction = new HashMap<String, Object>();
		transaction.put(S3Const.TABLE_TRANSACTION_CUST_ID, customer.getID());

		// generate order list
		List<Object> orders = new ArrayList<Object>();
		
		Iterator<S3Product> itr = productInCart.keySet().iterator();
		while(itr.hasNext()) {
			S3Product product = itr.next();
			Map<String,Object> item = new HashMap<String, Object>();
			item.put(S3Const.TABLE_ORDERITEM_BARCODE, product.barcode);
			item.put(S3Const.TABLE_ORDERITEM_QTY, productInCart.get(product));
			orders.add(item);
		}

		server.doTask(uuid, taskType, S3Const.CLASS_TRANSATION_TASK, transaction, orders);
	}
}
