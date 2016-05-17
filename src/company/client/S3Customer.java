package company.client;

import java.math.BigDecimal;
import java.util.Map;

import company.S3Const;

public class S3Customer extends S3User {
	
	// local variables
	private double balance = 0.0;
	private int point = 0;
	
	// constructor
	public S3Customer(String id, double balance){
		super(id);
		this.balance = balance;

	}
	public S3Customer(String id, double balance, int point){
		super(id);
		this.balance = balance;
		this.point = point;
	}
	
	public S3Customer(Map<?, ?> data) {
		super(data);
		update(data);
	}
	
	public void update(Map<?, ?> data) {
		BigDecimal value = (BigDecimal) data.get(S3Const.TABLE_CUSTOMER_BALANCE);
		if (value != null) {
			balance = value.doubleValue();
		}
		
		value = (BigDecimal) data.get(S3Const.TABLE_CUSTOMER_POINT);
		if (value != null) {
			point = value.intValue();
		}
	}
	
	// accessor
	public double getBalance(){return balance;}
	public int getPoint(){return point;}

	@Override
	void update() {
		// TODO Auto-generated method stub
		
	}
}
