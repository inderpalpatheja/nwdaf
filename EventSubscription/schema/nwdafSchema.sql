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

CREATE TABLE `NWDAF`.`nwdafUEmobilityInformation` (
  `supi` VARCHAR(128) NOT NULL,
  `ts` DATETIME NOT NULL,
  `DurationSec` INT NOT NULL,
  PRIMARY KEY (`supi`));
  
  CREATE TABLE `NWDAF`.`nwdafUserLocation` (
  `ID` INT NOT NULL AUTO_INCREMENT,
   `Tai` VARCHAR(128) NOT NULL, 
  `cellID` VARCHAR(128) NOT NULL, 
   `timeDuration` INT NULL, 
   PRIMARY KEY (`ID`)); 


CREATE TABLE `NWDAF`.`nwdafLocationID` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `supi` VARCHAR(128) NOT NULL,
  `locationID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`locationID`)
  REFERENCES `NWDAF`.`nwdafUserLocation` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION);







CREATE TABLE `NWDAF`.`nwdafQosSustainabilitySubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionID` VARCHAR(128) NOT NULL,
`5Qi` INT NOT NULL,
`plmnID` VARCHAR(128) NOT NULL,
`tac` VARCHAR(128) NOT NULL,
`snssais` VARCHAR(128) NULL,
`ranUeThroughputThreshold` INT NULL,
`qosFlowRetainThreshold` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionID`)
REFERENCES `NWDAF`.`nwdafSubscriptionTable` (`subscriptionID`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);


CREATE TABLE `NWDAF`.`nwdafQosSustainabilityInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`plmnID` VARCHAR(128) NOT NULL,
`snssais` VARCHAR(128) NULL,
`ranUeThroughput` INT NULL,
`qosFlowRetain` INT NULL,
PRIMARY KEY(`ID`));


CREATE TABLE `NWDAF`.`nwdafQosSustainabilitySubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`plmnID` VARCHAR(128) NOT NULL,
`snssais` VARCHAR(128) NULL,
`subscriptionID` VARCHAR(128) NOT NULL,
`correlationID` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));






CREATE TABLE `NWDAF`.`nwdafServiceExperienceSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionID` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`snssais` VARCHAR(128) NOT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionID`)
REFERENCES `NWDAF`.`nwdafSubscriptionTable` (`subscriptionID`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);
  
  
  
  
CREATE TABLE `NWDAF`.`nwdafServiceExperienceInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`snssais` VARCHAR(128) NOT NULL,
`mos` FLOAT NOT NULL,
`upperRange` FLOAT NOT NULL,
`lowerRange` FLOAT NOT NULL,
PRIMARY KEY (`ID`));



CREATE TABLE `NWDAF`.`nwdafServiceExperienceSubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`snssais` VARCHAR(128) NOT NULL,
`subscriptionID` VARCHAR(128) NOT NULL,
`correlationID` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));





CREATE TABLE `NWDAF`.`nwdafNetworkPerformanceSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionID` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`nwPerfType` INT NOT NULL,
`relativeRatioThreshold` INT NULL,
`absoluteNumThreshold` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionID`)
REFERENCES `NWDAF`.`nwdafSubscriptionTable` (`subscriptionID`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);



CREATE TABLE `NWDAF`.`nwdafNetworkPerformanceInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`nwPerfType` INT NOT NULL,
`relativeRatio` INT NOT NULL,
`absoluteNum` INT NOT NULL,
PRIMARY KEY (`ID`));


CREATE TABLE `NWDAF`.`nwdafNetworkPerformanceSubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`nwPerfType` INT NOT NULL,
`subscriptionID` VARCHAR(128) NOT NULL,
`correlationID` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));







CREATE TABLE `NWDAF`.`nwdafUserDataCongestionSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionID` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`congType` INT NOT NULL,
`congLevelThreshold` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionID`)
REFERENCES `NWDAF`.`nwdafSubscriptionTable` (`subscriptionID`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);


CREATE TABLE `NWDAF`.`nwdafUserDataCongestionInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`congType` INT NOT NULL,
`tai` VARCHAR(128) NOT NULL,
`congLevel` INT NOT NULL,
PRIMARY KEY (`ID`));


CREATE TABLE `NWDAF`.`nwdafUserDataCongestionSubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`congType` INT NOT NULL,
`tai` VARCHAR(128) NOT NULL,
`subscriptionID` VARCHAR(128) NOT NULL,
`correlationID` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));


  
  
