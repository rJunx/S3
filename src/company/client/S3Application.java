package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.server.S3ServerIF;

import company.S3Const;
import company.S3StaffType;
import company.S3UserType;

public class S3Application implements S3CustomerMenuIF{
	private S3ProductController productController;
	private S3CustomerController customerController;
	private S3StaffController staffController;
	private S3OrderItemController orderController;
	private S3TransactionController transactionController;
	
	private ArrayList<S3Product> productList = new ArrayList<S3Product>();
	private ArrayList<S3Customer> customerList = new ArrayList<S3Customer>();
	private ArrayList<S3Staff> staffList = new ArrayList<S3Staff>();
	private ArrayList<Transaction> transactList = new ArrayList<Transaction>();
	private ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
	private ArrayList<Supply> supplyList = new ArrayList<Supply>();
	private ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();
	
	private static final int TOTAL_SYNC_TASK = 1;
	private int finished_sync_task = 0;
	
	private S3User user;
	private S3Menu menu;
	private S3ServerIF server;
	
	private Scanner scan = new Scanner(System.in);
	
	// constructor
	public S3Application(String uuid, S3ServerIF server) {
		productController = new S3ProductController( uuid, server );
		customerController = new S3CustomerController( uuid, server );
		staffController = new S3StaffController( uuid, server );
		orderController = new S3OrderItemController(uuid, server);
		transactionController = new S3TransactionController(uuid, server);
		this.server = server;
	}
	
	// accessors
	public ArrayList<S3Product> getProductList(){return productList;}
	public ArrayList<S3Customer> getCustomerList(){return customerList;}
	public ArrayList<S3Staff> getStaffList(){return staffList;}
	public ArrayList<Transaction> getTransactionList(){return transactList;}
	public ArrayList<Supplier> getSupplierList(){return supplierList;}
	public ArrayList<Supply> getSupplyList(){return supplyList;}
	public ArrayList<OrderItem> getOrderItemList(){return orderItemList;}

	public S3ProductController getProductController() {return productController;}
	public S3CustomerController getCUstomerController() {return customerController;}
	public S3StaffController getStaffController() {return staffController;}
	public S3User getCurrentUser() { return user; }
	
	//Basic function for unknown user
	public char menu() {
		System.out.println("\n\n\n\t\tOrder Processing System\n");
		System.out.println("\tSearch Product by ID					1");
		System.out.println("\tSearch Product by List				2");
		System.out.println("\tAdd Product by Key-In ID				3");
		System.out.println("\tAdd Product from Name List			4");
		System.out.println("\tPay(Customer Log-in Required)			5");
		System.out.println("\tStaff Log-in							6");
		System.out.println("\tExit									e");
		System.out.println("\n\t**************************************");
		System.out.print("\tYour choice : ");
		char ch = scan.nextLine().charAt(0);
		return ch;
	}
	
	public void run() throws RemoteException, SQLException {
		syncAllData();
		
		char ch = ' ';
		do {
			if (menu != null) {
				menu.run();
			} else {
				ch = menu();
				switch (ch) {
				case '1':
					mSearchProdByID();
					break;
				case '2':
					mSearcyProdByList();
					break;
				case '3':
					mAddProdByID();
					break;
				case '4':
					mAddProdByList();
					break;
				case '5':
					onPurchase();
					break;
				case '6':
					sendStaffLogin();
					break;
				}
			}
		} while (ch != 'e');
	}
	
	public void waitForRes() {
		System.out.println("\nPress enter to continue");
		scan.nextLine();
	}
	
	private void mSearcyProdByList() {
		char exit = ' ';
		
		do{
			System.out.print("All the products are list below.");
			listAllProducts();
			System.out.print("Please select the ID for more informaiton.");
			int selected = scan.nextInt();
			System.out.println("ID: " + productList.get(selected).barcode);
			System.out.println("Name: " + productList.get(selected).name);
			System.out.println("Unit Price: " + productList.get(selected).price);
			System.out.println("Discount:  " + productList.get(selected).discount);
			
			System.out.println("\n Go back(Y/N)? Press Y for exist.");
		}while(exit == 'Y' || exit == 'y');
		
	}
	
	private void listAllProducts(){
		for(int i = 0; i < productList.size(); i++){
			System.out.println(i + "	" + productList.get(i).name);
		}
	}
	
	private void mSearchProdByID(){
		System.out.print("Please key-in product code: ");
		String input = scan.nextLine();
		
		
	}
	
	private S3Product searchProdByID(String id){
		for(int i = 0; i < productList.size(); i++){
			if(productList.get(i).barcode.equals(id)){
				return productList.get(i);
			}
		}
		
		///// ?!!!!!!!!!!!!!!  NOT COMPLETED
		return null;
	};
	
	
	private void mAddProdByID(){
		
	}
	
	private void mAddProdByList(){
		
	}
	
	private void sendStaffLogin() throws RemoteException, SQLException {
	    System.out.print("Enter Staff ID : ");  
	    String userID = scan.nextLine();
	    S3UserType userType = S3User.checkUserType(userID); 
	    	
	    if (userType == S3UserType.STAFF) {
	    	staffController.onGetStaffInfoByID(userID, S3Const.TASK_SHOW_STAFF_BY_ID);
	    } else {
	    	System.out.println("Invalid Staff ID"); 
	    }
	}
	
	private void receiveStaffLogin(List<?> data) {
		if (data == null || data.size() == 0) {
			System.out.println("Staff does not exit"); 
		} else {
		    S3Staff sf = new S3Staff((Map<?, ?>) data.get(0));
		    user = sf;
		    S3StaffType st = sf.getType();
			if (st == S3StaffType.MANAGER) {
				menu = new S3ManagerMenu(this);
			} else if (S3StaffType.SALES_STAFF == st) {
				menu = new S3SaleStaffMenu(this);
			} else if (S3StaffType.WAREHOUSE_STAFF == st) {
				menu = new S3WarehouseStaffMenu(this);
			}
		}
	}
	
	private void onPurchase() throws RemoteException, SQLException {
	    System.out.print("Enter Customer ID : ");  
	    String userID = scan.nextLine();
	    S3UserType userType = S3User.checkUserType(userID); 
	    	
	    if (userType == S3UserType.CUSTOMER) {
	    	customerController.onGetCustomerInfoByID(userID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	    } else {
	    	System.out.println("Invalid Customer ID"); 
	    }
	}
	
	private void onReceiveCustomer(List<?> data) {
		if (data == null || data.size() == 0) {
			System.out.println("Customer does not exit"); 
		} else {
			S3Customer c = new S3Customer((Map<?, ?>) data.get(0));
			System.out.println("Customer: " + c.getID() + " Balance: " + c.getBalance() + " Credit Point: " + c.getPoint());
			//Show final result
			
			System.out.println("Continue to buy(y/n)");
			char ch = scan.nextLine().charAt(0);
			if (ch == 'y') {
				// update database customer info and product info
			}
		}
	}
	
	public void logout() {
		menu = null;
		user = null;
	}
	
	public S3Product getProductByBarcode(String barCode) {
		S3Product p = null;
		for (int i = 0; i < productList.size(); i++) {
			p = productList.get(i);
			if(p.barcode.compareTo(barCode) == 0) {
				break; 
			} else {
				p = null;
			}
		}
		return p;
	}
	
	//-------------------------------Sync data---------------------------------------------
	//receive data from server(sender and receiver are the same client)
	public void onReceiveData(int taskType, Object data) throws RemoteException {
		if (finished_sync_task != TOTAL_SYNC_TASK) {
			//Sync data before start the application
			switch (taskType) {
			case S3Const.TASK_SYNC_PRODUCT:
				onSyncProduct((List<?>) data);
				finished_sync_task++;
				break;
			default:
				break;
			}
		} else {
			if (data != null) {
				HashMap<String, Object> args = new HashMap<>();
				args.put("taskType", taskType);
				args.put("data", data);
				switch(taskType) {
				case S3Const.TASK_SYNC_PRODUCT:
					server.broadcastMessage(args);
					break;
				}
			}
			
			if (menu != null) {
				menu.onReceiveData(taskType, (List<?>) data);
			} else {
				switch (taskType) {
				case S3Const.TASK_SHOW_ALL_PRODUCTS:
					onShowAllProducts((List<?>) data);
					break;
				case S3Const.TASK_SHOW_STAFF_BY_ID:
					receiveStaffLogin((List<?>) data);
					break;
				case S3Const.TASK_SHOW_CUSTOMER_BY_ID:
					onReceiveCustomer((List<?>) data);
					break;
				default:
					break;
				}	
			}
		}
	}
	
	//receive border cast data(server sends the data to all connected clients, mainly for sync data)
	public void onReceiveBordercastData( int taskType, Object data ) throws RemoteException {
		if (data == null) {
			return;
		}
		switch(taskType) {
		case S3Const.TASK_SYNC_PRODUCT:
			onSyncProduct((List<?>) data);
			break;
		//case S3Const.TASK_SYNC_CUSTOMER:
		//	onSyncCustomer((List<?>) data);
		//	break;
		}
	}
	
	//private void onSyncCustomer(List<?> data) {
	//	Map<?, ?> userInfo = (Map<?, ?>) data.get(0);
	//    String userID = (String)userInfo.get(S3Const.TABLE_USER_ID);
	//    if (user != null && user.getID().compareTo(userID) == 0) {
	//    	((S3Customer)user).update(userInfo);
	//    }
	//}
	
	private void onSyncProduct(List<?> data) {
		if (productList.size() == 0) {
			for(int i = 0; i < data.size(); i++){
				Map<?, ?> row = (Map<?, ?>)data.get(i);
				S3Product prod = new S3Product(row);
				productList.add(prod);
			}
		} else {
			for(int i = 0; i < data.size(); i++){
				Map<?, ?> row = (Map<?, ?>)data.get(i);
				String barcode = (String)row.get(S3Const.TABLE_PRODUCT_ID);
				boolean found = false;
				for (int j = 0; j < productList.size(); j++) {
					S3Product p = productList.get(j);
					if (p.barcode.compareTo(barcode) == 0) {
						p.update(row);
						found = true;
						break;
					}
				}
				
				if(!found) {
					S3Product prod = new S3Product(row);
					productList.add(prod);
				}
			}
		}
	}

	private void syncAllData() throws RemoteException, SQLException {
		productController.postGetAllProduct(S3Const.TASK_SYNC_PRODUCT);
	}
	
	//------------------------------need to arrangement--------------------------------------------------
	
	public static void changeBulkSalePlan(int planNo, int planItem, double value){
		S3OrderItemController.bulkSalePlan[planNo-1][planItem-1] = value;
	}
	
	// Get all the product information
	public void postShowAllProducts() throws RemoteException, SQLException {
		productController.postGetAllProduct(S3Const.TASK_SHOW_ALL_PRODUCTS);
	}
	
	public void onShowAllProducts( List<?> data ) {
		if(data != null && data.size() != 0){
			String barcode;
			String name;
			double price;
			int promotion;
			double discount;
			int stockLv;
			int replenishLv;
			String supplier;
			
			productList.clear();
			
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
				S3Product prod = new S3Product(barcode, name, price, promotion, discount, stockLv, replenishLv, supplier);
				productList.add(prod);
			}
		}
	}

	// Get one product information by the given ID
	public void postShowProductByID(String productID) throws RemoteException, SQLException{
		productController.postGetProductInfoByID(productID, S3Const.TASK_SHOW_PROD_BY_ID);
	}
	
	public void onShowProductByID(List<?> data){
		if(data != null && data.size() != 0){
			String barcode;
			String name;
			double price;
			int promotion;
			double discount;
			int stockLv;
			int replenishLv;
			String supplier;
			
			productList.clear();
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
				S3Product prod = new S3Product(barcode, name, price, promotion, discount, stockLv, replenishLv, supplier);
				productList.add(prod);
			}
		}
	}


	// Get one customer information by the given ID
	public void postShowCustomerByID(String custID) throws RemoteException, SQLException{
		customerController.onGetCustomerInfoByID(custID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	} 

	public void onShowCustomerByID(List<?> data){
		if(data != null && data.size() != 0){
			Map row = (Map)data.get(0);
			String id = (String)row.get(S3Const.TABLE_USER_ID);
			double balance = (double)row.get(S3Const.TABLE_CUSTOMER_BALANCE);
			int points = (int)row.get(S3Const.TABLE_CUSTOMER_POINT);
			S3Customer targetCust = new S3Customer(id, balance, points);
			customerList.clear();
			customerList.add(targetCust);
		}
	}
	
	//	Get one staff member information by the given ID
	public void postShowStaffByID(String staffID) throws RemoteException, SQLException{
		customerController.onGetCustomerInfoByID(staffID, S3Const.TASK_SHOW_STAFF_BY_ID);
	}
	
	public void onShowStaffByID(List<?> data){
		if(data.size() != 0){
			Map row = (Map)data.get(0);
			String id = (String)row.get(S3Const.TABLE_USER_ID);
			int type = (int)row.get(S3Const.TABLE_STAFF_TYPE);
			S3Staff targetStaff = new S3Staff(id,type);
			staffList.clear();
			staffList.add(targetStaff);
		}
	}

	// Get transaction on a given date
	public void postShowTransactionByDate(Date date)throws RemoteException, SQLException{
	// ?????????????????   util.Date converted to sql.date
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		transactionController.getTransactionByDate(sqlDate, S3Const.TASK_SHOW_TRANSACTION_BY_ID);
	}

	public void postShowTransactionByDate(List<?> data){
		if(data.size() != 0){
			String id;
			double cost;
			Date date;
			String custID;
			
			transactList.clear();
			
			for(int i = 0; i < data.size(); i++){
				Map row = (Map)data.get(i);
				id = (String)row.get(S3Const.TABLE_TRANSACTION_ID);
				cost = (double)row.get(S3Const.TABLE_TRANSACTION_COST);
				// !!!!!!!!! Date-related
				date = (Date)row.get(S3Const.TABLE_TRANSACTION_DATE);
				custID = (String)row.get(S3Const.TABLE_TRANSACTION_CUST_ID);
				Transaction transaction = new Transaction(id, cost, date, custID);
				
				transactList.add(transaction);
			}
		}
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
	public double getTotalPrice(HashMap<S3Product, Double> orderList, int custPoints){
		double cost = 0.0;
		double priceInProgress = 0.0;
		
		for(Map.Entry<S3Product, Double> entry : orderList.entrySet()){
			S3Product prod = entry.getKey();
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
	public double calcTotalCost(ArrayList<S3Product> prodInCart, S3Customer customer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void postShowSaleStaffByID(String staffID) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postUpdateCustomerByID(S3Customer customer) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postUpdateProductStockLevel(ArrayList<S3Product> products) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		
	}
}
