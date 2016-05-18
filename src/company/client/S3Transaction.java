package company.client;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import company.S3Const;

public class S3Transaction {

	// local variable
	public String id;
	public double cost;
	public Date date;
	public String custID;
	
	// constructor
	public S3Transaction(String id, double cost, Date date, String custID){
		this.id = id;
		this.cost =cost;
		this.date = date;
		this.custID = custID;
	}
	
	public S3Transaction(Map<?, ?> rawData) {
		Object value = rawData.get(S3Const.TABLE_TRANSACTION_ID);
		if (id != null) {
			id = (String)value;
		}
		
		value = rawData.get(S3Const.TABLE_TRANSACTION_CUST_ID);
		if (id != null) {
			custID = (String)value;
		}
		
		
		value = rawData.get(S3Const.TABLE_TRANSACTION_DATE);
		if (id != null) {
			//change date
		}
		
		cost = ((BigDecimal)rawData.get(S3Const.TABLE_TRANSACTION_COST)).doubleValue();
	}
	
	public void fillMapData(Map<String, Object> mapData) {
		mapData.put(S3Const.TABLE_TRANSACTION_COST, cost);
		
		if (custID != null)
			mapData.put(S3Const.TABLE_TRANSACTION_CUST_ID, custID);

		if (date != null)
			mapData.put(S3Const.TABLE_TRANSACTION_DATE, date);
		
		if (id != null)
			mapData.put(S3Const.TABLE_TRANSACTION_ID, id);
	}
}
