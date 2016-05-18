package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import company.S3Const;
import company.S3UserType;

public class S3SaleStaffMenu extends S3Menu {

	public S3SaleStaffMenu(S3Application application) {
		super(application);
		// TODO Auto-generated constructor stub
	}

	@Override
	void run() throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		char option;//option number from menu, selected by user
		
		do{
			System.out.println();
			System.out.println("								User:" + app.getCurrentUser().getID());
			System.out.println("\n\n\n\t\tSale Staff Menu\n");
			System.out.println("\tCreate New Members					1");
			System.out.println("\tUpdate User Balance					2");
			System.out.println("\tDelete Products in Cart				3");
			System.out.println("\tCancel Current Transaction			4");
			System.out.println("\tLogout								e");
			
			System.out.print("Please enter your option: ");
			option = scan.nextLine().charAt(0);
			

			switch (option){
			case '1':
				onCreateNewMember();
				break;
			case '2':
				onUpdateBalance();
				break;
			case '3':
				onDeleteProdInCart();
				break;
			case '4':
				onCancelTransaction();
				break;
			case 'e':
				app.logout();
				break;
			default:
				System.out.println("Please enter a valid option.");
			} 
		}while (option!= 'e');
	}
	
	// ??? How to name the customer ID???????? No need to add "C" at the beginning?
	public void onCreateNewMember() throws RemoteException, SQLException{
			boolean flag = true;
			String id = "";
			do {
				try {
					System.out.println("Please enter member ID: ");
					id = scan.next();
					flag = false;
					
					if (S3User.checkUserType(id) != S3UserType.CUSTOMER) {  //  should be "=="????
						throw new Exception();
					}
				} catch(Exception e) {
					flag = true;
					System.out.println("Invalid staff ID.");
				}
			} while (flag);
		
			int balance = this.fetchIntFromInput("Please enter balance: ", "Balance has to be integer! Please re-input!");
			app.getCustomerController().create(id, 0, balance);
	}
	
	public void onDeleteProdInCart(){
		System.out.println("The product items in cart: ");
		app.printProdInCart();
		
		System.out.println("Please enter the ID of the product to be deleted: ");
		String id = scan.nextLine();
		
		Boolean existed = false;
			
		for (S3Product product : app.getProdInCart().keySet()) {
			if(product.barcode.equals(id)){
				 existed = true;
				 app.getProdInCart().remove(product);
				 break;
			 }
		}
		if(existed == false){
			System.out.println("Invalid product ID!");
		}
	}
	
	public void onCancelTransaction(){
		System.out.println("Are you sure to cancel the current transaction? (Y/N)");
		char ch = scan.nextLine().charAt(0);
		if(ch == 'y' || ch == 'Y'){
			app.getProdInCart().clear();
		}
	}
	
	@Override
	public void onReceiveData(int taskType, List<?> data) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		switch(taskType) {
		case S3Const.TASK_TOP_UP_CUSTOMER:
			if (data == null || data.size() == 0) {
				System.out.println("Customer not exit.");
			} else {
				S3Customer c = new S3Customer((Map<?, ?>) data.get(0));
				double value = this.fetchDoubleFromInput("Please input top up value", "Invalid number.");
				app.getCustomerController().updateBalance(c.getID(), c.getBalance()+value);
			}
			break;
			default:
				break;
		}
	}
	
	public void onUpdateBalance() throws RemoteException, SQLException {
		boolean flag = true;
		String id = "";
		do {
			try {
				System.out.println("Please enter customer ID: ");
				id = scan.next();
				flag = false;
				
				if (S3User.checkUserType(id) != S3UserType.CUSTOMER) {
					throw new Exception();
				}
			} catch(Exception e) {
				flag = true;
				System.out.println("Invalid customer ID.");
			}
		} while (flag);
		
		app.getCustomerController().onGetCustomerInfoByID(id, S3Const.TASK_TOP_UP_CUSTOMER);
	}
}
