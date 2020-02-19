drop database if exists `accounts-db`;  
create database `accounts-db`;

drop user if exists `accounts-user`@`%`;
create user `accounts-user`@`%` identified by 'password';
grant all privileges on `accounts-db`.* to `accounts-user`@`%`;

use `accounts-db`;

create table Invoices (
    InvoiceID bigint not null auto_increment,
    InvoiceDate date,
    OrderNo bigint,
    CustomerId bigint,
    CustomerNo varchar(11),
    CustomerName varchar(50),
    Freight decimal(10,4),
    IsPaid bit,
    Version bigint,
    ObjectID varbinary(36),
    constraint PK_Invoices primary key (InvoiceID)
);

create table InvoiceDetails (
    InvoiceDetailsID bigint not null auto_increment,
    InvoiceID bigint not null,
    ProductID bigint not null,
    ProductName varchar(50),
    Quantity int,
    Amount decimal(10,4),
    Version bigint,
    ObjectID varbinary(36),
    constraint PK_InvoiceDetails primary key (InvoiceDetailsID),
    constraint FK_InvoiceDetails_Invoice foreign key (InvoiceId) references Invoices(InvoiceId)
);

insert into Invoices (InvoiceDate, OrderNo, CustomerId, CustomerNo, CustomerName, Freight, IsPaid, Version, ObjectID)
select OrderDate, OrderID, CustomerId, CustomerNo, CustomerName, Freight, 
    case when ShippedDate is null then 0 else 1 end, 1, uuid()
from `orders-db`.Orders;

insert into InvoiceDetails (InvoiceID, ProductID, ProductName, Quantity, Amount, Version, ObjectID)
select  i.InvoiceID, ProductID, ProductName, Quantity, UnitPrice, 1, uuid()
from `orders-db`.OrderDetails oi
    join Invoices i on oi.OrderID = i.OrderNo;

