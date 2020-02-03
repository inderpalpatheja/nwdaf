DROP DATABASE IF EXISTS NWDAF;
CREATE SCHEMA `NWDAF` ;
USE `NWDAF`

CREATE TABLE `NWDAF`.`nwdafLoadLevelInformation` (
  `snssais` VARCHAR(128) NULL,
  `anySlice` TINYINT NULL,
  `currentLoadLevelInfo` INT NULL,
  `subscriptionID` VARCHAR(128) NULL);
  
  CREATE TABLE `NWDAF`.`nwdafSubscriptionTable` (
  `subscriptionID` VARCHAR(128) NOT NULL,
  `eventID` INT NULL,
  `notificationURI` VARCHAR(128) NULL,
  `notifMethod` TINYINT NULL,
  `repetitionPeriod` INT NULL,
  `loadLevelThreshold` INT NULL,
  `loadLevelID` INT NULL,
  PRIMARY KEY (`subscriptionID`));

  CREATE TABLE 	`NWDAF`.`nwdafSubTable` (
`subscriptionID` varchar(256) NOT NULL,
`correlationID` varchar(256) NULL,
`unSubCorrelationID` varchar(256) NULL);


 CREATE TABLE `NWDAF`.`nwdafCounterTable` (
`requestType` VARCHAR(256) NOT NULL,
`requestCount` int NOT NULL,
`responseType` VARCHAR(256) NOT NULL,
`responseCount` int );



