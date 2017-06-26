##Tests to Cover MySQL connector

**DataBase Information**

DB name : BAL-DB

Uses Following Tables

`CREATE TABLE personsinlanka (
    personid int NOT NULL,
    lastname varchar(255) NOT NULL,
    firstname varchar(255),
    address varchar(255),
    city varchar(255),
    PRIMARY KEY (personid)
);`

`CREATE TABLE personsinus (
    personid int NOT NULL,
    lastname varchar(255) NOT NULL,
    firstname varchar(255),
    address varchar(255),
    city varchar(255),
    PRIMARY KEY (personid)
);`


This Tests MySQL connector related.

Invocation payload


`{  
   "id":3,
   "firstname":"danuja",
   "lastname":"perera",
   "address":"260, Mahawatta Rd, Colombo 14",
   "city":"kentaky"
}`