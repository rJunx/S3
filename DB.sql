drop table S3T_Customer;
drop table S3T_Staff;
drop table S3T_Product;
drop table S3T_ProductStore;

create table S3T_Customer (
  id char(11),
  point int default 0 not null,
  cash int default 0 not null,
  PRIMARY KEY (id)
);

create table S3T_Staff (
  id char(11),
  type short not null,
  PRIMARY KEY (id)
);

create table S3T_Product (
  barcode varchar(10),
  name varchar(20),
  price number not null,
  PRIMARY KEY (barcode)
);

create table S3T_ProductStore (
    barcode varchar(10),
    stockLv int not null,
    replenishLv int not null,
    
    PRIMARY KEY (barcode),
    FOREIGN KEY (barcode) REFERENCES S3T_PRODUCT(barcode)
);

INSERT INTO S3T_Customer values ('c0000000001', 0, 0);
INSERT INTO S3T_Staff values ('s0000000001', 0);
INSERT INTO S3T_Product values('0000000000', 'Product_0', 1.99);
INSERT INTO S3T_ProductStore values('0000000000', 10, 2);