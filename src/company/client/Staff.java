package company.client;

public class Staff {

	// local variables
	private String id;
	private int type;
		
	// constructor
	public Staff(String id, int type){
		this.id = id;
		this.type = type;
	}
	
	// accessor
	public String getId(){return id;}
	public int getType(){return type;}
	
	// mutator
	public void updateType(int newType){type = newType;}
}
