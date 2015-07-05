-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 05, 2015 at 07:01 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `stumark`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `CategoryId` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(40) NOT NULL,
  PRIMARY KEY (`CategoryId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `listings`
--

CREATE TABLE IF NOT EXISTS `listings` (
  `ListingId` int(11) NOT NULL AUTO_INCREMENT,
  `UserId` int(11) NOT NULL,
  `Name` varchar(40) NOT NULL,
  `CategoryId` int(11) NOT NULL,
  `SponsoredId` int(11) NOT NULL,
  `Description` text NOT NULL,
  `ListingType` varchar(1) NOT NULL,
  `Price` double NOT NULL,
  `PriceNegotiable` tinyint(1) NOT NULL,
  `PricePeriod` varchar(10) NOT NULL,
  `TransactionType` varchar(1) NOT NULL,
  `TransactionStart` date NOT NULL,
  `TransactionEnd` date DEFAULT NULL,
  `CreationDate` date NOT NULL,
  `ExpiryDate` date DEFAULT NULL,
  `isExpired` tinyint(1) NOT NULL,
  PRIMARY KEY (`ListingId`),
  KEY `UserId` (`UserId`),
  KEY `UserId_2` (`UserId`),
  KEY `CategoryId` (`CategoryId`),
  KEY `SponsoredId` (`SponsoredId`),
  KEY `SponsoredId_2` (`SponsoredId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=25 ;

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `MessageId` int(11) NOT NULL AUTO_INCREMENT,
  `ListingId` int(11) NOT NULL,
  `FromUserId` int(11) NOT NULL,
  `ToUserId` int(11) NOT NULL,
  `Message` text NOT NULL,
  `CreationDate` date NOT NULL,
  PRIMARY KEY (`MessageId`),
  KEY `ListingId` (`ListingId`,`FromUserId`,`ToUserId`),
  KEY `FromUserId` (`FromUserId`),
  KEY `ToUserId` (`ToUserId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE IF NOT EXISTS `payments` (
  `PaymentId` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `CreationDate` date NOT NULL,
  `Amount` double NOT NULL,
  PRIMARY KEY (`PaymentId`),
  KEY `UserID` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pictures`
--

CREATE TABLE IF NOT EXISTS `pictures` (
  `PictureId` int(11) NOT NULL AUTO_INCREMENT,
  `ListingId` int(11) NOT NULL,
  `Path` varchar(100) NOT NULL,
  PRIMARY KEY (`PictureId`),
  KEY `ListingId` (`ListingId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- Table structure for table `searchalerts`
--

CREATE TABLE IF NOT EXISTS `searchalerts` (
  `SearchAlertId` int(11) NOT NULL AUTO_INCREMENT,
  `PaymentId` int(11) NOT NULL,
  `CategoryId` int(11) NOT NULL,
  `ValidFrom` date NOT NULL,
  `ValidTo` date NOT NULL,
  `ListingTitle` varchar(40) NOT NULL,
  `Description` text NOT NULL,
  `ListingType` char(1) NOT NULL,
  `Price` double NOT NULL,
  `PriceNegotiable` tinyint(1) DEFAULT NULL,
  `PricePeriod` varchar(10) NOT NULL,
  `TransactionType` char(1) NOT NULL,
  `TransactionStart` date DEFAULT NULL,
  `TransactionEnd` date DEFAULT NULL,
  PRIMARY KEY (`SearchAlertId`),
  KEY `PaymentId` (`PaymentId`,`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `sponsoredlistings`
--

CREATE TABLE IF NOT EXISTS `sponsoredlistings` (
  `SponsoredListingId` int(11) NOT NULL AUTO_INCREMENT,
  `PaymentId` int(11) NOT NULL,
  `CategoryId` int(11) NOT NULL,
  `ValidFrom` date NOT NULL,
  `ValidTo` date NOT NULL,
  PRIMARY KEY (`SponsoredListingId`),
  KEY `PaymentId` (`PaymentId`,`CategoryId`),
  KEY `CategoryId` (`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `UserId` int(11) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(40) NOT NULL,
  `LastName` varchar(40) NOT NULL,
  `ActivationCode` varchar(10) DEFAULT NULL,
  `isActivated` tinyint(1) NOT NULL,
  `CreationDate` date NOT NULL,
  `Email` varchar(30) NOT NULL,
  `Password` varchar(30) NOT NULL,
  `AuthCode` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `listings`
--
ALTER TABLE `listings`
  ADD CONSTRAINT `listings_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserId`),
  ADD CONSTRAINT `listings_ibfk_2` FOREIGN KEY (`CategoryId`) REFERENCES `category` (`CategoryId`);

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`ListingId`) REFERENCES `listings` (`ListingId`),
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`FromUserId`) REFERENCES `users` (`UserId`),
  ADD CONSTRAINT `messages_ibfk_3` FOREIGN KEY (`ToUserId`) REFERENCES `users` (`UserId`);

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserId`);

--
-- Constraints for table `pictures`
--
ALTER TABLE `pictures`
  ADD CONSTRAINT `pictures_ibfk_1` FOREIGN KEY (`ListingId`) REFERENCES `listings` (`ListingId`);

--
-- Constraints for table `sponsoredlistings`
--
ALTER TABLE `sponsoredlistings`
  ADD CONSTRAINT `sponsoredlistings_ibfk_1` FOREIGN KEY (`PaymentId`) REFERENCES `payments` (`PaymentId`),
  ADD CONSTRAINT `sponsoredlistings_ibfk_2` FOREIGN KEY (`CategoryId`) REFERENCES `category` (`CategoryId`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
