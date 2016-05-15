package company.client;

public class Customer {
	
	// local variables
	private String id;
	private double balance = 0.0;
	private int point = 0;
	
	// constructor
	public Customer(String id, double balance){
		this.id = id;
		this.balance = balance;
	}
	public Customer(String id, double balance, int point){
		this.id = id;
		this.balance = balance;
		this.point = point;
	}
	
	// accessor
	public String getId(){return id;}
	public double getBalance(){return balance;}
	public int getPoint(){return point;}
	
	// mutator
	public void topUpBalance(double topUp){balance += topUp;}
	public void reduceBalance(double cost){balance -= cost;}
	public void addPoints(int newPnt){point += newPnt;}
	public void deletePoints(int pnt){point -= pnt;}
}
