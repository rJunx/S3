

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.server.S3Server;

import company.S3CreateCustomerTask;
import company.S3GetUserTask;
import company.S3UserType;

public class S3UserTaskTest {
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
	
	@Test(expected=SQLException.class)
	public void testCreate() throws SQLException, RemoteException {
		String userID = "c0000000003";
		S3CreateCustomerTask t1 = new S3CreateCustomerTask(uuid, userID, 0, 0);
		t1.run(null, server);
	}
	
	@Test
	public void testGet() throws SQLException {
		String userID = "c0000000003";
		S3GetUserTask t2 = new S3GetUserTask(uuid.toString(), S3UserType.CUSTOMER, userID);

		try {
			t2.run(null, server);
			
			List retList = t2.getRetList();
			
			if(retList.size() == 0) {
				S3CreateCustomerTask t1 = new S3CreateCustomerTask(uuid, userID, 0, 0);
				t1.run(null, server);
				
				t2.run(null, server);
				retList = t2.getRetList();
			}

			Map tuple = (Map)retList.get(0);
			String id = (String) tuple.get("ID");
			Assert.assertEquals(id, userID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
