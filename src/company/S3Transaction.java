package company;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class S3Transaction {

	// local variable
	public String id;
	public double cost;
	public Date date;
	public String custID;
	
	// constructor
	public S3Transaction(String id, double cost, java.util.Date date, String custID){
		this.id = id;
		this.cost =cost;
		this.date = date;
		this.custID = custID;
	}
	
	public S3Transaction(Map<?, ?> rawData) {
		Object value = rawData.get(S3Const.TABLE_TRANSACTION_ID);
		if (value != null) {
			id = (String)value;
		}
		
		value = rawData.get(S3Const.TABLE_TRANSACTION_CUST_ID);
		if (value != null) {
			custID = (String)value;
		}
		
		
		value = rawData.get(S3Const.TABLE_TRANSACTION_DATE);
		if (value != null) {
			//change date
			date = new Date(((java.sql.Date)value).getTime());
		}
		
		value = rawData.get(S3Const.TABLE_TRANSACTION_COST);
		if(value != null){
			cost = ((BigDecimal)value).doubleValue();
		}
	}
	
	
	public void fillRawData(Map<String, Object> mapData) {
		mapData.put(S3Const.TABLE_TRANSACTION_COST, cost);
		
		if (custID != null)
			mapData.put(S3Const.TABLE_TRANSACTION_CUST_ID, custID);

		if (date != null){
			java.sql.Date dateSql = new java.sql.Date(date.getTime());
			mapData.put(S3Const.TABLE_TRANSACTION_DATE, dateSql);
		}		
		if (id != null)
			mapData.put(S3Const.TABLE_TRANSACTION_ID, id);
	}
	
	public void fillRawData(List<Object> listData) {
		listData.add(id);
		listData.add(cost);
		
		java.sql.Date dateSql = new java.sql.Date(date.getTime());
		listData.add(dateSql);
		
		listData.add(custID);
	}
}
