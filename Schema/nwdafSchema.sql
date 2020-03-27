DROP SCHEMA IF EXISTS `NWDAF`;
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
    FOREIGN KEY (`subscriptionID`)
    REFERENCES `NWDAF`.`nwdafSubscriptionTable` (`subscriptionID`)
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
  `refCount` int NOT NULL,
  PRIMARY KEY (`ID`));
  
  
    
   CREATE TABLE `NWDAF`.`nwdafUEmobilitySubscriptionData` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `subscriptionID` VARCHAR(128) NOT NULL,
  `supi` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ID`));
  
  
  CREATE TABLE `NWDAF`.`nwdafUEmobilitySubscriptionTable` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `supi` VARCHAR(128) NULL,
  `subscriptionID` VARCHAR(128) NOT NULL,
  `correlationID` VARCHAR(128) NOT NULL,
  `refCount` INT NOT NULL,
   PRIMARY KEY (`ID`));

CREATE TABLE `NWDAF`.`nwdafUEmobility` (
  `supi` VARCHAR(128) NOT NULL,
  `ts` DATETIME NOT NULL,
  `DurationSec` INT NOT NULL,
  `location` VARCHAR(128) NULL,
  PRIMARY KEY (`supi`));
  
  CREATE TABLE `NWDAF`.`nwdafUserLocation` (
  `ID` INT NOT NULL AUTO_INCREMENT,
   `Tai` VARCHAR(128) NOT NULL, 
  `cellID` VARCHAR(128) NOT NULL, 
   `timeDuration` INT NULL, 
   PRIMARY KEY (`ID`)); 






CREATE TABLE `NWDAF`.`nwdafQosSustainability` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionID` VARCHAR(128) NOT NULL,
`5Qi` INT NOT NULL,
`plmnID` VARCHAR(128) NOT NULL,
`tac` VARCHAR(128) NOT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionID`)
REFERENCES `NWDAF`.`nwdafSubscriptionTable` (`subscriptionID`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);


CREATE TABLE `NWDAF`.`nwdafQosSustainabilitySubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionID` VARCHAR(128) NOT NULL,
`snssais` VARCHAR(128) NULL,
`ranUeThroughputThreshold` INT NULL,
`qosFlowRetainThreshold` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionID`)
REFERENCES `NWDAF`.`nwdafSubscriptionTable` (`subscriptionID`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);


CREATE TABLE `NWDAF`.`nwdafQosSustainabilityInformation` (
`snssais` VARCHAR(128) NOT NULL,
`ranUeThroughput` INT NULL,
`qosFlowRetain` INT NULL,
PRIMARY KEY(`snssais`));


CREATE TABLE `NWDAF`.`nwdafQosSustainabilitySubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`snssais` VARCHAR(128) NULL,
`subscriptionID` VARCHAR(128) NOT NULL,
`correlationID` VARCHAR(128) NOT NULL,
`refCount` int NOT NULL,
PRIMARY KEY (`ID`));

  
  
  
  
  
  
  
  
  
