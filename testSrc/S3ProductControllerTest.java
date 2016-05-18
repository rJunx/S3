

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
import company.S3TableOPType;
import company.client.S3ProductController;
import company.server.S3TableControlTask;

public class S3ProductControllerTest implements S3ClientIF {
	private static String uuid = UUID.randomUUID().toString();
	private static S3Server server;
	private static S3ProductController controller;
	private static int FLAG = 0; 
	
	private static double newPrice = 10.33;
	private static int newStockLv = 100;
	private static int newRepenishLv = 50;
	private static String productID = "0000000000";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			server = new S3Server();
			server.start();
			
			controller = new S3ProductController(uuid, server);
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
	public void testUpdatePrice() throws RemoteException, SQLException {
		FLAG = 0;
		controller.updatePrice(productID, newPrice);
		controller.postGetProductInfoByID(productID, S3Const.TASK_SHOW_PROD_BY_ID);
	}
	
	@Test
	public void testUpdateStockLevel() throws RemoteException, SQLException {
		FLAG = 1;
		controller.updateStockLevel(productID, newStockLv);
		controller.postGetProductInfoByID(productID, S3Const.TASK_SHOW_PROD_BY_ID);
	}
	
	@Test
	public void testUpdateReplenishLevel() throws RemoteException, SQLException {
		FLAG = 2;
		controller.updateReplenishLevel(productID, newRepenishLv);
		controller.postGetProductInfoByID(productID, S3Const.TASK_SHOW_PROD_BY_ID);
	}

	@Test
	public void testSelect() throws RemoteException, SQLException {
		FLAG = 3;

		controller.postGetProductInfoByID(productID, S3Const.TASK_SHOW_PROD_BY_ID);
	}
	
	@Test
	public void testCreate() throws RemoteException, SQLException {
		FLAG = 4;

		controller.create("0000000004", "Product_4", newPrice, newStockLv, newRepenishLv, 0, 0);
		controller.postGetProductInfoByID("0000000004", S3Const.TASK_SHOW_PROD_BY_ID);
	}
	
	@Test
	public void testCount() throws Exception {
		S3TableControlTask countTask = new S3TableControlTask(S3Const.TABLE_PRODUCT, S3TableOPType.COUNT, null, null);
		countTask.run(server);
		List<?> result = (List<?>)countTask.getResult();
		
		BigDecimal count = (BigDecimal)((Map<?, ?>)result.get(0)).get(S3Const.TABLE_COUNT_RESULT);
		
		System.out.print(count);
	}
	
	
	@Override
	public void receiveMsg(String msg) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void receiveData(int classType, Object data) throws RemoteException {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		
		Map tuple = (Map) ((List) data).get(0);
		Object value = 0;
		
		switch(FLAG) {
		case 0:
			value = ((BigDecimal) tuple.get(S3Const.TABLE_PRODUCT_PRICE)).doubleValue();
			Assert.assertEquals(newPrice, value);
			break;
		case 1:
			value = ((BigDecimal) tuple.get(S3Const.TABLE_PRODUCT_STOCK_LV)).intValue();
			Assert.assertEquals(newStockLv, value);
			break;
		case 2:
			value = ((BigDecimal) tuple.get(S3Const.TABLE_PRODUCT_REPLENISH_LV)).intValue();
			Assert.assertEquals(newRepenishLv, value);
			break;
		case 3:
			value = (String) tuple.get(S3Const.TABLE_PRODUCT_ID);
			Assert.assertEquals(productID, value);
			break;
		case 4:
			value = (String) tuple.get(S3Const.TABLE_PRODUCT_ID);
			Assert.assertEquals("0000000004", value);
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

	@Override
	public void receiveBordercastData(int taskType, Object data) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
