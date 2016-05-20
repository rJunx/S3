package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import company.S3Const;
import company.S3Product;

public class S3WarehouseStaffMenu extends S3Menu {

	public S3WarehouseStaffMenu(S3Application application) {
		super(application);
		// TODO Auto-generated constructor stub
	}

	@Override
	void run() throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		int option;//option number from menu, selected by user
		
		do{
			String fm = "\t%-40s%-3s";
			System.out.println(String.format("\n%49s", "Staff: " + app.getCurrentUser().getID()));
			System.out.println("\t\t Manager Menu \t\t");
			
			System.out.println(String.format(fm, "Add Product Stock Level", "1"));
			System.out.println(String.format(fm, "Log Out", "0"));
			
			System.out.println("\n\t******************************************");
			System.out.print("\tYour choice : ");
			option = scan.nextInt();

			switch (option){
			case 1:
				addProductStockLevel();
				break;
			case 0:
				app.logout();
				break;
			default:
				System.out.println("Please enter a valid option.");
			} 
		}while (option != 0);

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