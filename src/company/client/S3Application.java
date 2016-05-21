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
import company.S3Customer;
import company.S3OrderItem;
import company.S3Product;
import company.S3Staff;
import company.S3StaffType;
import company.S3Supplier;
import company.S3Supply;
import company.S3Transaction;
import company.S3TransactionCalculation;
import company.S3User;
import company.S3UserType;

public class S3Application{
	private S3ProductController productController;
	private S3CustomerController customerController;
	private S3StaffController staffController;
	private S3OrderItemController orderController;
	private S3TransactionController transactionController;
	private S3ReportController reportController;
	
	private ArrayList<S3Product> productList = new ArrayList<S3Product>();
	private ArrayList<S3Customer> customerList = new ArrayList<S3Customer>();
	private ArrayList<S3Staff> staffList = new ArrayList<S3Staff>();
	private ArrayList<S3Transaction> transactList = new ArrayList<S3Transaction>();
	private ArrayList<S3Supplier> supplierList = new ArrayList<S3Supplier>();
	private ArrayList<S3Supply> supplyList = new ArrayList<S3Supply>();
	private ArrayList<S3OrderItem> orderItemList = new ArrayList<S3OrderItem>();
	
	private HashMap<S3Product, Integer> productInCart = new HashMap<S3Product, Integer>();
	
	private static final int TOTAL_SYNC_TASK = 2;
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
		reportController = new S3ReportController(uuid, server);
		this.server = server;
	}
	
	// accessors
	public ArrayList<S3Product> getProductList(){return productList;}
	public ArrayList<S3Customer> getCustomerList(){return customerList;}
	public ArrayList<S3Staff> getStaffList(){return staffList;}
	public ArrayList<S3Transaction> getTransactionList(){return transactList;}
	public ArrayList<S3Supplier> getSupplierList(){return supplierList;}
	public ArrayList<S3Supply> getSupplyList(){return supplyList;}
	public ArrayList<S3OrderItem> getOrderItemList(){return orderItemList;}

	public HashMap<S3Product, Integer> getProdInCart(){return productInCart;}
	
	public S3ProductController getProductController() {return productController;}
	public S3CustomerController getCustomerController() {return customerController;}
	public S3StaffController getStaffController() {return staffController;}
	public S3ReportController getReportController() {return reportController;}
	public S3User getCurrentUser() {return user;}
	
	//Basic function for unknown user
	public char menu() {
		String fm = "\t%-40s%-3s";
		System.out.println("\n\t\t Supermarket Support System \t\t");
		System.out.println(String.format(fm, "Search Product by ID", "1"));
		System.out.println(String.format(fm, "Search Product by List", "2"));
		System.out.println(String.format(fm, "Show All Product", "3"));
		System.out.println(String.format(fm, "Select Product by Key-In ID", "4"));
		System.out.println(String.format(fm, "Select Product from Name List", "5"));
		System.out.println(String.format(fm, "Pay(Customer Log-in Required)", "6"));
		System.out.println(String.format(fm, "Staff Log-in", "7"));
		System.out.println(String.format(fm, "Exit", "e"));
		System.out.println("\n\t******************************************");
		System.out.print("\tYour choice : ");
		char ch = scan.next().charAt(0);
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
					showAllProduct();
					break;
				case '4':
					mSelectProdByID();
					break;
				case '5':
					mSelectProdByList();
					break;
				case '6':
					onPurchase();
					break;
				case '7':
					sendStaffLogin();
					break;
				}
			}
		} while (ch != 'e');
	}
	
	public void waitForRes() {
		System.out.println("\nPress enter to continue");
		scan.next();
	}
	
	public void showAllProduct() {
		int l = productList.size();
		
		System.out.println();
		System.out.println("Products:");
		System.out.println(String.format("%-15s%-25s%-13s%-16s%-16s%-25s%-25s", "barcode", "name", "price($)", "stock level", "discount(%)", "promotion", "Supplier Email"));
		for (int i = 0; i < l; i++) {
			S3Product p = productList.get(i);
			S3Supplier s = this.getSuppilerByID(p.supplier);
			System.out.println(String.format("%-15s%-25s%-13.2f%-16d%-16d%-25s%-25s", p.barcode, p.name, p.price, p.stockLv, p.discount, S3OrderItemController.getPromotionTips(p.promotion), s.email));
		}
		System.out.println();
	}
	
	public void mSearcyProdByList() {
		char exit = ' ';
		
		do{
			System.out.print("All the products are list below.");
			listAllProducts();
			System.out.print("Please select the ID for more informaiton.");
			int selected = scan.nextInt();
			
			printOneProductInfo(productList.get(selected));
			
			System.out.println("\n Go back(Y/N)? Press Y for exist.");
			exit = scan.next().charAt(0);
		}while(exit != 'Y' && exit != 'y');
		
	}
	
	public void listAllProducts(){
		for(int i = 0; i < productList.size(); i++){
			System.out.println(i + "	" + productList.get(i).name);
		}
	}
	
	public void printOneProductInfo(S3Product product){
		System.out.println("ID: " + product.barcode);
		System.out.println("Name: " + product.name);
		System.out.println("Unit Price: " + product.price);
		System.out.println("Discount:  " + product.discount);
	}
		
	public void mSearchProdByID(){
		char exit = ' ';
		do{
			System.out.print("Please key-in product code: ");
			String prodID = scan.next();
			S3Product product = getProductByBarcode(prodID);	
			
			if(product != null){
				printOneProductInfo(product);
			}else{
				System.out.println("Invalid product ID! Retry please!");
			}
			System.out.println("\n Go back(Y/N)? Press Y for exist.");	
			exit = scan.next().charAt(0);
		}while(exit != 'Y' && exit != 'y');
	}
	
	public void mSelectProdByID(){
		char exit = ' ';
		do{
			System.out.println("Please key-in product code: ");
			String prodID = scan.next();
			S3Product product = getProductByBarcode(prodID);	
			
			if(product == null)
				System.out.println("Invalid product ID! Retry please!");
			
			System.out.println("Please key-in weight(kg)/quanitity: ");
			int qty = scan.nextInt();
			
			// check duplicates, stock level before adding
			
			if(qty > product.stockLv){
				System.out.println("Check your quantity! It's great than the stock level");
			}else{
				checkDuplicatesBeforeAdd(product, qty);
			}	
			
			System.out.println("\n Go back(Y/N)? Press Y for exist.");
			exit = scan.next().charAt(0);
		}while(exit != 'Y' && exit != 'y');
	}
	
	public void checkDuplicatesBeforeAdd(S3Product product, int qty){
		if(productInCart.containsKey(product)){
			int oldQty = productInCart.get(product);
			productInCart.put(product, qty + oldQty);
		}else{
			productInCart.put(product, qty);
		}
	}
	
	public void mSelectProdByList(){
		char exit = ' ';
		
		do{
			System.out.print("All the products are list below.");
			listAllProducts();
			System.out.print("Please select the ID to add in shopping list.");
			int selected = scan.nextInt();
			
			System.out.println("Please key-in weight(kg)/quanitity: ");
			int qty = scan.nextInt();
			
			// check duplicates, stock level before adding
			if(qty > productList.get(selected).stockLv){
				System.out.println("Check your quantity! It's great than the stock level!");
			}else{
				checkDuplicatesBeforeAdd(productList.get(selected), qty);
			}		
			
			System.out.println("\n Go back(Y/N)? Press Y for exist.");
			exit = scan.next().charAt(0);
		}while(exit != 'Y' && exit != 'y');
	}
	
	public void sendStaffLogin() throws RemoteException, SQLException {
	    System.out.print("Enter Staff ID : ");  
	    String userID = scan.next();
	    S3UserType userType = S3User.checkUserType(userID); 
	    	
	    if (userType == S3UserType.STAFF) {
	    	staffController.onGetStaffInfoByID(userID, S3Const.TASK_SHOW_STAFF_BY_ID);
	    } else {
	    	System.out.println("Invalid Staff ID"); 
	    }
	}
	
	public void receiveStaffLogin(List<?> data) {
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
	
	public void onPurchase() throws RemoteException, SQLException {
	    System.out.print("Enter Customer ID : ");  
	    String userID = scan.next();
	    S3UserType userType = S3User.checkUserType(userID); 
	    	
	    if (userType == S3UserType.CUSTOMER) {
	    	customerController.onGetCustomerInfoByID(userID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	    } else {
	    	System.out.println("Invalid Customer ID"); 
	    }
	}
	
	public void onReceiveCustomer(List<?> data) throws RemoteException, SQLException {
		
		if (data == null || data.size() == 0) {
			System.out.println("Customer does not exit"); 
			return;
		} else {
			S3Customer c = new S3Customer((Map<?, ?>) data.get(0));
			System.out.println("Customer: " + c.getID() + " Balance: " + c.getBalance() + " Credit Point: " + c.getPoint());
			//Show final result
			System.out.println("All the selected items: ");
			
			printProdInCart();
			
			// get total price
			double totalCost = getTotalPrice(productInCart, c.getPoint());
			
			System.out.println("Total cost: " + totalCost);
			
			if(c.getBalance() >= totalCost){
				System.out.println("\n No cancellation permits, continue to buy(y/n)");
				char ch = scan.next().charAt(0);
				if (ch == 'y') {
					this.transactionController.purchase(S3Const.TASK_TRANSACTION_PURCHASE, c, productInCart);
				}else{
					return;
				}
			}
		}
	}
	
	public void printProdInCart(){
		System.out.println(String.format("%-15s%-25s%-25s", "BarCode", "Name", "Quantity(kg/serving)"));
		
		for (Map.Entry<S3Product, Integer> entry : productInCart.entrySet()) {
		    S3Product product = entry.getKey();
		    double qty = entry.getValue();
		    System.out.println(String.format("%-15s%-25s%-25.2f", product.barcode, product.name, qty));
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
	
	public S3Supplier getSuppilerByID(String id) {
		S3Supplier p = null;
		for (int i = 0; i < supplierList.size(); i++) {
			p = supplierList.get(i);
			if(p.id.compareTo(id) == 0) {
				break; 
			} else {
				p = null;
			}
		}
		return p;
	}
	
	//-------------------------------Sync data---------------------------------------------
	//receive data from server(sender and receiver are the same client)
	public void onReceiveData(int taskType, Object data) throws Exception {
		if (finished_sync_task != TOTAL_SYNC_TASK) {
			//Sync data before start the application
			switch (taskType) {
			case S3Const.TASK_SYNC_PRODUCT:
				onSyncProduct((List<?>) data);
				finished_sync_task++;
				break;
			case S3Const.TASK_SYNC_SUPPLIER:
				onSyncSupplier((List<?>) data);
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
				case S3Const.TASK_SYNC_SUPPLIER:
					server.broadcastMessage(args);
					break;
				}
			}
			
			if (menu != null) {
				menu.onReceiveData(taskType, (List<?>) data);
			} else {
				switch (taskType) {
				case S3Const.TASK_SHOW_STAFF_BY_ID:
					receiveStaffLogin((List<?>) data);
					break;
				case S3Const.TASK_SHOW_CUSTOMER_BY_ID:
					onReceiveCustomer((List<?>) data);
					break;
				case S3Const.TASK_TRANSACTION_PURCHASE:
					// clear prodInCart list
					productInCart.clear();
					HashMap<String, Object> args = new HashMap<>();
					args.put("taskType", S3Const.TASK_SYNC_PRODUCT);
					args.put("data", data);
					server.broadcastMessage(args);
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
		case S3Const.TASK_SYNC_SUPPLIER:
			onSyncSupplier((List<?>) data);
			break;
		}
	}
	
	//private void onSyncCustomer(List<?> data) {
	//	Map<?, ?> userInfo = (Map<?, ?>) data.get(0);
	//    String userID = (String)userInfo.get(S3Const.TABLE_USER_ID);
	//    if (user != null && user.getID().compareTo(userID) == 0) {
	//    	((S3Customer)user).update(userInfo);
	//    }
	//}
	
	private void onSyncSupplier(List<?> data) {
		if (supplierList.size() == 0) {
			for(int i = 0; i < data.size(); i++){
				Map<?, ?> row = (Map<?, ?>)data.get(i);
				S3Supplier prod = new S3Supplier(row);
				supplierList.add(prod);
			}
		} else {
			for(int i = 0; i < data.size(); i++){
				Map<?, ?> row = (Map<?, ?>)data.get(i);
				String id = (String)row.get(S3Const.TABLE_SUPPLIER_ID);
				boolean found = false;
				for (int j = 0; j < supplierList.size(); j++) {
					S3Supplier p = supplierList.get(j);
					if (p.id.compareTo(id) == 0) {
						p.update(row);
						found = true;
						break;
					}
				}
				
				if(!found) {
					S3Supplier prod = new S3Supplier(row);
					supplierList.add(prod);
				}
			}
		}
	}
	
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
		productController.postGetAllSuppiler(S3Const.TASK_SYNC_SUPPLIER);
	}
	
	//------------------------------Talk to xxxxControllers for SQL tasks --------------------------------------------------
	// ????????????? OrderItemo
	public static void changeBulkSalePlan(int planNo, int planItem, double value){
		S3OrderItemController.bulkSalePlan[planNo-1][planItem-1] = value;
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
				S3Transaction transaction = new S3Transaction(id, cost, date, custID);
				
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
			return price*(1-discount/100);
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
		public double getTotalPrice(HashMap<S3Product, Integer> orderList, int custPoints){
			double cost = 0.0;
			double priceInProgress = 0.0;
			
			for(Map.Entry<S3Product, Integer> entry : orderList.entrySet()){
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
}
