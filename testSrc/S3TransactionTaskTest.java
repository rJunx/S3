import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.server.S3Server;

import company.S3Const;
import company.S3OrderItem;
import company.S3TableOPType;
import company.server.S3TableControlTask;
import company.server.S3TransactionTask;

public class S3TransactionTaskTest {
	private static S3Server server;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		try {
			server = new S3Server();
			server.start();//connect db
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.shutdown();
	}


	@Test
	public void testRun() throws SQLException {
		
		//generate transaction only on given customer ID
		Map<String, Object> transaction = new HashMap<String, Object>();
		transaction.put(S3Const.TABLE_TRANSACTION_CUST_ID, "c0000000001");

		// generate order list
		List<Object> orders = new ArrayList<Object>();
		Map<String,Object> item = new HashMap<String, Object>();
		item.put(S3Const.TABLE_ORDERITEM_BARCODE, "0000000000");
		item.put(S3Const.TABLE_ORDERITEM_QTY, 1.0);
		orders.add(item);

		// instantiate a S3TransactionTastk object
		S3TransactionTask transactionTask = new S3TransactionTask(transaction, orders);
		
		// run()
		try {
			transactionTask.run(server);
			
			S3TableControlTask selectTask = new S3TableControlTask(S3Const.TABLE_TRANSACTION, S3TableOPType.SELECT, null, null);

			selectTask.run(server);
			List retList = (List)selectTask.getResult();
			
			Map tuple = (Map)retList.get(0);
			double totalCost = ((BigDecimal) tuple.get(S3Const.TABLE_TRANSACTION_COST)).doubleValue();
			
			Assert.assertEquals(4.0, totalCost, 0.01);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
