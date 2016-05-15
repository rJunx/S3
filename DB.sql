drop table S3T_Customer;
drop table S3T_Staff;
drop table S3T_Product;
drop table S3T_Transation;
drop table S3T_OrderItem;

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

create table S3T_Product (
  barcode varchar(10),
  name varchar(20),
  price number not null,
  promotion int default 0 not null,
  discount int default 0 not null,
  stockLv int default 0 not null,
  replenishLv int default 0 not null,
  supplier varchar(100),
  PRIMARY KEY (barcode)
);

create table S3T_Transation (
  id char(10),
  cost number default 0 not null,
  trans_date date not null,
  trans_time time not null,
  FOREIGN KEY (cust_id) REFERENCES S3T_Customer(id),
  PRIMARY KEY (id)
);

create table S3T_Supplier(
	id char(10),
	e_mail varchar(100)
)；

create table S3T_Supply(
	FOREIGN KEY (supplier_id) REFERENCES S3T_Supplier(id),
	FOREIGN KEY (prod_barcode) REFERENCES S3T_Product(barcode),
	quantity double not null,
	supply_date date not null,
	PRIMARY KEY (prod_barcode, supplier_id)
)；

create table S3T_OrderItem(
	FOREIGN KEY (trans_id) REFERENCES S3T_Transation(id),
	FOREIGN KEY (prod_barcode) REFERENCES S3T_Product(barcode),
	quantity double not null,
	PRIMARY KEY (prod_barcode, trans_id)
)；


INSERT INTO S3T_Customer values ('c0000000001', 0, 0);
INSERT INTO S3T_Staff values ('s0000000001', 0);
INSERT INTO S3T_Product values('0000000000', 'Product_0', 1.99, 0, 0, 0, 0, 'Supplier1 in Australia');
INSERT INTO S3T_Product values('1234567890', 'Product_1', 20, 1, 10, 100, 10, 'Supplier2 in Australia');