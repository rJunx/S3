package company.client;

import java.util.Map;
import company.S3Const;

public class Product {
	public String barcode;
	public String name;
	public double price;
	public int promotion;
	public double discount;
	public int stockLv;
	public int replenishLv;
	public String supplier;
	
	public Product(String barcode, String name, double price, int promotion, double discount, int stockLv, int replenishLv, String supplier){
		this.barcode= barcode;
		this.name = name;
		this.price = price;
		this.promotion = promotion;
		this.discount = discount;
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
		price = (double)row.get(S3Const.TABLE_PRODUCT_PRICE);
		promotion = (int)row.get(S3Const.TABLE_PRODUCT_PROMOTION);
		discount = (double)row.get(S3Const.TABLE_PRODUCT_DISCOUNT);
		stockLv = (int)row.get(S3Const.TABLE_PRODUCT_STOCK_LV);
		replenishLv = (int)row.get(S3Const.TABLE_PRODUCT_REPLENISH_LV);
		supplier = (String)row.get(S3Const.TABLE_PRODUCT_SUPPLIER);
	}
}
