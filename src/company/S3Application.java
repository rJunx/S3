package company;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import com.server.S3ServerIF;

public class S3Application {
	private Scanner scan = new Scanner(System.in);
	private static final int MAX_USERID_LENGTH = 11;

	private S3ProductController productController;
	private S3CustomerController customerController;
	
	public S3Application(String uuid, S3ServerIF server) {
		productController = new S3ProductController( uuid, server );
		customerController = new S3CustomerController( uuid, server );
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
	
	private void onShowProduct( List data ) {
		System.out.println(data);
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
	
	public void onRevData( int taskType, List data ) {
		switch (taskType) {
		case S3Const.TASK_SHOW_ALL_PRODUCTS:
			onShowProduct(data);
			break;
			
		default:
			break;
		}
		
		waitForRes();
	}

	public void onRevData( S3TaskMsg taskMsg, List data ) {
		System.out.println(data);
	}
	
	public void run() throws RemoteException, SQLException {
		char ch;
		do {
			ch = menu();
			switch (ch) {
			case '8':
				break;
			case '9':
				postShowProduct();
				break;
			}
		} while (ch != 'e');
	}
}
