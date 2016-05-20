import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.client.S3ClientIF;
import com.server.S3Server;

import company.S3Const;
import company.client.S3CustomerController;
import company.client.S3TransactionController;	

public class S3TransactionControllerTest implements S3ClientIF{
	private static String uuid = UUID.randomUUID().toString();
	private static S3Server server;
	private static S3TransactionController controller;
	private static S3CustomerController controllerCustomer;
	private static int FLAG = 0; 
	
	private static String id = "Tran1";
	private static double cost = 55.55;
	private static String custid = "c0000000001";
	
	// set the date and time
	private static java.util.Date dateUtil = new java.util.Date();
	private static java.sql.Date dateSql = new java.sql.Date(dateUtil.getTime());
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		try{
			server = new S3Server();
			server.start();
			
			controller = new S3TransactionController(uuid, server);
			controllerCustomer = new S3CustomerController(uuid, server);
			controllerCustomer.create("c0000000001", 0, 100);
		}catch(RemoteException e){
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		server.shutdown();
	}

	@Before
	public void setUp() throws RemoteException{
		server.registerClient(uuid, this);
	}

	@Test
	public void testCreateTransaction() throws RemoteException, SQLException{
		FLAG = 0;
		controller.createTransaction(id, cost, dateSql, custid);
		controller.getTransactionByDate(dateSql, S3Const.TASK_SHOW_TRANSACTION_BY_ID);
	}


	@Override
	public void receiveData(int taskType, Object data) throws Exception {
		// TODO Auto-generated method stub
		if(data == null){
			return;
		}
		Map tuple = (Map)(((List)data).get(0));
		Object value = 0;
		
		switch(FLAG){
		case 0:
			value = (String) tuple.get(S3Const.TABLE_TRANSACTION_ID);
			Assert.assertEquals("Tran1", value);
			break; 
		default:
			fail("Not yet implemented");
			break;
		}
	}
		
		
	
	@Override
	public void receiveMsg(String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void receiveBordercastData(int taskType, Object data) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTask(String className, Object... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
	


}
