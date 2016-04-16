
import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.server.S3Server;

import company.S3Const;
import company.S3GetProductTask;
import company.S3PAType;
import company.S3TableControlTask;
import company.S3TableOPType;

public class S3ProductTaskTest {
	private String uuid = UUID.randomUUID().toString();
	private static S3Server server;
	
	@BeforeClass
	public static void setUpBeforeClass() {

		try {
			server = new S3Server();
			server.start();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		server.shutdown();
	}
	
	@Test
	public void testGetByID() throws SQLException {
//		String productID = "0000000000";
//		S3GetProductTask t = new S3GetProductTask(uuid, S3PAType.ID, productID);
//
//		try {
//			t.run(null, server);
//			
//			List retList = t.getRetList();
//			
//			/*
//			if(retList.size() == 0) {
//				S3CreateCustomerTask t1 = new S3CreateCustomerTask(uuid, userID, 0, 0);
//				t1.run(null, server);
//				
//				t2.run(null, server);
//				retList = t2.getRetList();
//			}*/
//
//			Map tuple = (Map)retList.get(0);
//			String id = (String) tuple.get("BARCODE");
//			Assert.assertEquals(id, productID);
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}

}
