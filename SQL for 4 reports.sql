
--sales report( with differennt date range)
select barcode,name,sum(quantity*price)
from S3T_Transaction, S3T_Product, S3T_OrderItem
WHERE S3T_Transaction.ID=S3T_OrderItem.TRANS_ID AND S3T_Product.BARCODE=S3T_OrderItem.PROD_BARCODE  AND  TRANS_DATE between TO_DATE('4/1/2016', 'MM/DD/YYYY') and TO_DATE('8/4/2016', 'MM/DD/YYYY')
GROUP BY BARCODE,NAME;

--sales report on one date
select barcode,name,sum(quantity*price)
from S3T_Transaction, S3T_Product, S3T_OrderItem
WHERE S3T_Transaction.ID=S3T_OrderItem.TRANS_ID AND S3T_Product.BARCODE=S3T_OrderItem.PROD_BARCODE  AND  TRANS_DATE between TO_DATE('4/1/2016', 'MM/DD/YYYY') and TO_DATE('4/1/2016', 'MM/DD/YYYY')
GROUP BY BARCODE,NAME;


--purchase report
Same as sales report


--TOP 10 best seller by descending order based on sales report
select barcode,name,sum(quantity*price)
from S3T_Transaction, S3T_Product, S3T_OrderItem
WHERE S3T_Transaction.ID=S3T_OrderItem.TRANS_ID AND S3T_Product.BARCODE=S3T_OrderItem.PROD_BARCODE  AND  TRANS_DATE between TO_DATE('4/1/2016', 'MM/DD/YYYY') and TO_DATE('8/4/2016', 'MM/DD/YYYY')
GROUP BY BARCODE,NAME
ORDER BY sum(quantity*price) (DESC)
HAVING COUNT(*)<=10;




--supply report 
SELECT supplier_id,prod_barcode , e_mail
FROM S3T_Supplier,S3T_Supply
WHERE S3T_Supplier.ID= S3T_Supply.supplier_ID AND SUPPLY_DATE between TO_DATE('3/1/2016', 'MM/DD/YYYY') and TO_DATE('4/4/2016', 'MM/DD/YYYY');







