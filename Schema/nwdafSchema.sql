CREATE SCHEMA `NWDAF` ;
CREATE TABLE `NWDAF`.`nwdafSubscriptionTable` (
  `subscriptionID` VARCHAR(128) NOT NULL,
  `eventID` INT NOT NULL,
  `notificationURI` VARCHAR(128) NOT NULL,
  `notifMethod` TINYINT NULL,
  `repetitionPeriod` INT NULL,
  PRIMARY KEY (`subscriptionID`));

CREATE TABLE `NWDAF`.`nwdafSliceLoadLevelSubscriptionData` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `subscriptionID` VARCHAR(128) NULL,
  `snssais` VARCHAR(128) NULL,
  `loadLevelThreshold` INT NULL,
  PRIMARY KEY (`ID`),
  INDEX `subscriptionID_idx` (`subscriptionID` ASC) VISIBLE,
  CONSTRAINT `subscriptionID`
    FOREIGN KEY (`subscriptionID`)
    REFERENCES `nwdaf`.`nwdafsubscriptiontable` (`subscriptionID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
 CREATE TABLE `NWDAF`.`nwdafSliceLoadLevelInformation` (
  `snssais` VARCHAR(128) NOT NULL,
  `currentLoadLevel` INT NULL,
  PRIMARY KEY (`snssais`));
  
  CREATE TABLE `NWDAF`.`nwdafSliceLoadLevelSubscriptionTable` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `snssais` VARCHAR(128) NULL,
  `subscriptionID` VARCHAR(128) NOT NULL,
  `correlationID` VARCHAR(128) NOT NULL,
  `refCount` int null,
  `getAnalytics` varchar(5) not null,
  PRIMARY KEY (`ID`));
  
