CREATE TABLE if NOT EXISTS company(
id              int not null  auto_increment primary key,
company_name    varchar(255) not null

);


CREATE TABLE if NOT EXISTS employee(
id     int not null  auto_increment primary key ,
name   varchar(255) not null,
age    int,
salary double,
company_id int,
FOREIGN KEY (company_id) REFERENCES Company(id)
);