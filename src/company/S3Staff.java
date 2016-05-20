package company;

import java.math.BigDecimal;
import java.util.Map;

public class S3Staff extends S3User {

	private S3StaffType type;

	public S3Staff(String id, int type){
		super(id);
		this.type = S3StaffType.values()[type];
	}
	
	public S3Staff(Map<?,?> data) {
		super(data);
		int t = ((BigDecimal) data.get(S3Const.TABLE_STAFF_TYPE)).intValue(); 
		this.type = S3StaffType.values()[t];
	}
	
	// accessor
	public S3StaffType getType(){return type;}

	@Override
	void update() {
		// TODO Auto-generated method stub
		
	}
}
