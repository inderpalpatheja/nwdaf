DROP SCHEMA IF EXISTS `NWDAF`;
CREATE SCHEMA `NWDAF` ;


CREATE TABLE `NWDAF`.`nwdafSubscriptionID` (
  `subscriptionId` VARCHAR(128) NOT NULL,
  `notificationURI` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`subscriptionId`));


CREATE TABLE `NWDAF`.`nwdafSubscriptionTable` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `subscriptionId` VARCHAR(128) NOT NULL,
  `event` INT NOT NULL,
  `notificationMethod` TINYINT NULL,
  `repetitionPeriod` INT NULL,
  PRIMARY KEY (`ID`),
    FOREIGN KEY (`subscriptionId`)
    REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `NWDAF`.`nwdafSliceLoadLevelSubscriptionData` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `subscriptionId` VARCHAR(128) NULL,
  `snssai` VARCHAR(128) NULL,
  `loadLevelThreshold` INT NULL,
  PRIMARY KEY (`ID`),
    FOREIGN KEY (`subscriptionId`)
    REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
 CREATE TABLE `NWDAF`.`nwdafSliceLoadLevelInformation` (
  `snssai` VARCHAR(128) NOT NULL,
  `currentLoadLevel` INT NULL,
  PRIMARY KEY (`snssai`));
  
  CREATE TABLE `NWDAF`.`nwdafSliceLoadLevelSubscriptionTable` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `snssai` VARCHAR(128) NULL,
  `subscriptionId` VARCHAR(128) NOT NULL,
  `correlationId` VARCHAR(128) NOT NULL,
  `refCount` int NOT NULL,
  PRIMARY KEY (`ID`));
  
    
   CREATE TABLE `NWDAF`.`nwdafUEmobilitySubscriptionData` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `subscriptionId` VARCHAR(128) NOT NULL,
  `supi` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ID`));
  
  
  CREATE TABLE `NWDAF`.`nwdafUEmobilitySubscriptionTable` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `supi` VARCHAR(128) NULL,
  `subscriptionId` VARCHAR(128) NOT NULL,
  `correlationId` VARCHAR(128) NOT NULL,
  `refCount` INT NOT NULL,
   PRIMARY KEY (`ID`));

CREATE TABLE `NWDAF`.`nwdafUEmobilityInformation` (
  `supi` VARCHAR(128) NOT NULL,
  `ts` DATETIME NOT NULL,
  `DurationSec` INT NOT NULL,
  PRIMARY KEY (`supi`));
  
  CREATE TABLE `NWDAF`.`nwdafUserLocation` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `tai` VARCHAR(128) NOT NULL, 
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
`subscriptionId` VARCHAR(128) NOT NULL,
`5Qi` INT NOT NULL,
`tai` VARCHAR(128) NOT NULL,
`snssai` VARCHAR(128) NOT NULL,
`ranUeThrouThrd` INT NULL,
`qosFlowRetThrd` INT NULL,
`relTimeUnit` VARCHAR(8) NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionId`)
REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);


CREATE TABLE `NWDAF`.`nwdafQosSustainabilityInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`tai` VARCHAR(128) NOT NULL,
`snssai` VARCHAR(128) NOT NULL,
`ranUeThrou` INT NULL,
`qosFlowRet` INT NULL,
PRIMARY KEY(`ID`));


CREATE TABLE `NWDAF`.`nwdafQosSustainabilitySubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`tai` VARCHAR(128) NOT NULL,
`snssai` VARCHAR(128) NOT NULL,
`subscriptionId` VARCHAR(128) NOT NULL,
`correlationId` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));






CREATE TABLE `NWDAF`.`nwdafServiceExperienceSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionId` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`snssai` VARCHAR(128) NOT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionId`)
REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);
  
  
  
  
CREATE TABLE `NWDAF`.`nwdafServiceExperienceInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`snssai` VARCHAR(128) NOT NULL,
`mos` FLOAT NOT NULL,
`upperRange` FLOAT NOT NULL,
`lowerRange` FLOAT NOT NULL,
PRIMARY KEY (`ID`));



CREATE TABLE `NWDAF`.`nwdafServiceExperienceSubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`snssai` VARCHAR(128) NOT NULL,
`subscriptionId` VARCHAR(128) NOT NULL,
`correlationId` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));





CREATE TABLE `NWDAF`.`nwdafNetworkPerformanceSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionId` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`nwPerfType` INT NOT NULL,
`relativeRatioThrd` INT NULL,
`absoluteNumThrd` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionId`)
REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
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
`subscriptionId` VARCHAR(128) NOT NULL,
`correlationId` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));







CREATE TABLE `NWDAF`.`nwdafUserDataCongestionSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionId` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`congType` INT NOT NULL,
`congThreshold` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionId`)
REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
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
`subscriptionId` VARCHAR(128) NOT NULL,
`correlationId` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));






CREATE TABLE `NWDAF`.`nwdafAbnormalBehaviourSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionId` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`excepId` INT NOT NULL,
`excepLevelThrd` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionId`)
REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);



CREATE TABLE `NWDAF`.`nwdafAbnormalBehaviourInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`excepId` INT NOT NULL,
`excepLevel` INT NOT NULL,
PRIMARY KEY (`ID`));


CREATE TABLE `NWDAF`.`nwdafAbnormalBehaviourSubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`excepId` INT NOT NULL,
`subscriptionId` VARCHAR(128) NOT NULL,
`correlationId` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));






CREATE TABLE `NWDAF`.`nwdafUeCommSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionId` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NOT NULL,
`maxAnaEntry` INT NOT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionId`)
REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);



CREATE TABLE `NWDAF`.`nwdafUeCommInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`commDur` INT NOT NULL,
`ts` VARCHAR(128) NOT NULL,
`ulVol` INT NULL,
`dlVol` INT NULL,
PRIMARY KEY (`ID`));


CREATE TABLE `NWDAF`.`nwdafUeCommSubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`supi` VARCHAR(128) NOT NULL,
`subscriptionId` VARCHAR(128) NOT NULL,
`correlationId` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));








CREATE TABLE `NWDAF`.`nwdafNfLoadSubscriptionData` (
`ID` INT NOT NULL AUTO_INCREMENT,
`subscriptionId` VARCHAR(128) NOT NULL,
`nfType` INT NOT NULL,
`nfInstanceId` VARCHAR(128) NOT NULL,
`supi` VARCHAR(128) NULL,
`snssai` VARCHAR(128) NULL,
`nfLoadLevelThrd` INT NULL,
`nfCpuUsageThrd` INT NULL,
`nfMemoryUsageThrd` INT NULL,
`nfStorageUsageThrd` INT NULL,
PRIMARY KEY (`ID`),
FOREIGN KEY (`subscriptionId`)
REFERENCES `NWDAF`.`nwdafSubscriptionID` (`subscriptionId`)
ON DELETE NO ACTION
ON UPDATE NO ACTION);



CREATE TABLE `NWDAF`.`nwdafNfLoadInformation` (
`ID` INT NOT NULL AUTO_INCREMENT,
`nfType` INT NOT NULL,
`nfInstanceId` VARCHAR(128) NOT NULL,
`nfLoadLevel` INT NOT NULL,
`nfCpuUsage` INT NOT NULL,
`nfMemoryUsage` INT NOT NULL,
`nfStorageUsage` INT NOT NULL,
PRIMARY KEY (`ID`));


CREATE TABLE `NWDAF`.`nwdafNfLoadSubscriptionTable` (
`ID` INT NOT NULL AUTO_INCREMENT,
`nfType` INT NOT NULL,
`nfInstanceId` VARCHAR(128) NOT NULL,
`subscriptionId` VARCHAR(128) NOT NULL,
`correlationId` VARCHAR(128) NOT NULL,
`refCount` INT NOT NULL,
PRIMARY KEY (`ID`));



  
  
