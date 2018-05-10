--! Creating CUSTOMER table for customers of our railway
CREATE TABLE CUSTOMER(
ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY CLUSTERED,
   CUST_ID AS 'CU' + RIGHT('00000000' + CAST(ID AS VARCHAR(8)), 8) PERSISTED,
NAME VARCHAR(200),
EMAIL VARCHAR(200),
USERNAME VARCHAR(50),
PASSWORD VARCHAR(50)
);

--! Creating ADMIN table for administrators who run our railway
CREATE TABLE ADMIN(
ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY CLUSTERED,
   ADMIN_ID AS 'AD' + RIGHT('00000000' + CAST(ID AS VARCHAR(8)), 8) PERSISTED,
NAME VARCHAR(200),
EMAIL VARCHAR(200),
USERNAME VARCHAR(50),
PASSWORD VARCHAR(50)
);

--! There ALWAYS has to be at least one admin so that more admins can be created, so this is the "default" admin
INSERT INTO ADMIN (NAME, EMAIL, USERNAME, PASSWORD) VALUES(
'admin',
'nonexistent',
'admin',
'password'
);

--! Creating TRAIN table that holds information about the trains on our railway
CREATE TABLE TRAIN(
ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY CLUSTERED,
   TRAIN_ID AS 'TN' + RIGHT('00000000' + CAST(ID AS VARCHAR(8)), 8) PERSISTED,
NAME VARCHAR(200),
MODEL VARCHAR(200),
NUM_OF_SEATS INT
);

--! Creating TRAIN_STATION table that holds information about the train stations along our railway
CREATE TABLE TRAIN_STATION(
ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY CLUSTERED,
   TRAIN_STATION_ID AS 'TS' + RIGHT('00000000' + CAST(ID AS VARCHAR(8)), 8) PERSISTED,
NAME VARCHAR(200),
LOCATION VARCHAR(200)
);

--! Creating TRACK table that holds information about the individual tracks that run between two stations
CREATE TABLE TRACK(
ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY CLUSTERED,
   TRACK_ID AS 'TK' + RIGHT('00000000' + CAST(ID AS VARCHAR(8)), 8) PERSISTED,
STATION_FROM_ID CHAR(10),
STATION_TO_ID CHAR(10),
LENGTH INT
);

--! Creating SCHEDULE table that holds information about the daily schedules (assume every schedule runs every day).
CREATE TABLE SCHEDULE(
ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY CLUSTERED,
   SCHEDULE_ID AS 'SC' + RIGHT('00000000' + CAST(ID AS VARCHAR(8)), 8) PERSISTED,
TRAIN_ID CHAR(10),
TRACK_ID CHAR(10),
DEPARTURE_TIME TIME,
ARRIVAL_TIME TIME
);

--! Creating TICKET table that holds information about the tickets that have been created by the admins
CREATE TABLE TICKET(
ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY CLUSTERED,
   TICKET_ID AS 'TT' + RIGHT('00000000' + CAST(ID AS VARCHAR(8)), 8) PERSISTED,
SCHEDULE_ID CHAR(10),
EVENT_DATE DATE,
SEAT VARCHAR(3),
PRICE DECIMAL(6,2),
OWNER_ID CHAR(10)
);