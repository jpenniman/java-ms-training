CREATE DATABASE  IF NOT EXISTS `inventory-db` /*!40100 DEFAULT CHARACTER SET latin1 */;

create user `inventory-user`@`%` identified by 'password';
grant all privileges on `inventory-db`.* to `inventory-user`@`%`;

USE `inventory-db`;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventory-db
-- ------------------------------------------------------
-- Server version	5.7.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Products`
--

DROP TABLE IF EXISTS `Products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Products` (
  `ProductID` int(11) NOT NULL AUTO_INCREMENT,
  `ProductName` varchar(40) NOT NULL,
  `SupplierID` int(11) DEFAULT NULL,
  `QuantityPerUnit` varchar(20) DEFAULT NULL,
  `UnitPrice` decimal(10,4) DEFAULT '0.0000',
  `UnitsInStock` smallint(2) DEFAULT '0',
  `UnitsOnOrder` smallint(2) DEFAULT '0',
  `ReorderLevel` smallint(2) DEFAULT '0',
  `Discontinued` bit(1) NOT NULL DEFAULT b'0',
  `Location` varchar(5) DEFAULT NULL,
  `Version` int(11) NOT NULL DEFAULT '1',
  `ObjectID` binary(16) DEFAULT NULL,
  PRIMARY KEY (`ProductID`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Products`
--

LOCK TABLES `Products` WRITE;
/*!40000 ALTER TABLE `Products` DISABLE KEYS */;
INSERT INTO `Products` VALUES (1,'Chai',1,'10 boxes x 20 bags',13.5000,39,0,10,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(2,'Chang',1,'24 - 12 oz bottles',14.2500,17,40,25,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(3,'Aniseed Syrup',1,'12 - 550 ml bottles',7.5000,13,70,25,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(4,'Chef Anton\'s Cajun Seasoning',2,'48 - 6 oz jars',16.5000,53,0,0,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(5,'Chef Anton\'s Gumbo Mix',2,'36 boxes',16.0100,0,0,0,_binary '','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(6,'Grandma\'s Boysenberry Spread',3,'12 - 8 oz jars',18.7500,120,0,25,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(7,'Uncle Bob\'s Organic Dried Pears',3,'12 - 1 lb pkgs.',22.5000,15,0,10,_binary '\0','P7',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(8,'Northwoods Cranberry Sauce',3,'12 - 12 oz jars',30.0000,6,0,0,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(9,'Mishi Kobe Niku',4,'18 - 500 g pkgs.',72.7500,29,0,0,_binary '','M6',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(10,'Ikura',4,'12 - 200 ml jars',23.2500,31,0,0,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(11,'Queso Cabrales',5,'1 kg pkg.',15.7500,22,30,30,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(12,'Queso Manchego La Pastora',5,'10 - 500 g pkgs.',28.5000,86,0,0,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(13,'Konbu',6,'2 kg box',4.5000,24,0,5,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(14,'Tofu',6,'40 - 100 g pkgs.',17.4400,35,0,0,_binary '\0','P7',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(15,'Genen Shouyu',6,'24 - 250 ml bottles',11.6300,39,0,5,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(16,'Pavlova',7,'32 - 500 g boxes',13.0900,29,0,10,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(17,'Alice Mutton',7,'20 - 1 kg tins',29.2500,0,0,0,_binary '','M6',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(18,'Carnarvon Tigers',7,'16 kg pkg.',46.8800,42,0,0,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(19,'Teatime Chocolate Biscuits',8,'10 boxes x 12 pieces',6.9000,25,0,5,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(20,'Sir Rodney\'s Marmalade',8,'30 gift boxes',60.7500,40,0,0,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(21,'Sir Rodney\'s Scones',8,'24 pkgs. x 4 pieces',7.5000,3,40,5,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(22,'Gustaf\'s Knckebrd',9,'24 - 500 g pkgs.',15.7500,104,0,25,_binary '\0','G5',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(23,'Tunnbrd',9,'12 - 250 g pkgs.',6.7500,61,0,25,_binary '\0','G5',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(24,'Guaran Fantstica',10,'12 - 355 ml cans',3.3800,20,0,0,_binary '','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(25,'NuNuCa Nu-Nougat-Creme',11,'20 - 450 g glasses',10.5000,76,0,30,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(26,'Gumbr Gummibrchen',11,'100 - 250 g bags',23.4200,15,0,0,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(27,'Schoggi Schokolade',11,'100 - 100 g pieces',32.9300,49,0,30,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(28,'Rssle Sauerkraut',12,'25 - 825 g cans',34.2000,26,0,0,_binary '','P7',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(29,'Thringer Rostbratwurst',12,'50 bags x 30 sausgs.',92.8400,0,0,0,_binary '','M6',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(30,'Nord-Ost Matjeshering',13,'10 - 200 g glasses',19.4200,10,0,15,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(31,'Gorgonzola Telino',14,'12 - 100 g pkgs',9.3800,0,70,20,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(32,'Mascarpone Fabioli',14,'24 - 200 g pkgs.',24.0000,9,40,25,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(33,'Geitost',15,'500 g',1.8800,112,0,20,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(34,'Sasquatch Ale',16,'24 - 12 oz bottles',10.5000,111,0,15,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(35,'Steeleye Stout',16,'24 - 12 oz bottles',13.5000,20,0,15,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(36,'Inlagd Sill',17,'24 - 250 g  jars',14.2500,112,0,20,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(37,'Gravad lax',17,'12 - 500 g pkgs.',19.5000,11,50,25,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(38,'Cte de Blaye',18,'12 - 75 cl bottles',197.6300,17,0,15,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(39,'Chartreuse verte',18,'750 cc per bottle',13.5000,69,0,5,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(40,'Boston Crab Meat',19,'24 - 4 oz tins',13.8000,123,0,30,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(41,'Jack\'s New England Clam Chowder',19,'12 - 12 oz cans',7.2400,85,0,10,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(42,'Singaporean Hokkien Fried Mee',20,'32 - 1 kg pkgs.',10.5000,26,0,0,_binary '','G5',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(43,'Ipoh Coffee',20,'16 - 500 g tins',34.5000,17,10,25,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(44,'Gula Malacca',20,'20 - 2 kg bags',14.5900,27,0,15,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(45,'Rogede sild',21,'1k pkg.',7.1300,5,70,15,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(46,'Spegesild',21,'4 - 450 g glasses',9.0000,95,0,0,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(47,'Zaanse koeken',22,'10 - 4 oz boxes',7.1300,36,0,0,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(48,'Chocolade',22,'10 pkgs.',9.5600,15,70,25,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(49,'Maxilaku',23,'24 - 50 g pkgs.',15.0000,10,60,15,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(50,'Valkoinen suklaa',23,'12 - 100 g bars',12.1900,65,0,30,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(51,'Manjimup Dried Apples',24,'50 - 300 g pkgs.',39.7500,20,0,10,_binary '\0','P7',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(52,'Filo Mix',24,'16 - 2 kg boxes',5.2500,38,0,25,_binary '\0','G5',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(53,'Perth Pasties',24,'48 pieces',24.6000,0,0,0,_binary '','M6',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(54,'Tourtire',25,'16 pies',5.5900,21,0,10,_binary '\0','M6',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(55,'Pt chinois',25,'24 boxes x 2 pies',18.0000,115,0,20,_binary '\0','M6',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(56,'Gnocchi di nonna Alice',26,'24 - 250 g pkgs.',28.5000,21,10,30,_binary '\0','G5',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(57,'Ravioli Angelo',26,'24 - 250 g pkgs.',14.6300,36,0,20,_binary '\0','G5',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(58,'Escargots de Bourgogne',27,'24 pieces',9.9400,62,0,20,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(59,'Raclette Courdavault',28,'5 kg pkg.',41.2500,79,0,0,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(60,'Camembert Pierrot',28,'15 - 300 g rounds',25.5000,19,0,0,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(61,'Sirop d\'rable',29,'24 - 500 ml bottles',21.3800,113,0,25,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(62,'Tarte au sucre',29,'48 pies',36.9800,17,0,0,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(63,'Vegie-spread',7,'15 - 625 g jars',32.9300,24,0,5,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(64,'Wimmers gute Semmelkndel',12,'20 bags x 4 pieces',24.9400,22,80,30,_binary '\0','G5',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(65,'Louisiana Fiery Hot Pepper Sauce',2,'32 - 8 oz bottles',15.7900,76,0,0,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(66,'Louisiana Hot Spiced Okra',2,'24 - 8 oz jars',12.7500,4,100,20,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(67,'Laughing Lumberjack Lager',16,'24 - 12 oz bottles',10.5000,52,0,10,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(68,'Scottish Longbreads',8,'10 boxes x 8 pieces',9.3800,6,10,15,_binary '\0','C3',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(69,'Gudbrandsdalsost',15,'10 kg pkg.',27.0000,26,0,15,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(70,'Outback Lager',7,'24 - 355 ml bottles',11.2500,15,10,30,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(71,'Flotemysost',15,'10 - 500 g pkgs.',16.1300,26,0,0,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(72,'Mozzarella di Giovanni',14,'24 - 200 g pkgs.',26.1000,14,0,0,_binary '\0','D4',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(73,'Rd Kaviar',17,'24 - 150 g jars',11.2500,101,0,5,_binary '\0','S8',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(74,'Longlife Tofu',4,'5 kg pkg.',7.5000,4,20,5,_binary '\0','P7',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(75,'Rhnbru Klosterbier',12,'24 - 0.5 l bottles',5.8100,125,0,25,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(76,'Lakkalikri',23,'500 ml',13.5000,57,0,20,_binary '\0','B1',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â'),(77,'Original Frankfurter grne Soe',12,'12 boxes',9.7500,32,0,15,_binary '\0','C2',1,_binary 'Ã°@4|ÃªÂ¢\"B\Â');
/*!40000 ALTER TABLE `Products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Suppliers`
--

DROP TABLE IF EXISTS `Suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Suppliers` (
  `SupplierID` int(11) NOT NULL AUTO_INCREMENT,
  `CompanyName` varchar(40) NOT NULL,
  `ContactName` varchar(30) DEFAULT NULL,
  `ContactTitle` varchar(30) DEFAULT NULL,
  `Address` varchar(60) DEFAULT NULL,
  `City` varchar(15) DEFAULT NULL,
  `Region` varchar(15) DEFAULT NULL,
  `PostalCode` varchar(10) DEFAULT NULL,
  `Country` varchar(15) DEFAULT NULL,
  `Phone` varchar(24) DEFAULT NULL,
  `Fax` varchar(24) DEFAULT NULL,
  `HomePage` mediumtext,
  `Version` int(11) NOT NULL DEFAULT '1',
  `ObjectID` binary(16) DEFAULT NULL,
  PRIMARY KEY (`SupplierID`),
  KEY `CompanyName` (`CompanyName`),
  KEY `PostalCode` (`PostalCode`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Suppliers`
--

LOCK TABLES `Suppliers` WRITE;
/*!40000 ALTER TABLE `Suppliers` DISABLE KEYS */;
INSERT INTO `Suppliers` VALUES (1,'Exotic Liquids','Charlotte Cooper','Purchasing Manager','49 Gilbert St.','London',NULL,'EC1 4SD','UK','(171) 555-2222',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(2,'New Orleans Cajun Delights','Shelley Burke','Order Administrator','P.O. Box 78934','New Orleans','LA','70117','USA','(100) 555-4822',NULL,'#CAJUN.HTM#',1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(3,'Grandma Kelly\'s Homestead','Regina Murphy','Sales Representative','707 Oxford Rd.','Ann Arbor','MI','48104','USA','(313) 555-5735','(313) 555-3349',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(4,'Tokyo Traders','Yoshi Nagase','Marketing Manager','9-8 Sekimai\nMusashino-shi','Tokyo',NULL,'100','Japan','(03) 3555-5011',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(5,'Cooperativa de Quesos \'Las Cabras\'','Antonio del Valle Saavedra ','Export Administrator','Calle del Rosal 4','Oviedo','Asturias','33007','Spain','(98) 598 76 54',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(6,'Mayumi\'s','Mayumi Ohno','Marketing Representative','92 Setsuko\nChuo-ku','Osaka',NULL,'545','Japan','(06) 431-7877',NULL,'Mayumi\'s (on the World Wide Web)#http://www.microsoft.com/accessdev/sampleapps/mayumi.htm#',1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(7,'Pavlova, Ltd.','Ian Devling','Marketing Manager','74 Rose St.\nMoonie Ponds','Melbourne','Victoria','3058','Australia','(03) 444-2343','(03) 444-6588',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(8,'Specialty Biscuits, Ltd.','Peter Wilson','Sales Representative','29 King\'s Way','Manchester',NULL,'M14 GSD','UK','(161) 555-4448',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(9,'PB Knckebrd AB','Lars Peterson','Sales Agent','Kaloadagatan 13','Gteborg',NULL,'S-345 67','Sweden ','031-987 65 43','031-987 65 91',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(10,'Refrescos Americanas LTDA','Carlos Diaz','Marketing Manager','Av. das Americanas 12.890','So Paulo',NULL,'5442','Brazil','(11) 555 4640',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(11,'Heli Swaren GmbH & Co. KG','Petra Winkler','Sales Manager','Tiergartenstrae 5','Berlin',NULL,'10785','Germany','(010) 9984510',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(12,'Plutzer Lebensmittelgromrkte AG','Martin Bein','International Marketing Mgr.','Bogenallee 51','Frankfurt',NULL,'60439','Germany','(069) 992755',NULL,'Plutzer (on the World Wide Web)#http://www.microsoft.com/accessdev/sampleapps/plutzer.htm#',1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(13,'Nord-Ost-Fisch Handelsgesellschaft mbH','Sven Petersen','Coordinator Foreign Markets','Frahmredder 112a','Cuxhaven',NULL,'27478','Germany','(04721) 8713','(04721) 8714',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(14,'Formaggi Fortini s.r.l.','Elio Rossi','Sales Representative','Viale Dante, 75','Ravenna',NULL,'48100','Italy','(0544) 60323','(0544) 60603','#FORMAGGI.HTM#',1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(15,'Norske Meierier','Beate Vileid','Marketing Manager','Hatlevegen 5','Sandvika',NULL,'1320','Norway','(0)2-953010',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(16,'Bigfoot Breweries','Cheryl Saylor','Regional Account Rep.','3400 - 8th Avenue\nSuite 210','Bend','OR','97101','USA','(503) 555-9931',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(17,'Svensk Sjfda AB','Michael Bjrn','Sales Representative','Brovallavgen 231','Stockholm',NULL,'S-123 45','Sweden','08-123 45 67',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(18,'Aux joyeux ecclsiastiques','Guylne Nodier','Sales Manager','203, Rue des Francs-Bourgeois','Paris',NULL,'75004','France','(1) 03.83.00.68','(1) 03.83.00.62',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(19,'New England Seafood Cannery','Robb Merchant','Wholesale Account Agent','Order Processing Dept.\n2100 Paul Revere Blvd.','Boston','MA','02134','USA','(617) 555-3267','(617) 555-3389',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(20,'Leka Trading','Chandra Leka','Owner','471 Serangoon Loop, Suite #402','Singapore',NULL,'0512','Singapore','555-8787',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(21,'Lyngbysild','Niels Petersen','Sales Manager','Lyngbysild\nFiskebakken 10','Lyngby',NULL,'2800','Denmark','43844108','43844115',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(22,'Zaanse Snoepfabriek','Dirk Luchte','Accounting Manager','Verkoop\nRijnweg 22','Zaandam',NULL,'9999 ZZ','Netherlands','(12345) 1212','(12345) 1210',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(23,'Karkki Oy','Anne Heikkonen','Product Manager','Valtakatu 12','Lappeenranta',NULL,'53120','Finland','(953) 10956',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(24,'G\'day, Mate','Wendy Mackenzie','Sales Representative','170 Prince Edward Parade\nHunter\'s Hill','Sydney','NSW','2042','Australia','(02) 555-5914','(02) 555-4873','G\'day Mate (on the World Wide Web)#http://www.microsoft.com/accessdev/sampleapps/gdaymate.htm#',1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(25,'Ma Maison','Jean-Guy Lauzon','Marketing Manager','2960 Rue St. Laurent','Montral','Qubec','H1J 1C3','Canada','(514) 555-9022',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(26,'Pasta Buttini s.r.l.','Giovanni Giudici','Order Administrator','Via dei Gelsomini, 153','Salerno',NULL,'84100','Italy','(089) 6547665','(089) 6547667',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(27,'Escargots Nouveaux','Marie Delamare','Sales Manager','22, rue H. Voiron','Montceau',NULL,'71300','France','85.57.00.07',NULL,NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(28,'Gai pturage','Eliane Noz','Sales Representative','Bat. B\n3, rue des Alpes','Annecy',NULL,'74000','France','38.76.98.06','38.76.98.58',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B'),(29,'Forts d\'rables','Chantal Goulet','Accounting Manager','148 rue Chasseur','Ste-Hyacinthe','Qubec','J2S 7S8','Canada','(514) 555-2955','(514) 555-2921',NULL,1,_binary 'ÃÂ¤4|ÃªÂ¢\"B');
/*!40000 ALTER TABLE `Suppliers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-11 17:49:09
