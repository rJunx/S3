package company.client;

import java.util.Date;

public class Supply {
	// local variables
	public String supplierID; 
	public String productID;
	public double qty;
	public Date date;
	
	// constructor
	public Supply(String supplierID, String productID, double qty, Date date){
		this.supplierID = supplierID;
		this.productID = productID;
		this.qty = qty;
		this.date = date;
	}
}
