package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;

import com.server.S3ServerIF;

import company.S3Const;

public class S3ReportController {

	private S3ServerIF server;
	private String uuid;
	
	public S3ReportController(String cUUID, S3ServerIF s) {
		// TODO Auto-generated constructor stub
		server = s;
		uuid = cUUID;
	}
	
	public void generateSalesReport(int taskType, Date start, Date end) throws RemoteException, SQLException  {
		// TODO Auto-generated constructor stub
		server.doTask( uuid, taskType, S3Const.CLASS_REPORT_SALES_TASK, start, end );
	}
	
	public void generateTop10SellerReport(int taskType, Date start, Date end) throws RemoteException, SQLException  {
		server.doTask( uuid, taskType, S3Const.CLASS_REPORT_TOP_TEN_SELLER_TASK, start, end );
	}
	
	public void generateSupplyReport(int taskType, Date start, Date end) throws RemoteException, SQLException {
		server.doTask( uuid, taskType, S3Const.CLASS_REPORT_SUPPLY_TASK, start, end );
	}
}
