package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import company.S3Const;

public class S3WarehouseStaffMenu extends S3Menu {

	public S3WarehouseStaffMenu(S3Application application) {
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
			System.out.println("1.Add Product Stock Level");
			System.out.println("2.Logout ");
			System.out.print("Please enter your option: ");
			optionNumber = scan.nextInt();

			switch (optionNumber){
			case 1:
				addProductStockLevel();
				break;
			case 2:
				app.logout();
				break;
			default:
				System.out.println("Please enter a valid option.");
			} 
		}while (optionNumber!= 2);

	}
	
	public void addProductStockLevel() throws RemoteException, SQLException{
		// find the product
		System.out.println("Please type in product ID: ");
		String id = scan.nextLine();
		S3Product product = app.getProductByBarcode(id);
		if(product == null){
			System.out.println("Invalid product ID!");
			return;
		}
		
		// find the delta quantity
		System.out.println("Please type in quantity: ");
		int qty = fetchIntFromInput("Please type in quantity: ", "Input has to be integer!");
		
		// update the database
		int newQty = product.stockLv + qty;
		app.getProductController().update(product.barcode, S3Const.TASK_UPDATE_PRODUCT_STOCK_LV, S3Const.TABLE_PRODUCT_STOCK_LV, newQty);
	}
	
	@Override
	void onReceiveData(int taskType, List<?> data) {
		
		switch(taskType) {
			case S3Const.TASK_UPDATE_PRODUCT_STOCK_LV:
			break;
			default:
			break;
		}

	}
}