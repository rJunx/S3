package company;

public class S3TaskMsg {

	public String uuid;
	public int type;
	public String className;
	public Object[] initArgs;
	
	public S3TaskMsg(String UUID, int taskType, String cName, Object[] args) {
		// TODO Auto-generated constructor stub
		
		uuid = UUID;
		type = taskType;
		className = cName;
		initArgs = args;
	}

}
