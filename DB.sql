drop table S3T_Customer;
drop table S3T_Staff;
drop table S3T_Product;
drop table S3T_ProductStore;
drop table S3T_ProductDiscount;

create table S3T_Customer (
  id char(11),
  point int default 0 not null,
  cash number default 0 not null,
  PRIMARY KEY (id)
);

create table S3T_Staff (
  id char(11),
  type short not null,
  PRIMARY KEY (id)
);

create table S3T_Promotion (
  type int,
  
  count int default 0 not null,
  discount int default 0 not null,
  
  PRIMARY KEY (type)
);

create table S3T_Product (
  barcode varchar(10),
  name varchar(20),
  price number not null,
  
  promotion int default 0 not null,
  discount int default 0 not null,
  stockLv int default 0 not null,
  replenishLv int default 0 not null,
    
  PRIMARY KEY (barcode),
  FOREIGN KEY (promotion) REFERENCES S3T_Promotion(type)
);

INSERT INTO S3T_Customer values ('c0000000001', 0, 0);
INSERT INTO S3T_Staff values ('s0000000001', 0);
INSERT INTO S3T_Product values('0000000000', 'Product_0', 1.99);
INSERT INTO S3T_ProductStore values('0000000000', 10, 2);

INSERT INTO S3T_Promotion values(0);
INSERT INTO S3T_ProductDiscount values('0000000000');
