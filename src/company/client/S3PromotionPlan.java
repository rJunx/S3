package company.client;

public class S3PromotionPlan {
	public static double[][] bulkSalePlan ={{0.00,0.00,0.00},
											{0.10,0.20,0.30}, 
											{0.20,0.40,0.60}};
	public static int tenItems = 0;
	public static int twentyItems = 1;
	public static int thirtyItems = 2;
	
	public double getPromotionRate(int planID, int qty){
		if(planID < 0 || planID > 2)
			return 0.0;
		if(qty > 30){
			return bulkSalePlan[planID][2];
		}else if(qty > 20){
			return bulkSalePlan[planID][1];
		}else if(qty > 10){
			return bulkSalePlan[planID][0];
		}else 
			return 0.0;
	}
}
