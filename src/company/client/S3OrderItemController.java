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

public class S3OrderItemController {
	private S3ServerIF server;
	private String uuid;
	
	public static double[][] bulkSalePlan ={{0.00,0.00,0.00},
											{0.10,0.20,0.30},
											{0.15,0.25,0.35}, 
											{0.20,0.40,0.60}};

	
	public S3OrderItemController(String oUUID, S3ServerIF s){
		server = s;
		uuid = oUUID;
	}
	
	// adding sequence should be the same as the attribute sequence in database	
	public void createOrderItems(String TransID, String ProdID, double quantity) throws RemoteException, SQLException{
		List<Object> values = new ArrayList<Object>();
		values.add(TransID);
		values.add(ProdID);
		values.add(quantity);
		
		server.doTask(uuid, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_ORDERITEM, S3TableOPType.INSERT, values, null);
	}
	
	public void updateOrderItemQty(String TransID, String ProdID, String qty, Object value) throws RemoteException, SQLException{
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(S3Const.TABLE_TRANSACTION_ID, TransID);
		conditions.put(S3Const.TABLE_PRODUCT_ID, ProdID);
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(qty, values);
		
		server.doTask(uuid, S3Const.CLASS_BASIC_TABLE_CONTROL_TASK, S3Const.TABLE_ORDERITEM, S3TableOPType.UPDATE, values, conditions);
	}
	
	static public String getPromotionTips(int value) {
		String v = "";
		switch(value) {
		case 0:
			v = "NONE";
		case 1:
			v = "10(10%) 20(20%) 30(30%)";
		case 2:
			v = "10(15%) 20(25%) 30(35%)";
		case 3:
			v = "10(20%) 20(40%) 30(60%)";
		}
		return v;
	}
}
	
	

