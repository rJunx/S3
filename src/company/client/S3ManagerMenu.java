package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import company.S3Const;
import company.S3StaffType;
import company.S3UserType;

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
			System.out.println(" 1.Update Price");
			System.out.println(" 2.Update Stock Level");
			System.out.println(" 3.Update Promotion discount to products ");
			System.out.println(" 4.Update Supplier information");
			System.out.println(" 5.Get Sales Report");
			System.out.println(" 6.Get Supplier Report ");//do not have enough info to work out the out of scope supplies
			System.out.println(" 7.Get Top 10 best sellers ");
			System.out.println(" 8.Add Product ");
			System.out.println(" 9.Add Customer ");
			System.out.println("10.Add Staff ");
			System.out.println("11.Show All Product Info");
			System.out.println("12.Logout ");
			System.out.print("Please enter your option: ");
			optionNumber = scan.nextInt();

			switch (optionNumber){
			case 1:
				onUpdatePrice();
				break;
			case 2:
				break;
			case 3:
				break;
			case 8:
				onAddProduct();
				break;
			case 9:
				break;
			case 10:
				onAddStaff();
				break;
			case 11:
				onShowAllProductInfo();
				break;
			case 12:
				app.logout();
				break;
			default:
				System.out.println("Please enter a valid option.");
			} 
		}while (optionNumber!=12);
	}
	
	private void onShowAllProductInfo() {
		ArrayList<S3Product> plist = app.getProductList();
		int l = plist.size();
		
		System.out.println("barcode" + "\t"
						+"name" + "\t"
						+"price" + "\t"
						+"stock level" + "\t"
						+"replenish level" + "\t"
						+"discount" + "\t"
						+"promotion" + "\t"
		);
		for (int i = 0; i < l; i++) {
			S3Product p = plist.get(i);
			System.out.println(p.barcode + '\t' + p.name + '\t' + p.price + '\t' + p.stockLv + '\t' + p.replenishLv + '\t' + p.discount + '\t' + p.promotion);
		}
	}
	
	private void onAddStaff() throws RemoteException, SQLException  {
		boolean flag = true;
		String id = "";
		do {
			try {
				System.out.println("Please enter staff ID: ");
				id = scan.next();
				flag = false;
				
				if (S3User.checkUserType(id) != S3UserType.STAFF) {
					throw new Exception();
				}
			} catch(Exception e) {
				flag = true;
				System.out.println("Invalid staff ID.");
			}
		} while (flag);
		
		int ntype = this.fetchIntFromInput("Please enter staff type\n0: sales staff\n1: warehouse staff\n2: manager", "Invalid number.");
		app.getStaffController().create(id, ntype);	
	}
	
	private void onUpdatePrice() throws RemoteException, SQLException {
		S3Product p = fecthProductByInput();
		double price = fetchDoubleFromInput("Please enter product price", "Invaild number.");
		app.getProductController().updatePrice(p.barcode, price, S3Const.TASK_UPDATE_PRODUCT_PRICE);
	}
	
	private void onAddProduct() throws RemoteException, SQLException {
		boolean flag = true;
		String barcode = null;
		do {
			barcode = this.fetchStringFromInput("Please enter product Barcode:", null);
			flag = false;
			S3Product p = app.getProductByBarcode(barcode);
			flag = p != null;
			if (flag) {
				System.out.println("Product exited.");
			}
		} while (flag);
		
		String name = fetchStringFromInput("Please enter product Name:", null);
		double price = fetchDoubleFromInput("Please enter product price", "Invaild number.");
		
		int stockLv = fetchIntFromInput("Please enter product stock level", "Invaild number.");
		int replenishLv = fetchIntFromInput("Please enter product replenish level", "Invaild number.");
		
		int discount = fetchIntFromInput("Please enter product discount", "Invaild number.");
		int promotion = fetchIntFromInput("Please enter product promotion", "Invaild number.");
		
		app.getProductController().create(barcode, name, price, stockLv, replenishLv, promotion, discount);
	}
	
	public void onReceiveData(int taskType, List<?> data) throws RemoteException, SQLException {
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
			String barcode = this.fetchStringFromInput("Please enter product Barcode:", null);
			p = app.getProductByBarcode(barcode);
			if (p == null) {
				System.out.println("Invalid Barcode.");
			}
		} while (p == null);

		return p;
	}
}
