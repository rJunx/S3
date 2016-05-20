package company.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.common.S3DBTask;

import company.S3Const;
import company.S3TableOPType;

public class S3SupplyReportTask extends S3DBTask {

	public S3SupplyReportTask(Date start, Date end) {
		// TODO Auto-generated constructor stub
		
		String sf = "TO_DATE('%s', '%s')";
		
		String startDate = String.format(sf, new java.sql.Date(start.getTime()), S3Const.DATE_FORMAT);
		String endDate = String.format(sf, new java.sql.Date(end.getTime()), S3Const.DATE_FORMAT);
		
		String statement = String.format(
				"SELECT supplier_id,prod_barcode,e_mail "
				+ "FROM S3T_Supplier,S3T_Supply "
				+ "WHERE S3T_Supplier.ID= S3T_Supply.supplier_ID AND SUPPLY_DATE between %s and %s", 
				startDate, endDate);
		
		setSQLStatement(statement);
	}
}
