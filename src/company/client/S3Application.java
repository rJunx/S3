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

public class S3Application {


	private Scanner scan = new Scanner(System.in);

	private S3ProductController productController;
	private S3CustomerController customerController;
	private S3StaffController staffController;
	private S3OrderItemController orderController;
	private S3TransactionController transactionController;

	
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
	
	private void postShowProduct() throws RemoteException, SQLException {
		productController.postGetAllProduct(S3Const.TASK_SHOW_ALL_PRODUCTS);
	}
	
	private void onShowProduct( List<?> data ) {
		System.out.println(data);
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
	
	private S3UserType checkUserType(String userID) {
		if (userID.charAt(0) == 'c') {
			return S3UserType.CUSTOMER;
		} else if (userID.charAt(0) == 's') {
			return S3UserType.STAFF;
		} else {
			return S3UserType.UNKNOWN;
		}
	}
	
	public void onRevData( int taskType, Object data ) {
		switch (taskType) {
		case S3Const.TASK_SHOW_ALL_PRODUCTS:
			onShowProduct((List<?>)data);
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
	
	public void PostCheckDiscount(String prodID) throws RemoteException, SQLException{
		productController.postGetProductInfoByID(prodID, S3Const.TASK_SHOW_PROD_BY_ID);
	}
	
	///  KB has questions... 
	public Object onShowProductByID(List data){
		if (data == null || data.size() == 0) {
			System.out.println("Product does not exit"); 
		} else {
			//  what should I do ....? How can I get the price, discount info out?
		}
		return data.get(0);
	}
	
	public double calculatePrice(double originalPrice, double discount){
		return originalPrice * (1 - discount);
	}
	
	public void run() throws RemoteException, SQLException {
		char ch;
		do {
			ch = menu();
			switch (ch) {
			case '8':
				postLogin();
				break;
			case '9':
				postShowProduct();
				break;
			case 'c':
				inPutProdItems();
				break;
			}
		} while (ch != 'e');
	}
	

	

	public void postShowCustomerByID(String custID) throws RemoteException, SQLException{
		customerController.onGetCustomerInfoByID(custID, S3Const.TASK_SHOW_CUSTOMER_BY_ID);
	} 

	public void onShowCustomerByID(List data){
		if (data == null || data.size() == 0) {
			System.out.println("Customer does not exit"); 
		} else {
			// same question as the one at "onShowProductByID";
			// it looks like that I have to create individual class for Product, Customer and Staff...
		}
	}
	
	
// -------------------------------------------------- Added by KB -------------------------------------
	//  ------ to show all the product IDs
	
	
	
	
	
	
	
	
	
	//-------To input the product items
	// Note that "InputMismatchException" could be avoided in GUI at later stage
	public HashMap<String, Double> inPutProdItems(){
		Scanner scan = new Scanner(System.in);
		String prodId;
		double quantity;
		HashMap<String, Double> prodInCart = new HashMap<String, Double>();
		do{
			System.out.println("Please input the code bar, or type * to finish.");
			prodId = scan.next();
			System.out.println();
			System.out.println("Please input the quantity (copies/weight)");
			quantity = scan.nextDouble();
			
			// check if the product has been input or not
			if(prodInCart.containsKey(prodId)){
				double oldQty = prodInCart.get(prodId);
				double newQty = oldQty + quantity;
				prodInCart.put(prodId, newQty);
			}else
				prodInCart.put(prodId, quantity);
		}while(prodId != "*");
		return prodInCart;
	}
	
	// ------Remove items from the selected list
	
	
	
	
	
	// ------To get the product price info of the selected products
	
	
	
	
	// ----- Transaction-related calculation--------
	
	public double priceAfterDiscount(double price, double discount){
		return price*(1-discount);
	}
	
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
	
	public double priceAfterCustPoints(int points, double price){
		if((price - (int)(points/20)* 5) >= 0)
			return (price - (int)(points/20)* 5);
		else 
			return 0.0;
	}

	public double priceAfterPromotion(int promotion, double qty, double price){
		return (1 - checkPromotionRate(promotion, qty)) * price; 
	}
	
	// ------ 

}
