package company.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.common.S3TaskIF;
import com.server.S3ServerIF;

import company.S3Const;
import company.S3Customer;
import company.S3OrderItem;
import company.S3Product;
import company.S3TableOPType;
import company.S3Transaction;
import company.S3TransactionCalculation;

import java.math.BigDecimal;

public class S3TransactionTask implements S3TaskIF {
	private S3Transaction transInfo = null;
	private List<S3OrderItem> orderList = new ArrayList<S3OrderItem>();
	private List<Object> ret = new ArrayList<Object>();
	
	//how to use:
	//In the client, call server.doTask(clientUUID, S3Const.CLASS_TRANSATION_TASK, transation, orders );
	//transation ---> {S3Const.TABLE_TRANSACTION_CUST_ID : xxxxxx }
	//item in orders ---> { S3Const.TABLE_ORDERITEM_BARCODE : xxxxxx, S3Const.TABLE_ORDERITEM_QTY : xx }
	public S3TransactionTask(Map<?, ?> transation, List<?> orders) {
		// TODO Auto-generated constructor stub
		transInfo = new S3Transaction(transation);
		
		for (int i = 0; i < orders.size(); i++) {
			
			orderList.add(new S3OrderItem((Map<?, ?>)orders.get(i)));
		}
	}

	@Override
	public void run(S3ServerIF server) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, S3Product> prodInfo = getProductListInfo(server);
		List<S3Product> prodList = new ArrayList<S3Product>(prodInfo.values());
		
		List<Object> invalidOrder = checkOrder(prodInfo);
		
		
		if (invalidOrder.size() == 0) {
			
			S3Customer cInfo = getCustomerInfo(server);
			
			
			//do the calculation and get the result
			S3TransactionCalculation transCal = new S3TransactionCalculation(cInfo, orderList);
			double totalCost = transCal.getTotalCost(prodList);
			double newBalance = cInfo.getBalance() - totalCost;
			if(newBalance < 0){
				// Joon to finish "Insufficient balance" scenario
				
			}
			else{
				// update customer
				int newCustPoints = cInfo.getPoint() - transCal.getTotalPointsConsumed(cInfo) + transCal.getTotalPointsEarned(totalCost);
				cInfo.changeBalance(newBalance);
				cInfo.changePoint(newCustPoints);
				updateCustomerInfo(cInfo, server);
				
				// update products
				for(int i = 0; i < this.orderList.size(); i++){
					String barcode = orderList.get(i).barCode;
					S3Product p = prodInfo.get(barcode);
					p.stockLv -= orderList.get(i).qty;
					
					Map<String, Object> rawData = new HashMap<String, Object>();
					p.fillRawData(rawData);
					ret.add(rawData);
				}
				updateProductInfo(prodInfo, server);
				
				// update transaction
				insertTransation(totalCost, server);
			}
		} else {
			
		}
	}
	
	private void insertTransation(double cost, S3ServerIF server) throws Exception {
		S3TableControlTask countTask = new S3TableControlTask(S3Const.TABLE_TRANSACTION, S3TableOPType.COUNT, null, null);
		countTask.run(server);
		
		List<?> result = (List<?>)countTask.getResult();
		BigDecimal count = (BigDecimal)((Map<?, ?>)result.get(0)).get(S3Const.TABLE_COUNT_RESULT);
		count = count.add(new BigDecimal(1));
		
		transInfo.id = count.toString();
		transInfo.date = new Date();
		transInfo.cost = cost;
		
		List<Object> values = new ArrayList<Object>();
		transInfo.fillRawData(values);
		
		S3TableControlTask insertTask = new S3TableControlTask(S3Const.TABLE_TRANSACTION, S3TableOPType.INSERT, values, null);
		insertTask.run(server);
		
		values = new ArrayList<Object>();
		
		for (int i = 0; i < orderList.size(); i++) {
			S3OrderItem order = orderList.get(i);
			order.transID = transInfo.id;
			
			order.fillRawData(values);
			
			insertTask = new S3TableControlTask(S3Const.TABLE_ORDERITEM, S3TableOPType.INSERT, values, null);
			insertTask.run(server);
		}
	}
	
	private void updateCustomerInfo(S3Customer newInfo, S3ServerIF server) throws Exception {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(S3Const.TABLE_USER_ID, newInfo.getID());
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(S3Const.TABLE_CUSTOMER_BALANCE, newInfo.getBalance());
		values.put(S3Const.TABLE_CUSTOMER_POINT, newInfo.getPoint());
		
		S3TableControlTask updateTask = new S3TableControlTask(S3Const.TABLE_CUSTOMER, S3TableOPType.UPDATE, values, conditions);
		updateTask.run(server);
	}
	
	
	private void updateProductInfo(Map<String, S3Product> prodInfo, S3ServerIF server) throws Exception {
		Iterator<String> itr = prodInfo.keySet().iterator();
		Map<String, Object> conditions = new HashMap<String, Object>();
		Map<String, Object> values = new HashMap<String, Object>();
		
		while(itr.hasNext()) {
			String k = itr.next();
			S3Product product = prodInfo.get(k);
			
			conditions.put(S3Const.TABLE_PRODUCT_ID, product.barcode);
			values.put(S3Const.TABLE_PRODUCT_STOCK_LV, product.stockLv);
			
			S3TableControlTask updateTask = new S3TableControlTask(S3Const.TABLE_PRODUCT, S3TableOPType.UPDATE, values, conditions);
			updateTask.run(server);
		}
	}
	
	private S3Customer getCustomerInfo(S3ServerIF server) throws Exception {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(S3Const.TABLE_USER_ID, transInfo.custID);
		
		S3TableControlTask selectTask = new S3TableControlTask(S3Const.TABLE_CUSTOMER, S3TableOPType.SELECT, null, conditions);
		selectTask.run(server);
		List<?> retList = (List<?>)selectTask.getResult();
		
		return new S3Customer((Map<?, ?>)retList.get(0));
	}
	
	private Map<String, S3Product> getProductListInfo(S3ServerIF server) throws Exception {
		Map<String, S3Product> prodDict = new HashMap<String, S3Product>();
		Map<String, Object> conditions = new HashMap<String, Object>();
		
		for (int i = 0; i < orderList.size(); i++) {
			S3OrderItem order = orderList.get(i);
			
			conditions.put(S3Const.TABLE_PRODUCT_ID, order.barCode);
			
			S3TableControlTask selectTask = new S3TableControlTask(S3Const.TABLE_PRODUCT, S3TableOPType.SELECT, null, conditions);
			selectTask.run(server);
			List<?> retList = (List<?>)selectTask.getResult();
			//assume always has product!!
			prodDict.put(order.barCode, new S3Product((Map<?, ?>)retList.get(0)));
		}
		
		return prodDict;
	}
	
	private List<Object> checkOrder(Map<String, S3Product> prodInfo) throws Exception {
		List<Object> invalidOrder = new ArrayList<Object>();
		
		for (int i = 0; i < orderList.size(); i++) {
			S3OrderItem order = orderList.get(i);
			S3Product item = (S3Product)prodInfo.get(order.barCode);
			if (item.stockLv < order.qty) {
				invalidOrder.add(order);
			}
		}
		
		return invalidOrder;
	}
	

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return ret;
	}

}
