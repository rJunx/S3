package company.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class S3TransactionCalculation {
	// local variables
	S3Customer customer;
	List<S3OrderItem> itemList = new ArrayList<S3OrderItem>();
	
	// Construtor
	public S3TransactionCalculation(S3Customer customer, List<S3OrderItem> itemList){
		this.customer = customer;
		this.itemList = itemList;	
	}
	
	
	public int getTotalPointsConsumed(S3Customer customer){
		int points = customer.getPoint();
		int totalPoints = (int)(points/20*5);
		return totalPoints;
	}
		

	public double getTotalCost(List<S3Product> prodList){
		double cost = 0.0;
		double priceInProgress = 0.0;
		S3Product prod;
		double qty;
				
		for(int i = 0; i < itemList.size(); i++){
			
			prod = getProductByID(itemList.get(i).barCode, prodList);
			qty = itemList.get(i).qty;
			priceInProgress = priceAfterDiscount(prod.price,prod.discount);
			priceInProgress = priceAfterPromotion(prod.promotion, qty, priceInProgress);
			priceInProgress = priceInProgress * qty;
			cost += priceInProgress;
		}
		cost = priceAfterCustPoints(customer.getPoint(), cost);
		return cost;
	}
	
	public int getTotalPointsEarned(double totalCost){
		return (int)(totalCost/10);
	}
	
	
	public double checkPromotionRate(int planID, double qty){
		if(qty > 30){
			return S3PromotionPlan.bulkSalePlan[planID][S3PromotionPlan.thirtyItems];
		}else if(qty > 20){
			return S3PromotionPlan.bulkSalePlan[planID][S3PromotionPlan.twentyItems];
		}else if(qty > 10){
			return S3PromotionPlan.bulkSalePlan[planID][S3PromotionPlan.tenItems];
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
	
	
	public S3Product getProductByID(String id, List<S3Product> prodList){
		S3Product product = null;
		for(int i = 0; i < prodList.size(); i++){
			if(prodList.get(i).barcode.equals(id)){
				product = prodList.get(i);
				break;
			}
		}
		return product;
	}
	
}
