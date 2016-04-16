package company;

import com.common.S3Task;

public class S3UpateProductTask extends S3Task {
	private String id;
	private Object value;
	private String key;
	
	public S3UpateProductTask(String uuid, String productID, String k, Object v) {
		super(uuid);
		// TODO Auto-generated constructor stub
		
		id = productID;
		value = v;
		key = k;
	}

	@Override
	protected String getSQLStatement() {
		// TODO Auto-generated method stub
		String sset = null;
		if (value instanceof String) {
			sset = key + "='" + value +"'";
		} else {
			sset = key + "=" + value;
		}
		
		return String.format("UPDATE S3T_PRODUCT SET %s WHERE BARCODE = '%s'", sset, id);
	}
}
