# help 
DROP DATABASE IF EXISTS NWDAF;
CREATE SCHEMA `NWDAF` ;
CREATE TABLE `NWDAF`.`nwdafLoadLevelInformation` (
  `loadLevelID` INT NOT NULL AUTO_INCREMENT,
  `snssais` VARCHAR(128) NULL,
  `anySlice` TINYINT NULL,
  `currentLoadLevelInfo` INT NULL,
   `subscriptionID` VARCHAR(128) NULL,
  `correlationID` varchar(256) NOT NULL,
   PRIMARY KEY (`loadLevelID`));
    
  
  CREATE TABLE `NWDAF`.`nwdafSubscriptionTable` (
  `subscriptionID` VARCHAR(128) NOT NULL,
  `eventID` INT NOT NULL,
  `notificationURI` VARCHAR(128) NOT NULL,
  `notifMethod` TINYINT NULL,
  `repetitionPeriod` INT NULL,
  `loadLevelThreshold` INT NULL,
  PRIMARY KEY (`subscriptionID`));
   

  CREATE TABLE 	`NWDAF`.`nwdafIDTable` (
  `correlationID` varchar(256) NOT NULL,
  `unSubCorrelationID` varchar(256) NOT NULL,
  PRIMARY KEY (`correlationID`));


ALTER TABLE `NWDAF`.`nwdafLoadLevelInformation`
ADD FOREIGN KEY (`correlationID`) REFERENCES `NWDAF`.`nwdafIDTable`(`correlationID`);

