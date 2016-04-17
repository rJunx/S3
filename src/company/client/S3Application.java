package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.server.S3ServerIF;

import company.S3Const;
import company.S3UserType;

public class S3Application {
	private Scanner scan = new Scanner(System.in);

	private S3ProductController productController;
	private S3CustomerController customerController;
	private S3StaffController staffController;
	
	public S3Application(String uuid, S3ServerIF server) {
		productController = new S3ProductController( uuid, server );
		customerController = new S3CustomerController( uuid, server );
		staffController = new S3StaffController( uuid, server ); 
	}

	public char menu() {
		System.out.println("\n\n\n\t\tOrder Processing System\n");
		System.out.println("\tPrint All Orders for Product         0");
		System.out.println("\tNew Order                            1");
		System.out.println("\tAdd New Customer                     2");
		System.out.println("\tAdd New Product                      3");
		System.out.println("\tDespatch Order and Generate Invoice  4");
		System.out.println("\tCancel Order                         5");
		System.out.println("\tReceive Payment                      6");
		System.out.println("\tPrint Orders                         7");
		System.out.println("\tLogin                                8");
		System.out.println("\tShow Products                        9");
		System.out.println("\tExit                                 e");
		System.out.println("\n\t**************************************");
		System.out.print("\tYour choice : ");
		char ch = scan.nextLine().charAt(0);
		return ch;
	}
	
	public void waitForRes() {
		System.out.println("\nPress enter to continue");
		scan.nextLine();
	}
	
	private void postShowProduct() throws RemoteException, SQLException {
		productController.postGetAllProduct(S3Const.TASK_SHOW_ALL_PRODUCTS);
		
	}
	
	private void onShowProduct( List<?> data ) {
		System.out.println(data);
	}
	
	private void postLogin() throws RemoteException, SQLException {
	    System.out.print("Enter userID : ");  
	    String userID = scan.nextLine();
	    S3UserType userType = checkUserType(userID);
	    
	    if (userType == S3UserType.CUSTOMER) {
	    	customerController.onGetCustomerInfoByID(userID, S3Const.TASK_LOGIN);
	    } else if (userType == S3UserType.STAFF) {
	    	staffController.onGetStaffInfoByID(userID, S3Const.TASK_LOGIN);
	    } else {
	    	System.out.println("Invalid userID"); 
	    }
	}
	
	private void onLogin(List data) {
		if (data == null || data.size() == 0) {
			System.out.println("User does not exit"); 
		} else {
			System.out.println("Welcome " + ((Map) data.get(0)).get(S3Const.TABLE_USER_ID));
		}
	}

	private S3UserType checkUserType(String userID) {
		if (userID.charAt(0) == 'c') {
			return S3UserType.CUSTOMER;
		} else if (userID.charAt(0) == 's') {
			return S3UserType.STAFF;
		} else {
			return S3UserType.UNKNOWN;
		}
	}
	
	public void onRevData( int taskType, Object data ) {
		switch (taskType) {
		case S3Const.TASK_SHOW_ALL_PRODUCTS:
			onShowProduct((List<?>)data);
			break;
		case S3Const.TASK_LOGIN:
			onLogin((List<?>)data);
			break;
		default:
			break;
		}
		
		waitForRes();
	}
	
	public void run() throws RemoteException, SQLException {
		char ch;
		do {
			ch = menu();
			switch (ch) {
			case '8':
				postLogin();
				break;
			case '9':
				postShowProduct();
				break;
			}
		} while (ch != 'e');
	}
}
