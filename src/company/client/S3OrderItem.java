package company.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import company.S3Const;

public class S3OrderItem {
	// local variables
	public String transID = null;
	public String barCode;
	public double qty;
	
	// constructor
	public S3OrderItem(String transID, String barCode, double qty){
		this.transID = transID;
		this.barCode =barCode;
		this.qty = qty;
	}
	
	public S3OrderItem(Map<?, ?> rawData) {
		Object value = rawData.get(S3Const.TABLE_ORDERITEM_QTY);
		if (value instanceof BigDecimal) {
			qty = ((BigDecimal)value).doubleValue();
		} else {
			qty = (double) value;
		}
		
		barCode = (String)rawData.get(S3Const.TABLE_ORDERITEM_BARCODE);
		
		Object id = rawData.get(S3Const.TABLE_ORDERITEM_TRANS_ID);
		if (id != null) {
			transID = (String)id;
		}
	}
	
	public void fillRawData(Map<String, Object> mapData) {
		mapData.put(S3Const.TABLE_ORDERITEM_QTY, qty);
		mapData.put(S3Const.TABLE_ORDERITEM_BARCODE, barCode);
		if (transID != null) {
			mapData.put(S3Const.TABLE_ORDERITEM_TRANS_ID, transID);
		}
	}
	
	public void fillRawData(List<Object> listData) {
		listData.add(transID);
		listData.add(barCode);
		listData.add(qty);
	}
}
