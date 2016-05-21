package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import company.S3Const;
import company.S3Product;
import company.S3StaffType;
import company.S3Supplier;
import company.S3User;
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
			String fm = "\t%-40s%-3s";
			System.out.println(String.format("\n%49s", "Staff: " + app.getCurrentUser().getID()));
			System.out.println("\t\t Manager Menu \t\t");
			
			System.out.println(String.format(fm, "Update Unit Price", "1"));
			System.out.println(String.format(fm, "Update Stock Level", "2"));
			System.out.println(String.format(fm, "Update Product Promotion Plan", "3"));
			System.out.println(String.format(fm, "Update Supplier Information", "4"));
			System.out.println(String.format(fm, "Update Product Discount", "5"));
			System.out.println(String.format(fm, "Update Product Replenish Level", "6"));
//			System.out.println(String.format(fm, "Update Product Re-order Level", "7")); // need to update database first
			
			System.out.println(String.format(fm, "Generate Sales Report", "8"));
			//System.out.println(String.format(fm, "Generate Purchase Order Report", "9"));
			System.out.println(String.format(fm, "Generate Supply Report", "10"));
			System.out.println(String.format(fm, "Generate Best-Seller Report", "11"));
			System.out.println(String.format(fm, "Show All Product Informaiton", "12"));

			System.out.println(String.format(fm, "Add Product", "13"));
			System.out.println(String.format(fm, "Add Staff", "14"));
			System.out.println(String.format(fm, "Add Supplier", "15"));
			
			System.out.println(String.format(fm, "tLog Out", "0"));
			System.out.println("\n\t******************************************");
			System.out.print("\tYour choice : ");

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
				onGenSalesReport();
				break;
			case 9:
				break;
			case 10:
				onGenSupplyReport();
				break;
			case 11:
				onGenBestSellerReport();
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
			case 15:
				onAddSupplier();
				break;
			case 0:
				app.logout();
				break;
				
			default:
				System.out.println("Please enter a valid option.");
			} 
		}while (option != 0);
	}
	
	private void onGenSalesReport() throws RemoteException, SQLException {
		Date start = fetchDateFromInput("Please enter start date(yyyy-mm-dd):", "Invalid date.");
		Date end = fetchDateFromInput("Please enter end date(yyyy-mm-dd):", "Invalid date.");
		app.getReportController().generateSalesReport(S3Const.TASK_SALES_REPORT, start, end);
	}
	
	private void onGenSupplyReport() throws RemoteException, SQLException {
		Date start = fetchDateFromInput("Please enter start date(yyyy-mm-dd):", "Invalid date.");
		Date end = fetchDateFromInput("Please enter end date(yyyy-mm-dd):", "Invalid date.");
		app.getReportController().generateSupplyReport(S3Const.TASK_SUPPLY_REPORT, start, end);
	}
	
	private void onGenBestSellerReport() throws RemoteException, SQLException {
		Date start = fetchDateFromInput("Please enter start date(yyyy-mm-dd):", "Invalid date.");
		Date end = fetchDateFromInput("Please enter end date(yyyy-mm-dd):", "Invalid date.");
		app.getReportController().generateTop10SellerReport(S3Const.TASK_TOP_SELLER_REPORT, start, end);
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
		app.showAllProduct();
	}
	
	private void onUpdatePrice() throws RemoteException, SQLException {
		S3Product p = fecthProductByInput();
		double price = fetchDoubleFromInput("Please enter product price", "Invaild number.");
		app.getProductController().updatePrice(p.barcode, price, S3Const.TASK_UPDATE_PRODUCT_PRICE);
	}
	
	private void onAddSupplier() throws RemoteException, SQLException {
		String email = fetchStringFromInput("Please enter Supplier Email:", "Invaild Email.");
		app.getProductController().createSupplier(String.format("%010d", app.getSupplierList().size()+1), email);
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
		
		S3Supplier s = fetchSuppilerByInput();
		
		app.getProductController().create(barcode, name, price, stockLv, replenishLv, promotion, discount, s.id);
	}
	
	public void onReceiveData(int taskType, List<?> data) throws RemoteException, SQLException {
		switch(taskType) {
		case S3Const.TASK_UPDATE_PRODUCT_PRICE:
			break;
		//Print the report
		case S3Const.TASK_SUPPLY_REPORT:
			printSupplyReport(data);
			break;
		case S3Const.TASK_TOP_SELLER_REPORT:
			System.out.println("\nTop10 Seller Report:");
			printSalesReport(data);
			break;
		case S3Const.TASK_SALES_REPORT:
			System.out.println("\nSales Report:");
			printSalesReport(data);
			break;
			default:
				break;
		}
	}
	
	private void printSupplyReport(List<?> data) {
		System.out.println("\nSupply Report:");
		
		System.out.println(String.format("%-15s%-15s%-25s", "Supplier ID", "Barcode", "EMail"));
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> item = (Map<String, Object>) data.get(i);
			
			System.out.println(String.format("%-15s%-25s%-10.2f", 
					item.get(S3Const.TABLE_SUPPLIER_ID), 
					item.get(S3Const.TABLE_ORDERITEM_BARCODE),
					item.get(S3Const.TABLE_SUPPLIER_EMAIL)
					));
		}
	}
	
	private void printSalesReport(List<?> data) {
		System.out.println(String.format("%-15s%-25s%-10s", "Barcode", "Name", "Total($)"));
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> item = (Map<String, Object>) data.get(i);
			
			System.out.println(String.format("%-15s%-25s%-10.2f", 
					item.get(S3Const.TABLE_PRODUCT_ID), 
					item.get(S3Const.TABLE_PRODUCT_NAME),
					item.get("SUM(QUANTITY*PRICE)")
					));
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
	
	private S3Supplier fetchSuppilerByInput() {
		S3Supplier p = null;
		do {
			String id = this.fetchStringFromInput("Please enter product Suppiler:", null);
			p = app.getSuppilerByID(id);
			if (p == null) {
				System.out.println("Invalid Suppiler.");
			}
		} while (p == null);

		return p;
	}

}
