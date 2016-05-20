
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.server.S3Server;

import company.S3Const;
import company.S3StaffType;
import company.S3TableOPType;
import company.server.S3TableControlTask;

public class S3TableControlTaskTest {
	private static S3Server server;
	
	@BeforeClass
	public static void setUpBeforeClass() {

		try {
			server = new S3Server();
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		server.shutdown();
	}
	
	protected void update() throws SQLException {
		
	}
	
	@Test 
	public void testUpdate() throws SQLException {
		/*
		String productID = "0000000003";
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(S3Const.TABLE_PRODUCT_ID, productID);
		
		int newStockLv = 20;
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(S3Const.TABLE_PRODUCT_STOCK_LV, newStockLv);
		
		S3TableControlTask updateTask = new S3TableControlTask(S3Const.TABLE_PRODUCT, S3TableOPType.INSERT, values, conditions);
		*/
		List<Object> staffValue = new ArrayList<Object>();
		staffValue.add("s0000000000");
		staffValue.add(S3StaffType.MANAGER.ordinal());
		
		S3TableControlTask staffTask = new S3TableControlTask(S3Const.TABLE_STAFF, S3TableOPType.INSERT, staffValue, null);
		
		try {
			staffTask.run(server);
			//updateTask.run(server);
			
			/*
			S3TableControlTask selectTask = new S3TableControlTask(S3Const.TABLE_PRODUCT, S3TableOPType.SELECT, null, conditions);
			selectTask.run(server);
			List retList = (List)selectTask.getResult();
			
			Map tuple = (Map)retList.get(0);
			int lv = ((BigDecimal) tuple.get(S3Const.TABLE_PRODUCT_STOCK_LV)).intValue();
			
			Assert.assertEquals(lv, newStockLv);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSelect() throws SQLException {
		/*
		String productID = "0000000003";
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(S3Const.TABLE_PRODUCT_ID, productID);
		S3TableControlTask selectTask = new S3TableControlTask(S3Const.TABLE_PRODUCT, S3TableOPType.SELECT, null, m);
		
		try {
			selectTask.run(server);
			List retList = (List)selectTask.getResult();
			
			if(retList.size() == 0) {
				List<Object> values = new ArrayList<Object>();
				values.add(productID);
				values.add("Product_3"); //name
				values.add(200); 	//PRICE
				values.add(0);	//STOCKLV
				values.add(0);	//REPLENISHLV
				values.add(0);	//PROMOTION
				values.add(0);	//DISCOUNT
				
				S3TableControlTask createTask = new S3TableControlTask(S3Const.TABLE_PRODUCT, S3TableOPType.INSERT, values, null);
				createTask.run(server);
				
				selectTask.run(server);
				retList = (List) selectTask.getResult();
			}
			
			Map tuple = (Map)retList.get(0);
			String id = (String) tuple.get(S3Const.TABLE_PRODUCT_ID);
			
			System.out.print(retList);
			Assert.assertEquals(id, productID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
