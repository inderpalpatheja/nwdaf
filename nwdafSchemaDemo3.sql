# help 

DROP DATABASE IF EXISTS NWDAF;
CREATE SCHEMA `NWDAF` ;
CREATE TABLE `NWDAF`.`nwdafLoadLevelInformation` (
   `snssais` VARCHAR (128) NOT NULL,
   `currentLoadLevel` INT,
   PRIMARY KEY (`snssais`));
    
  
  CREATE TABLE `NWDAF`.`nwdafSubscriptionTable` (
  `subscriptionID` VARCHAR(128) NOT NULL,
  `eventID` INT NOT NULL,
  `notificationURI` VARCHAR(128) NOT NULL,
  `snssais` VARCHAR (128) NOT NULL,
  `anySlice` TINYINT NOT NULL,
  `notifMethod` TINYINT NULL,
  `repetitionPeriod` INT NULL,
  `loadLevelThreshold` INT NULL,
  PRIMARY KEY (`subscriptionID`));
   

  CREATE TABLE 	`NWDAF`.`nwdafIDTable` (
  `snssais` varchar (128) NOT NULL,
  `correlationID` varchar(256) NOT NULL,
  `unSubCorrelationID` varchar(256) NOT NULL,
  PRIMARY KEY (`snssais`));


ALTER TABLE `NWDAF`.`nwdafSubscriptionTable`
ADD FOREIGN KEY (`snssais`) REFERENCES `NWDAF`.`nwdafLoadLevelInformation`(`snssais`);



