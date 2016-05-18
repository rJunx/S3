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
		
		int option = -1;
		do{
			System.out.println("\n								User:" + app.getCurrentUser().getID());
			System.out.println("\n\n\n\t\tManager Menu \n");
			System.out.println("\tUpdate Unit Price						1");
			System.out.println("\tUpdate Stock Level					2");
			System.out.println("\tUpdate Product Promotion Plan			3");
			System.out.println("\tUpdate Supplier Information			4");
			System.out.println("\tUpdate Product Discount				5");
			System.out.println("\tUpdate Product Replenish Level		6");
//			System.out.println("\tUpdate Product Re-order Level			7");	// need to update database first
			
			System.out.println("\tGenerate Sales Report					8");
			System.out.println("\tGenerate Purchase Order Report		9");
			System.out.println("\tGenerate Supply Report				10"); //do not have enough info to work out the out of scope supplies
			System.out.println("\tGenerate Best-Seller Report			11");
			System.out.println("\tShow All Product Informaiton			12");
			
			System.out.println("\n\tAdd Product							13");
			System.out.println("\tAdd Staff								14");

			System.out.println("\n\tLog Out								0");

			option = scan.nextInt();

			switch (option){
			case 1:
				onUpdatePrice();
				break;
			case 2:
				onUpdateStockLv();
				break;
			case 3:
				onUpdatePromotionPlan();
				break;
			case 4:
				onUpdateSupplierInfo(); // INCOMPLETED
				break;
			case 5:
				onUpdateProductDiscount();
				break;
			case 6:
				onUpdateProductReplenishLv();
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			case 12:
				onShowAllProductInfo();
				break;
			case 13:
				onAddProduct();
				break;
			case 14:
				onAddStaff();
				break;
			case 0:
				app.logout();
				break;
				
			default:
				System.out.println("Please enter a valid option.");
			} 
		}while (option != 0);
	}
	
	// How to name the staff? No need to add anything at the front?
	private void onAddStaff() throws RemoteException, SQLException{
		boolean flag = true;
		String id = "";
		do {
			try {
				System.out.println("Please enter staff ID: ");
				id = scan.next();
				flag = false;
				
				if (S3User.checkUserType(id) != S3UserType.STAFF) {	//  should be "=="????
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
	
	private void onUpdateSupplierInfo(){
		
	}
	
	private void onUpdateProductReplenishLv() throws RemoteException, SQLException{
		S3Product p = fecthProductByInput();
		int replenishLv = fetchIntFromInput("Please enter product replenish level", "Invaild number.");
		app.getProductController().update(p.barcode, S3Const.TASK_UPDATE_PRODUCT_REPLENISH_LV, S3Const.TABLE_PRODUCT_REPLENISH_LV, replenishLv);
	}
	
	private void onUpdateProductDiscount() throws RemoteException, SQLException{
		S3Product p = fecthProductByInput();
		int discount = fetchIntFromInput("Please enter product discount (0-100)%", "Invaild number.");
		app.getProductController().update(p.barcode, S3Const.TASK_UPDATE_PRODUCT_DISCOUNT, S3Const.TABLE_PRODUCT_DISCOUNT, discount);
	}
	
	private void onUpdatePromotionPlan() throws RemoteException, SQLException{
		S3Product p = fecthProductByInput();
		int planNum = fetchIntFromInput("Please enter product promotion plan", "Invaild number.");
		app.getProductController().update(p.barcode, S3Const.TASK_UPDATE_PRODUCT_PROMOTION, S3Const.TABLE_PRODUCT_PROMOTION, planNum);
	}
	
	private void onUpdateStockLv() throws RemoteException, SQLException{
		S3Product p = fecthProductByInput();
		int stockLv = fetchIntFromInput("Please enter product stock level", "Invaild number.");
		app.getProductController().updateStockLevel(p.barcode, stockLv);
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
