package company;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import com.client.S3ClientIF;

public class S3Application {
	private Scanner scan = new Scanner(System.in);
	private S3ClientIF client;
	private static final int MAX_USERID_LENGTH = 11;

	public S3Application(S3ClientIF c) {
		client = c;
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
		System.out.println("\tExit                                 9");
		System.out.println("\n\t**************************************");
		System.out.print("\tYour choice : ");
		char ch = scan.nextLine().charAt(0);
		return ch;
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

	protected void sendLogin() throws RemoteException {
		System.out.print("Enter userID : ");
		String id = scan.nextLine();
		S3UserType type;

		if (id.length() != MAX_USERID_LENGTH || (type = checkUserType(id)) == S3UserType.UNKNOWN) {
			System.out.println("Invalid userID");
		} else {
			client.sendTask("S3LoginTask", type, id);
		}
	}
	
	protected void onLogin(List data) {
		if (data.isEmpty()) {
			System.out.println("User does not exit......");
		} else {
			System.out.println(data);
		}
	}
	
	public void onRevData( String taskType, List data ) {
		switch (taskType) {
		case "S3LoginTask" :
			onLogin(data);
			break;
		default :
			break;
		}
	}

	public void run() throws RemoteException {
		char ch;
		do {
			ch = menu();
			switch (ch) {
			case '8':
				sendLogin();
				break;
			}
		} while (ch != '9');
	}
}
