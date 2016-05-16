package company.client;

import java.math.BigDecimal;
import java.util.Map;
import company.S3Const;

public class Product {
	public String barcode;
	public String name;
	public double price;
	public int promotion;
	public int discount;
	public int stockLv;
	public int replenishLv;
	public String supplier;
	
	public Product(String barcode, String name, double price, int promotion, double discount, int stockLv, int replenishLv, String supplier){
		this.barcode= barcode;
		this.name = name;
		this.price = price;
		this.promotion = promotion;
		this.discount = (int)discount;
		this.stockLv = stockLv;
		this.replenishLv = replenishLv;
		this.supplier = supplier;
	}
	
	public Product(Map row) {
		update(row);
	}
	
	public void update(Map row) {
		barcode = (String)row.get(S3Const.TABLE_PRODUCT_ID);
		name = (String)row.get(S3Const.TABLE_PRODUCT_NAME);
		price = ((BigDecimal) row.get(S3Const.TABLE_PRODUCT_PRICE)).doubleValue();
		promotion = ((BigDecimal) row.get(S3Const.TABLE_PRODUCT_PROMOTION)).intValue();
		discount = ((BigDecimal) row.get(S3Const.TABLE_PRODUCT_DISCOUNT)).intValue();
		stockLv = ((BigDecimal) row.get(S3Const.TABLE_PRODUCT_STOCK_LV)).intValue();
		replenishLv = ((BigDecimal) row.get(S3Const.TABLE_PRODUCT_REPLENISH_LV)).intValue();
		//supplier = (String) row.get(S3Const.TABLE_PRODUCT_SUPPLIER);
	}
}
