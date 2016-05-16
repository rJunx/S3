package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface S3CustomerMenuIF {
	
	// --------------------- For Tony to use ------------------------------------------------
	
	// return total cost of the given products for the given customer
	public double calcTotalCost(ArrayList<Product> prodInCart, Customer customer);
	
	// return Arraylist<Product> with all the products' info
	public void postShowAllProducts() throws RemoteException, SQLException;
	
	// return one product's info by the given id
	public void postShowProductByID(String productID) throws RemoteException, SQLException;
	
	// return one customer's info by the given id
	public void postShowCustomerByID(String custID) throws RemoteException, SQLException;
	
	// return one staff's info by the given id
	public void postShowSaleStaffByID(String staffID) throws RemoteException, SQLException;
	
	
	// update customer's information
	public void postUpdateCustomerByID(Customer customer) throws RemoteException, SQLException;
	
	// update product's stockLevel
	public void postUpdateProductStockLevel(ArrayList<Product> products) throws RemoteException, SQLException;
	
}
