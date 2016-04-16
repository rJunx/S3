package company;

import com.common.S3Task;

public class S3GetProductTask extends S3Task {
	private S3PAType cpType;
	private Object pcArg;
	
	public S3GetProductTask(String uuid, S3PAType type, Object arg) {
		super(uuid);
		// TODO Auto-generated constructor stub
		cpType = type;
		pcArg = arg;
	}

	protected String getSQLStatement() {
		if (cpType == S3PAType.ID) {
			return String.format("SELECT * FROM S3T_Product WHERE BARCODE = '%s'", pcArg);
		} else if (pcArg == S3PAType.ID) {
			return String.format("SELECT * FROM S3T_Product WHERE NAME = '%s'", pcArg);
		} else {
			return String.format("SELECT * FROM S3T_Product");
		}
	}
}
