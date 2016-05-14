package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.server.S3ServerIF;

import company.S3Const;
import company.S3UserType;

public class S3Application implements S3CustomerMenuIF{


	private Scanner scan = new Scanner(System.in);

	private S3ProductController productController;
	private S3CustomerController customerController;
	private S3StaffController staffController;
	private S3OrderItemController orderController;
	private S3TransactionController transactionController;
	
	
	private S3CustomerMenu customerMenu;

	
	public S3Application(String uuid, S3ServerIF server) {
		productController = new S3ProductController( uuid, server );
		customerController = new S3CustomerController( uuid, server );
		staffController = new S3StaffController( uuid, server ); 
		orderController = new S3OrderItemController(uuid, server);
		transactionController = new S3TransactionController(uuid, server);
	}
	
	public static void changeBulkSalePlan(int planNo, int planItem, double value){
		S3OrderItemController.bulkSalePlan[planNo-1][planItem-1] = value;
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
		System.out.println("\tInput Products					   c");
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
	

	private S3UserType checkUserType(String userID) {
		if (userID.charAt(0) == 'c') {
			return S3UserType.CUSTOMER;
		} else if (userID.charAt(0) == 's') {
			return S3UserType.STAFF;
		} else {
			return S3UserType.UNKNOWN;
		}
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

	// ***************************************** "run" ***************************************************************************
		public void run() throws RemoteException, SQLException {
			char ch;
			do {
				ch = menu();
				switch (ch) {
				case '8':
					postLogin();
					break;
				case '9':
					postShowAllProducts();
					break;
				}
			} while (ch != 'e');
		}
	
	// ***************************************** onRevDate ***************************************************************************
		public void onRevData( int taskType, Object data ) {
			switch (taskType) {
			case S3Const.TASK_SHOW_ALL_PRODUCTS:
				onShowAllProducts((List<?>)data);
				break;
			case S3Const.TASK_LOGIN:
				onLogin((List<?>)data);
				break;
			case S3Const.TASK_SHOW_PROD_BY_ID:
				onShowProductByID((List<?>) data);
				break;
			case S3Const.TASK_SHOW_CUSTOMER_BY_ID:
				onShowCustomerByID((List<?>) data);
				break;
			default:
				break;
			}
			
			waitForRes();
		}
		
	// ***************************************** END OF "onRevData" ***************************************************************************	
		
	
	public void postShowAllProducts() throws RemoteException, SQLException {
		productController.postGetAllProduct(S3Const.TASK_SHOW_ALL_PRODUCTS);
	}
	
	public void onShowAllProducts( List<?> data ) {
		ArrayList<Product> results = new ArrayList<Product>();
		String barcode;
		String name;
		double price;
		int promotion;
		double discount;
		int stockLv;
		int replenishLv;
		String supplier;
		for(int i = 0; i < data.size(); i++){
			Map row = (Map)data.get(i);
			barcode = (String)row.get(S3Const.TABLE_PRODUCT_ID);
			name = (String)row.get(S3Const.TABLE_PRODUCT_NAME);
			price = (double)row.get(S3Const.TABLE_PRODUCT_PRICE);
			promotion = (int)row.get(S3Const.TABLE_PRODUCT_PROMOTION);
			discount = (double)row.get(S3Const.TABLE_PRODUCT_DISCOUNT);
			stockLv = (int)row.get(S3Const.TABLE_PRODUCT_STOCK_LV);
			replenishLv = (int)row.get(S3Const.TABLE_PRODUCT_REPLENISH_LV);
			supplier = (String)row.get(S3Const.TABLE_PRODUCT_SUPPLIER);
			Product prod = new Product(barcode, name, price, promotion, discount, stockLv, replenishLv, supplier);
			results.add(prod);
		}
		customerMenu.onRevData(S3Const.TASK_SHOW_ALL_PRODUCTS, results) ;
	}
	

	
	public void postShowProductByID(String productID) throws RemoteException, SQLException{
		productController.postGetProductInfoByID(productID, S3Const.TASK_SHOW_PROD_BY_ID);
	}
	
	public void onShowProductByID(List data){
		ArrayList<Product> results = new ArrayList<Product>();
		String barcode;
		String name;
		double price;
		int promotion;
		double discount;
		int stockLv;
		int replenishLv;
		String supplier;
		for(int i = 0; i < data.size(); i++){
			Map row = (Map)data.get(i);
			barcode = (String)row.get(S3Const.TABLE_PRODUCT_ID);
			name = (String)row.get(S3Const.TABLE_PRODUCT_NAME);
			price = (double)row.get(S3Const.TABLE_PRODUCT_PRICE);
			promotion = (int)row.get(S3Const.TABLE_PRODUCT_PROMOTION);
			discount = (double)row.get(S3Const.TABLE_PRODUCT_DISCOUNT);
			stockLv = (int)row.get(S3Const.TABLE_PRODUCT_STOCK_LV);
			replenishLv = (int)row.get(S3Const.TABLE_PRODUCT_REPLENISH_LV);
			supplier = (String)row.get(S3Const.TABLE_PRODUCT_SUPPLIER);
			Product prod = new Product(barcode, name, price, promotion, discount, stockLv, replenishLv, supplier);
			results.add(prod);
		}
		customerMenu.onRevData(S3Const.TASK_SHOW_PROD_BY_ID, results) ;
	}



	public void postShowCustomerByID(String custID) throws RemoteException, SQLException{
		customerController.onGetCustomerInfoByID(custID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	} 

	public void onShowCustomerByID(List data){
		Map row = (Map)data.get(0);
		String id = (String)row.get(S3Const.TABLE_USER_ID);
		double balance = (double)row.get(S3Const.TABLE_CUSTOMER_CASH);
		int points = (int)row.get(S3Const.TABLE_CUSTOMER_POINT);
		Customer targetCust = new Customer(id, balance, points);
		customerMenu.onRevData(S3Const.TASK_SHOW_CUSTOMER_BY_ID, targetCust);
	}
	
	




	
	
// ----- Transaction-related calculation--------

	
	public double checkPromotionRate(int planID, double qty){
		if(qty > 30){
			return S3OrderItemController.bulkSalePlan[planID][2];
		}else if(qty > 20){
			return S3OrderItemController.bulkSalePlan[planID][1];
		}else if(qty > 10){
			return S3OrderItemController.bulkSalePlan[planID][0];
		}else 
			return 0.0;
	}

	
	public double priceAfterDiscount(double price, double discount){
		return price*(1-discount);
	}
		
	public double priceAfterPromotion(int promotion, double qty, double price){
		return (1 - checkPromotionRate(promotion, qty)) * price; 
	}

	public double priceAfterCustPoints(int points, double price){
		if((price - (int)(points/20)* 5) >= 0)
			return (price - (int)(points/20)* 5);
		else 
			return 0.0;
	}
	
	// to get the total price based on the given (product + qty)
	public double getTotalPrice(HashMap<Product, Double> orderList, int custPoints){
		double cost = 0.0;
		double priceInProgress = 0.0;
		
		for(Map.Entry<Product, Double> entry : orderList.entrySet()){
			Product prod = entry.getKey();
			double qty = entry.getValue();
			
			priceInProgress = priceAfterDiscount(prod.price,prod.discount);
			priceInProgress = priceAfterPromotion(prod.promotion, qty, priceInProgress);
			priceInProgress = priceInProgress * qty;
			cost += priceInProgress;
		}
		cost = priceAfterCustPoints(custPoints, cost);
		return cost;
	}

	@Override
	public double calcTotalCost(ArrayList<Product> prodInCart, Customer customer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void postShowSaleStaffByID(String staffID) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postUpdateCustomerByID(Customer customer) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postUpdateProductStockLevel(ArrayList<Product> products) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		
	}
	

}
