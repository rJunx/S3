package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public abstract class S3Menu {
	protected Scanner scan = new Scanner(System.in);
	protected S3Application app;
	
	public S3Menu(S3Application application) {
		app = application;
	}
	
	protected double fetchDoubleByInput() {
		double value = -1;
		
		do {
			try {
				System.out.println("Please enter a number:");
				value = scan.nextDouble();
			} catch (Exception e) {
				value = -1;
			}
		} while (value == -1);
		return value;
	}
	
	abstract void run() throws RemoteException, SQLException;
	abstract void onReceiveData(int taskType, List<?> data);
}
