drop table S3T_Supply;
drop table S3T_OrderItem;
drop table S3T_Transaction;
drop table S3T_Staff;
drop table S3T_Customer;
drop table S3T_Product;
drop table S3T_Supplier;


create table S3T_Customer (
  id char(11),
  point int default 0 not null,
  cash number default 0 not null,
  PRIMARY KEY (id)
);

create table S3T_Staff (
  id char(11),
  type int not null,
  PRIMARY KEY (id)
);

create table S3T_Supplier(
	id char(10),
	e_mail varchar(200),
  	PRIMARY KEY (id)
);

create table S3T_Product (
  barcode char(10),
  name varchar(20),
  price number not null,
  promotion int default 0 not null,
  discount int default 0 not null,
  stockLv int default 0 not null,
  replenishLv int default 0 not null,
  supplier_id char(10) not null,
  PRIMARY KEY (barcode),
  FOREIGN KEY (supplier_id) REFERENCES S3T_Supplier(id)
);

create table S3T_Transaction (
  id char(10),
  cost number default 0 not null,
  trans_date date not null,
  cust_id char(11) not null,
  PRIMARY KEY (id),
  FOREIGN KEY (cust_id) REFERENCES S3T_Customer(id)
);

create table S3T_OrderItem(
	trans_id char(10) not null,
  	prod_barcode char(10) not null,
	quantity number not null,
	PRIMARY KEY (prod_barcode, trans_id),
  	FOREIGN KEY (trans_id) REFERENCES S3T_Transaction(id),
	FOREIGN KEY (prod_barcode) REFERENCES S3T_Product(barcode)
);


create table S3T_Supply(
  	supplier_id char(10),
  	prod_barcode char(10),
	quantity number not null,
	supply_date date not null,
	PRIMARY KEY (prod_barcode, supplier_id),
  	FOREIGN KEY (supplier_id) REFERENCES S3T_Supplier(id),
	FOREIGN KEY (prod_barcode) REFERENCES S3T_Product(barcode)
);



INSERT INTO S3T_Customer values ('c0000000001', 21, 200);
INSERT INTO S3T_Staff values ('s0000000001', 0);
INSERT INTO S3T_Supplier values('s1', 's1@gmail.com.au');
INSERT INTO S3T_Product values('0000000000', 'Product_000000000000', 10, 1, 10, 100, 10, 's1');
INSERT INTO S3T_Product values('1234567890', 'Product_000000000001', 20, 2, 20, 200, 20, 's1');
