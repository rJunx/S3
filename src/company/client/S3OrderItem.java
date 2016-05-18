package company.client;

public class S3OrderItem {
	// local variables
	public String transID;
	public String barCode;
	public double qty;
	
	// constructor
	public S3OrderItem(String transID, String barCode, double qty){
		this.transID = transID;
		this.barCode =barCode;
		this.qty = qty;
	}
}
