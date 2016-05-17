package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import company.S3Const;

public class S3ManagerMenu extends S3Menu {
	public S3ManagerMenu(S3Application application) {
		super(application);
		// TODO Auto-generated constructor stub
	}

	@Override
	void run() throws RemoteException, SQLException {
		// TODO Auto-generated method stub

		/* Read user's option and implement the corresponding task*/
		int optionNumber;//option number from menu, selected by user
		
		do{
			System.out.println();
			System.out.println("								User:" + app.getCurrentUser().getID());
			System.out.println("Which of the following manager task would you like to do?");
			System.out.println("1.Update Price");
			System.out.println("2.Update Stock Level");
			System.out.println("3.Update Promotion discount to products ");
			System.out.println("4.Update Supplier information");
			System.out.println("5.Get Sales Report");
			System.out.println("6.Get Supplier Report ");//do not have enough info to work out the out of scope supplies
			System.out.println("7.Get Top 10 best sellers ");
			System.out.println("8.Logout ");
			System.out.print("Please enter your option: ");
			optionNumber = scan.nextInt();

			switch (optionNumber){
			case 1:
				updatePrice();
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
	
	private void updatePrice() throws RemoteException, SQLException {
		S3Product p = fecthProductByInput();
		double price = fetchDoubleByInput();
		app.getProductController().updatePrice(p.barcode, price, S3Const.TASK_UPDATE_PRODUCT_PRICE);
	}
	
	public void onReceiveData(int taskType, List<?> data) {
		switch(taskType) {
		case S3Const.TASK_UPDATE_PRODUCT_PRICE:
			break;
			default:
				break;
		}
	}

	private S3Product fecthProductByInput() {
		S3Product p = null;
		do {
			try {
				System.out.println("Please enter product Barcode:");
				String barcode = scan.next();
				
				p = app.getProductByBarcode(barcode);
				if (p == null) {
					throw new Exception();
				}
			} catch(Exception e) {
				p = null;
				System.out.println("Invalid Barcode.");
			}
		} while (p == null);

		return p;
	}
}
