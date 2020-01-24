-- MySQL dump 10.13  Distrib 8.0.13, for macos10.14 (x86_64)
--
-- Host: localhost    Database: EventSubDBMS
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `eventTable`
--

DROP TABLE IF EXISTS `eventTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `eventTable` (
  `subscriptionID` varchar(100) NOT NULL,
  `eventID` int(11) NOT NULL,
  `notificationURI` varchar(100) NOT NULL,
  `notifMethod` int(11) NOT NULL,
  `repetitionPeriod` int(11) DEFAULT NULL,
  `loadLevelThreshold` int(11) DEFAULT NULL,
  PRIMARY KEY (`subscriptionID`),
  UNIQUE KEY `eventID_UNIQUE` (`subscriptionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventTable`
--

LOCK TABLES `eventTable` WRITE;
/*!40000 ALTER TABLE `eventTable` DISABLE KEYS */;
INSERT INTO `eventTable` VALUES ('10ac1036-549a-4b32-9be7-02fd723cbc74',4,'https://www.amazon.com',1,0,5),('1d400afe-7c4f-4205-a8a9-52dff09ea442',5,'http://www.facebook.com/',1,0,11),('48c2f8c6-b044-482e-8890-fce7975164ca',5,'http://www.priority.com/',1,0,11),('c4b7dc38-8abc-47b8-825c-3c116e00d6bc',5,'http://www.vmware.com/',1,0,11),('d264b03b-2c1d-4a1a-8ca8-ff174976efe2',5,'http://www.facefbook.com/',1,0,11),('e06b049e-9674-49fa-bf06-94a45f777e6b',5,'https://www.google.com',1,0,11);
/*!40000 ALTER TABLE `eventTable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-24 12:15:22
