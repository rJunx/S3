package company.client;

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
	
}
