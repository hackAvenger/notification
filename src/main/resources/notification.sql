-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: localhost    Database: notification
-- ------------------------------------------------------
-- Server version	5.7.42

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
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  `recipient` varchar(255) NOT NULL,
  `body` text,
  `subject` text,
  `email_cc` varchar(255) DEFAULT NULL,
  `email_bcc` varchar(255) DEFAULT NULL,
  `request` text,
  `response` text,
  `reference_id` varchar(255) DEFAULT NULL,
  `retry_count` int(11) DEFAULT '0',
  `scheduled_time` datetime(6) DEFAULT NULL,
  `metadata` text,
  `category` enum('OTP','TRANSACTION','NOT_APPLICABLE') DEFAULT 'NOT_APPLICABLE',
  `type` enum('SMS','EMAIL','WHATSAPP','PUSH') DEFAULT NULL,
  `status` enum('DELIVERED','SENT','PENDING','FAILED','INPROGRESS') DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `vendor_id` bigint(20) DEFAULT NULL,
  `vendor_status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbv8kwwu9ebnqglt5lj6mf8gsm` (`product_id`),
  KEY `FK7usgy1efav68q2dfbjnf7a870` (`vendor_id`),
  CONSTRAINT `FK7usgy1efav68q2dfbjnf7a870` FOREIGN KEY (`vendor_id`) REFERENCES `vendor` (`id`),
  CONSTRAINT `FKbv8kwwu9ebnqglt5lj6mf8gsm` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification_attachment`
--

DROP TABLE IF EXISTS `notification_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) NOT NULL,
  `notification_id` bigint(20) DEFAULT NULL,
  `upload_type` enum('S3_LINK','FILE') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK23ehk4v86waa6oi9fg2l9t16r` (`notification_id`),
  CONSTRAINT `FK23ehk4v86waa6oi9fg2l9t16r` FOREIGN KEY (`notification_id`) REFERENCES `notification` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification_log`
--

DROP TABLE IF EXISTS `notification_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) DEFAULT NULL,
  `request` text,
  `response` text,
  `reference_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `notification_log_ibfk_1` FOREIGN KEY (`notification_id`) REFERENCES `notification` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=896 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  `product_key` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','DELETED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_vendor`
--

DROP TABLE IF EXISTS `product_vendor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_vendor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  `sender` varchar(200) DEFAULT NULL,
  `from_name` varchar(100) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','DELETED') DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `vendor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK60ssua8d4oysje24ijp9edkyt` (`product_id`),
  KEY `FK4dh5569om6a0g8xvmke9icrww` (`vendor_id`),
  CONSTRAINT `FK4dh5569om6a0g8xvmke9icrww` FOREIGN KEY (`vendor_id`) REFERENCES `vendor` (`id`),
  CONSTRAINT `FK60ssua8d4oysje24ijp9edkyt` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vendor`
--

DROP TABLE IF EXISTS `vendor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  `access_key` text,
  `secret_key` text,
  `base_url` varchar(255) DEFAULT NULL,
  `uris` varchar(255) DEFAULT NULL,
  `vendor_name` varchar(255) DEFAULT NULL,
  `vendor_type` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','DELETED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-07 17:06:17
