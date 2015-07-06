-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: stumark
-- ------------------------------------------------------
-- Server version	5.6.25-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `category`
--

LOCK TABLES `Category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `Category` VALUES (1,'Accomodation'),(2,'Vehicles'),(3,'Books'),(4,'House items'),(5,'Clothing'),(6,'Pets'),(7,'Music'),(8,'Electronics'),(9,'Others');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `listings`
--

LOCK TABLES `Listings` WRITE;
/*!40000 ALTER TABLE `listings` DISABLE KEYS */;
INSERT INTO `Listings` VALUES (6,10,'Amazing Student Studio',1,0,'Modern and fashion student apartment. 35 sqm near Arcistrasse','O',550,1,'M','R','2015-07-14','2015-07-31','2015-07-05','2015-12-01',0),(7,4,'Student Room in Family house',1,0,'I rent my daughter\'s room for a female student. 15 sqm in nice location in Munich','O',450,0,'D','R','2015-09-05','2016-09-05','2015-07-05','2015-08-01',0),(8,6,'Cocker spaniel male',6,0,'My little cocker spaniel Nini needs a groom. Write me if you have a cocker spaniel with pedigree papers.','R',500,1,'M','B','2015-07-05','2015-09-05','2015-07-05','2015-07-05',0),(9,9,'Looking for a guitar during summer',7,0,'I want to rent a an electrical guitar during the summer. I want one similar to any of the picture. Let me know if you can rent yours at least a month.','R',50,1,'M','R','2015-08-01','2015-10-01','2015-07-05','2015-08-01',0),(10,5,'Renting my electrical guitar ',7,0,'I rent my electrical guitar since September. I will be out of Munich for six months so if you want to enjoy music in your hands, send me a message. I punt a monthly price but we can arrange a price for whole semester.','O',70,1,'M','R','2015-09-01','2016-01-01','2015-07-05','2015-08-01',0),(12,2,'Good condition bike for the WS semester',2,0,'I will be out in exchange semester, I rent my bike for the whole WS. ','O',25,1,'M','R','2015-06-05','2016-02-02','2015-07-05',NULL,0),(13,7,'Available German language book A1.2',3,0,'I want to sell a German book A1.2 for the semester.  It has notes and marks.I find them useful','O',10,1,'A','S','2015-07-05','2015-09-01','2015-07-05','2015-08-10',0),(14,7,'Unity party ticket',9,0,'I have two free tickets for unity party. Bought them on time but can not go.','O',14,0,'D','S','2015-06-05','2015-09-01','2015-07-05','2015-07-05',0),(15,3,'Books about cognitive systems',3,0,'I am looking for books on cognitive systems. I collect them so let me know if you have any of them.','R',15,0,'D','B','2015-07-15','2015-09-01','2015-07-05','2015-07-08',0),(16,8,'Professional photo camera ',8,0,'I rent my professional Nikon x3. It is in good condition and you can enjoy the weather hanging outside with real photo experience.','O',14,1,'W','R','2015-06-05','2015-09-01','2015-07-05','2015-09-01',0),(17,8,'Share a schones wochenende ticket',2,0,'I have a ticket for four on DB line for summer. We can share places with two more people. Trip is from Munich to Berlin.','O',10,1,'D','R','2015-08-05','2015-10-05','2015-07-05','2015-12-05',0),(18,6,'Cocker Puppies look for home',6,0,'I have 10 pure cocker spaniel puppies. We look for a good family to them. Write me in case you are interested.','O',10,1,'W','S','2015-06-05','2015-07-05','2015-07-05','2015-07-05',0);
/*!40000 ALTER TABLE `listings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `messages`
--

LOCK TABLES `Messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `Messages` VALUES (1,12,7,2,'Hey, I am interested in your bike. I want it for 3 months, is it ok?','2015-07-05'),(2,15,6,3,'I have a couple of books on the topic. Will you give me 30 euros for both? ISB9826262 and ISBN 922671292','2015-07-05');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `payments`
--

LOCK TABLES `Payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `pictures`
--

LOCK TABLES `Pictures` WRITE;
/*!40000 ALTER TABLE `pictures` DISABLE KEYS */;
INSERT INTO `Pictures` VALUES (2,6,'L620150705140138221.jpeg'),(3,7,'L720150705141653209.jpeg'),(4,9,'L920150705145832136.jpeg'),(5,10,'L1020150705150212800.jpeg'),(6,8,'L1120150705150222240.jpeg'),(7,12,'L1220150705161654772.jpeg'),(8,13,'L1320150705164043184.jpeg'),(9,14,'L142015070516420275.jpeg'),(10,16,'L1620150705165346288.jpeg'),(11,17,'L1720150705165639520.jpeg'),(12,18,'L1820150705165938253.jpeg');
/*!40000 ALTER TABLE `pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `searchalerts`
--

LOCK TABLES `Searchalerts` WRITE;
/*!40000 ALTER TABLE `searchalerts` DISABLE KEYS */;
/*!40000 ALTER TABLE `searchalerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sponsoredlistings`
--

LOCK TABLES `Sponsoredlistings` WRITE;
/*!40000 ALTER TABLE `sponsoredlistings` DISABLE KEYS */;
/*!40000 ALTER TABLE `sponsoredlistings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,'sai','kiran','abcde',1,'2015-06-07','124234@gmail.com','123456',''),(2,'Fabiola','Moyon','36109',1,'2015-07-05','fabiola@tum.de','fabiola','0658344558'),(3,'Sai','Kiran','19322',1,'2015-07-05','sai@tum.de','sai','2823081511'),(4,'Madhukar','Sollepura','03971',1,'2015-07-05','madhukar@tum.de','madhukar','0988828975'),(5,'Joaquin','Quintanar','53211',1,'2015-07-05','joaquin@tum.de','joaquin',NULL),(6,'Andreas','Mortz','13092',1,'2015-07-05','andreas@tum.de','andreas','8060991965'),(7,'Peter','Jhonson','77091',1,'2015-07-05','peter@tum.de','peter','2043859541'),(8,'Jade','Ferraz','58787',1,'2015-07-05','jade@tum.de','jade','5791307019'),(9,'Ali','Sahid','74688',1,'2015-07-05','ali@tum.de','ali','2496825257'),(10,'Latifa','Fayade','95839',1,'2015-07-05','latifa@tum.de','latifa','0740930926');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-07-05 17:14:44
