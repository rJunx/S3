package company;

import java.util.Map;

public class S3Supplier {
	// local variables
	public String id; 
	public String email;
	
	// constructor
	public S3Supplier(String id, String email){
		this.id = id;
		this.email = email;
	}
	
	public S3Supplier(Map<?, ?> row) {
		update(row);
	}
	
	public void update(Map<?, ?> row) {
		id = (String)row.get(S3Const.TABLE_SUPPLIER_ID);
		email = (String)row.get(S3Const.TABLE_SUPPLIER_EMAIL);
	}
}
