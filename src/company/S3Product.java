package company;

import java.math.BigDecimal;
import java.util.Map;

public class S3Product {
	public String barcode;
	public String name;
	public double price;
	public int promotion;
	public int discount;
	public int stockLv;
	public int replenishLv;
	public String supplier;
	
	// Constructor
	public S3Product(String barcode, String name, double price, int promotion, double discount, int stockLv, int replenishLv, String supplier){
		this.barcode= barcode;
		this.name = name;
		this.price = price;
		this.promotion = promotion;
		this.discount = (int)discount;
		this.stockLv = stockLv;
		this.replenishLv = replenishLv;
		this.supplier = supplier;
	}
	
	public S3Product(Map<?, ?> row) {
		update(row);
	}
	
	public void update(Map<?, ?> row) {
		barcode = (String)row.get(S3Const.TABLE_PRODUCT_ID);
		name = (String)row.get(S3Const.TABLE_PRODUCT_NAME);
		
		Object value = row.get(S3Const.TABLE_PRODUCT_PRICE);
		if (value instanceof BigDecimal) {
			price = ((BigDecimal)value).doubleValue();
		} else {
			price = Double.parseDouble(value.toString());
		}
		
		value = row.get(S3Const.TABLE_PRODUCT_PROMOTION);
		if (value instanceof BigDecimal) {
			promotion = ((BigDecimal)value).intValue();
		} else {
			promotion = (int)value;
		}
		
		value = row.get(S3Const.TABLE_PRODUCT_DISCOUNT);
		if (value instanceof BigDecimal) {
			discount = ((BigDecimal)value).intValue();
		} else {
			discount = (int)value;
		}
		
		value = row.get(S3Const.TABLE_PRODUCT_STOCK_LV);
		if (value instanceof BigDecimal) {
			stockLv = ((BigDecimal)value).intValue();
		} else {
			stockLv = (int)value;
		}
		
		value = row.get(S3Const.TABLE_PRODUCT_REPLENISH_LV);
		if (value instanceof BigDecimal) {
			replenishLv = ((BigDecimal)value).intValue();
		} else {
			replenishLv = (int)value;
		}

		value = row.get(S3Const.TABLE_PRODUCT_SUPPLIER);
		if (value != null) {
			supplier = (String)value;
		}
	}
	
	public void fillRawData(Map<String, Object> rawData) {
		rawData.put(S3Const.TABLE_PRODUCT_ID, barcode);
		rawData.put(S3Const.TABLE_PRODUCT_NAME, name);
		rawData.put(S3Const.TABLE_PRODUCT_PRICE, price);
		rawData.put(S3Const.TABLE_PRODUCT_PROMOTION, promotion);
		rawData.put(S3Const.TABLE_PRODUCT_DISCOUNT, discount);
		rawData.put(S3Const.TABLE_PRODUCT_STOCK_LV, stockLv);
		rawData.put(S3Const.TABLE_PRODUCT_REPLENISH_LV, replenishLv);
		rawData.put(S3Const.TABLE_SUPPLIER_ID, supplier);
	}
}
