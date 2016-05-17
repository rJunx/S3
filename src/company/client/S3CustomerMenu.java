package company.client;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by TonyZheng on 11/05/2016.
 */


import java.util.*;

import company.S3Const;


public class S3CustomerMenu extends S3Menu {
    public S3CustomerMenu(S3Application application) {
		super(application);
		// TODO Auto-generated constructor stub
	}
    
    public void run() throws RemoteException, SQLException {
    	
    }

	public static String productCode;
    public static String productName;
    public char searchMenuCh;
    public char mainMenuCh;
    public char purchaseMenuCh;
    public char transactionMenuCh;
    Scanner scanner = new Scanner(System.in);
    public static String productCodeToController;
    public HashMap<String,String> productList = new HashMap<String,String>();
    public ArrayList<String> productCodeList = new ArrayList<String>();
    boolean isIn;
    public HashMap<String,Integer> purchaseList = new HashMap<>();
    //String productList[] = new String[productlist.lenth];
    public String proName;
    public String proCode;
    public int proPrice;
    public String proPromotion;
    
//	---------------------------------KB added ----------------------------------------------
    S3Application application;
    
    public void onRevData(int taskType, Object data){
		switch (taskType) {
		case S3Const.TASK_SHOW_PROD_BY_ID:
			onShowProductByID((ArrayList<S3Product>)data);
			break;
		case S3Const.TASK_SHOW_ALL_PRODUCTS:
			onShowAllProducts((ArrayList<S3Product>)data);
		case S3Const.TASK_SHOW_CUSTOMER_BY_ID:
			onShowCustomerByID((S3Customer)data);
			break;
		default:
			break;
		}
	}
    
    private void onShowProductByID(ArrayList<S3Product> data) {
		//Print out all the product's price
		for(int i = 0; i < data.size(); i++){
			System.out.print(data.get(i).barcode + "   " + data.get(i));
		}
	}

	private void onShowAllProducts(ArrayList<S3Product> data) {
		//Print out the only product, so data.size() = 1 actually
		for(int i = 0; i < data.size(); i++){
			System.out.print(data.get(i).name);
		}
	}
	
	private void onShowCustomerByID(S3Customer data){
		// Do something with the customer found in database
	}
	
	private void test() throws RemoteException, SQLException{
		// do something.... you need all the products
		application.postShowAllProducts();
	}
	
// ----------------------- End of "KB added" -----------------------------------------------

    public char searchMenu(){
        System.out.println("Search Products");
        System.out.println("");
        System.out.println("Please Select Searching Method");
        System.out.println("");
        System.out.println("*******************************************");
        System.out.println("");
        System.out.println("Press '1' to Show ALL Product List.");
        System.out.println("");
        System.out.println("Press '2' to Scan Product Barcode");
        System.out.println("");
        System.out.println("Press 'e' to Return Main Menu");
        System.out.println("");
        System.out.println("Your choose is(1/2/e):");
        searchMenuCh = scanner.nextLine().charAt(0);
    return searchMenuCh;
    }

    public char mainMenu(){
        System.out.println("Welcome to Pro Market Tech");
        System.out.println("");
        System.out.println("Press '1' to Purchase");
        System.out.println("");
        System.out.println("Press '2' to Search Product");
        System.out.println("");
        System.out.println("Press 'e' to EXIT");
        System.out.println("");
        System.out.println("Your Choose is(1/2/e):");
        mainMenuCh = scanner.nextLine().charAt(0);
        return mainMenuCh;
    }

    public char purchaseMenu(){
        System.out.println("SELECT Purchase Type");
        System.out.println("Press '1' to Scan Barcode");
        System.out.println("Press '2' to Show All Products");
        System.out.println("press 'e' to EXIT");
        purchaseMenuCh = scanner.nextLine().charAt(0);
        return purchaseMenuCh;
    }

    public char transactionMenu(){
        System.out.println();
        System.out.println("Please check your Items");
        System.out.println("Press Y to continue");
        System.out.println("Press N to CHANGE Items");
        transactionMenuCh = scanner.nextLine().charAt(0);
        return transactionMenuCh;
    }

    public void purchaseByCode(){
        purchaseMethod();
    }

    public void printPurchaseList() {
        System.out.println("Product Name : Quantity");
        Set<String> set1=purchaseList.keySet();
        Iterator<String> it1=set1.iterator();
        while(it1.hasNext()){
            String key = it1.next();
            Integer quantity = purchaseList.get(key);
            String name = productList.get(key);
            System.out.println(name + "    :   "+quantity);
        }
    }

    public void purchaseByList(){
        //out print productList
        /* System.out.println("Product Code : Product Name")
         * Set<String> listSet=productList.keySet();
         * Iterator<String> listIt=listSet.iterator();
         * while(listIt.hasNext()){
         *      String key = listIt.next();
         *      String value = productList.get(key);
         *      System.out.println(key + "        :      " +value);
         * }
         */
        purchaseMethod();
    }

    public void purchaseMethod(){
        do {
            this.productCodeToController = productCodeToController();
            System.out.println("Please enter Quantity");
            int quantity = scanner.nextInt();
            System.out.println("Press 'e' to Exit entering");
            purchaseList(productCodeToController,quantity);
        }while (productCodeToController != String.valueOf("e"));


        if (productCodeToController == String.valueOf("e")){
            System.out.println("Please check your purchase List");
            printPurchaseList();
            System.out.println("Press Y to conform your List");
            System.out.println("press N to change your List");
        }
    }

    public void switchMainMenu() throws Exception{
        this.mainMenuCh = mainMenu();
        do {
            switch (mainMenuCh){
                case '1':
                    purchaseMenu();
                    break;
                case '2':
                    searchMenu();
                    break;
            }
        }while (mainMenuCh !='e');
        if(mainMenuCh == 'e'){

        }
    }

    public void switchSearchMenu() throws Exception{
        this.searchMenuCh = searchMenu();
        do{
            switch (searchMenuCh){
                case '1':
                    searchByList();
                    break;
                case '2':
                    searchByCode();
                    break;
            }
        }while (searchMenuCh !='e');
        if (searchMenuCh =='e'){
            mainMenu();
        }
    }

    public void switchPurchaseMenu() throws Exception{
        this.purchaseMenuCh = purchaseMenu();
        do{
            switch(purchaseMenuCh){
                case '1':
                    purchaseByCode();
                    break;
                case '2':
                    purchaseByList();
                    break;
            }
        }while (purchaseMenuCh !='e');
        if (purchaseMenuCh =='e'){
            mainMenu();
        }
    }

    public String barCodeScanner(){
        System.out.println("Please Enter Product Code");
        String productCode = scanner.nextLine();
        return productCode;
    }

    public void searchByList(){
        //out print productList
        /* System.out.println("Product Code : Product Name")
         * Set<String> listSet=productList.keySet();
         * Iterator<String> listIt=listSet.iterator();
         * while(listIt.hasNext()){
         *      String key = listIt.next();
         *      String value = productList.get(key);
         *      System.out.println(key + "        :      " +value);
         * }
         */
        System.out.println("Please enter the Product Code of Item");
        this.productCodeToController =productCodeToController();
        // Send this code to DB method
        // searchProduct(productCodeToController);
        System.out.println("Product Details");
        System.out.println("product name :"+ proName + "  Product Price :" + proPrice);
        System.out.println("promotion is :" + proPromotion);
    }

    public void searchResult(String productCode,String productName,int price,String promotion){
        this.proName = productName;
        this.proCode = productCode;
        this.proPrice = price;
        this.proPromotion = promotion;
    }

    public void searchByCode(){
        this.productCodeToController = productCodeToController();
        // Send this Code to DB method
        // searchProduct(productCodeToController);
        System.out.println("Product Details");
        System.out.println("product name :"+ proName + "  Product Price :" + proPrice);
        System.out.println("promotion is :" + proPromotion);
    }

    public ArrayList switchProductCodeList(){ //switch HashMap into ArrayList
        Set<String> set = productList.keySet();
        Iterator<String> it = set.iterator();
        while(it.hasNext()){
            String key = it.next();
            productCodeList.add(key);
        }
        return productCodeList;
    }

    public boolean checkProductCode(){ //check if product code is valid
        this.productCodeList = switchProductCodeList();
        this.productCode = barCodeScanner();
        // this.productList = productList;
        boolean isIn = productCodeList.contains(productCode);
        return isIn;
    }

    public String productCodeToController(){ //return if product code is not valid
        do {
            this.isIn = checkProductCode();
            this.productCode = barCodeScanner();
            if (isIn = true) {
                productCodeToController = productCode;
            } else {
                System.out.println("Wrong Product Code, Check and Enter Again");
                productCodeToController();
            }
        }while (productCodeToController != String.valueOf('e'));
        return productCodeToController;
    }

    public void purchaseList(String productCodeToController,int quantity){
        if(purchaseList.containsKey(productCodeToController)){
            purchaseList.put(productCodeToController,purchaseList.get(productCodeToController)+quantity);
        }else {
            purchaseList.put(productCodeToController, quantity);
        }
    }

    public HashMap getPurchaseList(){
        return purchaseList;
    }

	@Override
	void onReceiveData(int taskType, List<?> data) {
		// TODO Auto-generated method stub
		
	}
}
