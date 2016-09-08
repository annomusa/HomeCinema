-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 08, 2016 at 05:29 AM
-- Server version: 5.6.24
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hbo`
--

-- --------------------------------------------------------

--
-- Table structure for table `channel`
--

CREATE TABLE IF NOT EXISTS `channel` (
  `ID_CHANNEL` int(11) NOT NULL,
  `NAME_CHANNEL` varchar(10) DEFAULT NULL,
  `DESCRIPTION` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `channel`
--

INSERT INTO `channel` (`ID_CHANNEL`, `NAME_CHANNEL`, `DESCRIPTION`) VALUES
(1, 'HBO HD', NULL),
(2, 'HBO Sign', NULL),
(3, 'HBO Fam', NULL),
(4, 'HBO Hits', NULL),
(5, 'Cinemax', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `film`
--

CREATE TABLE IF NOT EXISTS `film` (
  `ID_FILM` int(11) NOT NULL,
  `NAME_FILM` varchar(50) DEFAULT NULL,
  `PLOT` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `film`
--

INSERT INTO `film` (`ID_FILM`, `NAME_FILM`, `PLOT`) VALUES
(1, 'Dead Again', NULL),
(2, 'Zeus and Roxanne', NULL),
(3, 'About Alex', NULL),
(4, 'The Curious Case Of Benjamin Button', NULL),
(5, 'Land Ho!', NULL),
(6, 'The Vatican Tape', NULL),
(7, 'Dying Of The Light', NULL),
(8, 'Mr. Beans Holiday', NULL),
(9, 'Nancy Drew', NULL),
(10, 'Troy', NULL),
(11, 'Maleficent', NULL),
(12, 'Boyhood', NULL),
(13, 'Seven Pounds', NULL),
(14, 'Bessie', NULL),
(15, 'Superstar', NULL),
(16, 'Phil Spector', NULL),
(17, 'Girl, Interrupted', NULL),
(18, 'True Blood', NULL),
(19, 'The New', NULL),
(20, 'Project Greenlight', NULL),
(21, 'The Alzheimer''s Project', NULL),
(22, 'True Detective', NULL),
(23, 'Nightingale', NULL),
(24, 'Silicon Valley', NULL),
(25, 'Any Given Wednesday', NULL),
(26, 'Girls', NULL),
(27, 'Broadwalk Empire', NULL),
(28, 'Chop Shop', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jadwal`
--

CREATE TABLE IF NOT EXISTS `jadwal` (
  `ID_JADWAL` int(11) NOT NULL,
  `ID_FILM` int(11) DEFAULT NULL,
  `ID_CHANNEL` int(11) DEFAULT NULL,
  `WAKTU` time DEFAULT NULL,
  `TANGGAL` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `jadwal`
--

INSERT INTO `jadwal` (`ID_JADWAL`, `ID_FILM`, `ID_CHANNEL`, `WAKTU`, `TANGGAL`) VALUES
(1, 1, 1, '01:35:00', '2016-09-07'),
(2, 2, 1, '03:20:00', '2016-09-07'),
(3, 3, 1, '05:00:00', '2016-09-07'),
(4, 4, 1, '06:40:00', '2016-09-07'),
(5, 5, 1, '09:25:00', '2016-09-07'),
(6, 6, 1, '11:00:00', '2016-09-07'),
(7, 7, 1, '12:30:00', '2016-09-07'),
(8, 8, 1, '14:05:00', '2016-09-07'),
(9, 9, 1, '15:40:00', '2016-09-07'),
(10, 10, 1, '17:20:00', '2016-09-07'),
(11, 11, 1, '20:00:00', '2016-09-07'),
(12, 12, 1, '21:40:00', '2016-09-07'),
(13, 13, 2, '00:00:00', '2016-09-07'),
(14, 14, 2, '02:05:00', '2016-09-07'),
(15, 15, 2, '03:55:00', '2016-09-07'),
(16, 16, 2, '05:15:00', '2016-09-07'),
(17, 17, 2, '06:50:00', '2016-09-07');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `channel`
--
ALTER TABLE `channel`
  ADD PRIMARY KEY (`ID_CHANNEL`), ADD UNIQUE KEY `CHANNEL_PK` (`ID_CHANNEL`);

--
-- Indexes for table `film`
--
ALTER TABLE `film`
  ADD PRIMARY KEY (`ID_FILM`), ADD UNIQUE KEY `FILM_PK` (`ID_FILM`);

--
-- Indexes for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`ID_JADWAL`), ADD UNIQUE KEY `JADWAL_PK` (`ID_JADWAL`), ADD KEY `RELATIONSHIP_1_FK` (`ID_CHANNEL`), ADD KEY `RELATIONSHIP_2_FK` (`ID_FILM`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jadwal`
--
ALTER TABLE `jadwal`
ADD CONSTRAINT `FK_JADWAL_RELATIONS_CHANNEL` FOREIGN KEY (`ID_CHANNEL`) REFERENCES `channel` (`ID_CHANNEL`),
ADD CONSTRAINT `FK_JADWAL_RELATIONS_FILM` FOREIGN KEY (`ID_FILM`) REFERENCES `film` (`ID_FILM`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
