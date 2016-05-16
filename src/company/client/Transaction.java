package company.client;

import java.util.Date;

public class Transaction {

	// local variable
	public String id;
	public double cost;
	public Date date;
	public String custID;
	
	// constructor
	public Transaction(String id, double cost, Date date, String custID){
		this.id = id;
		this.cost =cost;
		this.date = date;
		this.custID = custID;
	}
}
