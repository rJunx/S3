package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import company.S3UserType;

public class S3SaleStaffMenu extends S3Menu {

	public S3SaleStaffMenu(S3Application application) {
		super(application);
		// TODO Auto-generated constructor stub
	}

	@Override
	void run() throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		int optionNumber;//option number from menu, selected by user
		
		do{
			System.out.println();
			System.out.println("								User:" + app.getCurrentUser().getID());
			System.out.println("1.Update User Balance");
			System.out.println("8.Logout ");
			System.out.print("Please enter your option: ");
			optionNumber = scan.nextInt();

			switch (optionNumber){
			case 1:
				updateBalance();
				break;
			case 2:
				break;
			case 3:
				break;
			case 8:
				app.logout();
				break;
			default:
				System.out.println("Please enter a valid option.");
			} 
		}while (optionNumber!=8);
	}

	@Override
	void onReceiveData(int taskType, List<?> data) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateBalance() throws RemoteException, SQLException {
		boolean flag = true;
		String id = "";
		do {
			try {
				System.out.println("Please enter customer ID: ");
				id = scan.next();
				flag = false;
				
				if (S3User.checkUserType(id) != S3UserType.CUSTOMER) {
					throw new Exception();
				}
			} catch(Exception e) {
				flag = true;
				System.out.println("Invalid customer ID.");
			}
		} while (flag);
		
		double value = this.fetchDoubleByInput();
		app.getCUstomerController().updateBalance(id, value);
	}
}
