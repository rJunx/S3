package company.client;

import java.util.Date;

public class S3Supply {
	// local variables
	public String supplierID; 
	public String productID;
	public double qty;
	public Date date;
	
	// constructor
	public S3Supply(String supplierID, String productID, double qty, Date date){
		this.supplierID = supplierID;
		this.productID = productID;
		this.qty = qty;
		this.date = date;
	}
}
