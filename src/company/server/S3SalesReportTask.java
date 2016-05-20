package company.server;

import java.util.Date;

import com.common.S3DBTask;

import company.S3Const;

public class S3SalesReportTask extends S3DBTask {

	public S3SalesReportTask(Date start, Date end) {
		// TODO Auto-generated constructor stub
		
		String sf = "TO_DATE('%s', '%s')";
		
		String startDate = String.format(sf, new java.sql.Date(start.getTime()), S3Const.DATE_FORMAT);
		String endDate = String.format(sf, new java.sql.Date(end.getTime()), S3Const.DATE_FORMAT);
		
		String statement = String.format(
				"select barcode,name,sum(quantity*price) "
			  + "from S3T_Transaction, S3T_Product, S3T_OrderItem "
			  + "WHERE S3T_Transaction.ID=S3T_OrderItem.TRANS_ID AND S3T_Product.BARCODE=S3T_OrderItem.PROD_BARCODE AND TRANS_DATE between %s and %s"
			  + "GROUP BY BARCODE,NAME", 
				startDate, endDate);
		
		setSQLStatement(statement);
	}

}
