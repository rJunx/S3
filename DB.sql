drop table S3T_Customer;
drop table S3T_Staff;
drop table S3T_Product;

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
    
  PRIMARY KEY (barcode)
);

INSERT INTO S3T_Customer values ('c0000000001', 0, 0);
INSERT INTO S3T_Staff values ('s0000000001', 0);
INSERT INTO S3T_Product values('0000000000', 'Product_0', 1.99, 0, 0, 0, 0);
