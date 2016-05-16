import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
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

public class S3CustomerControllerTest implements S3ClientIF {
	private static String uuid = UUID.randomUUID().toString();
	private static S3Server server;
	private static S3CustomerController controller;
	private static int FLAG = 0; 
	
	private static double newCash = 100.99;
	private static int newPoint = 50;
	private static String userID = "c0000000001";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			server = new S3Server();
			server.start();
			
			controller = new S3CustomerController(uuid, server);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.shutdown();
	}
	
	@Before
	public void setUp() throws RemoteException {
		server.registerClient(uuid, this);
	}

	@Test
	public void testUpdateCash() throws RemoteException, SQLException {
		FLAG = 0;
		controller.updateCash(userID, newCash);
		controller.onGetCustomerInfoByID(userID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	}
	
	@Test
	public void testUpdatePoint() throws RemoteException, SQLException {
		FLAG = 1;
		controller.updatePoint(userID, newPoint);
		controller.onGetCustomerInfoByID(userID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	}
	
	@Test
	public void testSelect() throws RemoteException, SQLException {
		FLAG = 2;

		controller.onGetCustomerInfoByID(userID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	}

	@Override
	public void revMsg(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revData(int classType, Object data) throws RemoteException {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		
		Map tuple = (Map) ((List)data).get(0);
		Object value = 0;
		
		switch(FLAG) {
		case 0:
			value = ((BigDecimal) tuple.get(S3Const.TABLE_CUSTOMER_CASH)).doubleValue();
			Assert.assertEquals(newCash, value);
			break;
		case 1:
			value = ((BigDecimal) tuple.get(S3Const.TABLE_CUSTOMER_POINT)).intValue();
			Assert.assertEquals(newPoint, value);
			break;
		case 2:
			value = (String) tuple.get(S3Const.TABLE_USER_ID);
			Assert.assertEquals(userID, value);
			break;
		default:
			fail("Not yet implemented");
			break;
		}
	}

	@Override
	public void sendTask(String className, Object... args) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
