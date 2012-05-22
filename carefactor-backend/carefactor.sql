-- phpMyAdmin SQL Dump
-- version 3.4.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 22, 2012 at 08:00 AM
-- Server version: 5.1.52
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `carefactor`
--

-- --------------------------------------------------------

--
-- Table structure for table `cf_food_bank`
--

CREATE TABLE IF NOT EXISTS `cf_food_bank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_log_id` varchar(22) NOT NULL,
  `producer_id` varchar(22) NOT NULL,
  `user_id` int(11) NOT NULL,
  `category` varchar(22) NOT NULL,
  `foodname` varchar(22) NOT NULL,
  `quantity` varchar(22) NOT NULL,
  `unit` varchar(11) NOT NULL,
  `description` text NOT NULL,
  `expiry_date` date NOT NULL,
  `pick_up_date_start` date NOT NULL,
  `pick_up_date_end` date NOT NULL,
  `is_free` enum('TRUE','FALSE') NOT NULL,
  `currency` varchar(22) NOT NULL,
  `price` double NOT NULL,
  `update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `cf_food_bank`
--

INSERT INTO `cf_food_bank` (`id`, `resource_log_id`, `producer_id`, `user_id`, `category`, `foodname`, `quantity`, `unit`, `description`, `expiry_date`, `pick_up_date_start`, `pick_up_date_end`, `is_free`, `currency`, `price`, `update_timestamp`) VALUES
(1, '', 'demo-producer-1', 2, 'Animal Products', 'Beef', '50', 'Kg', 'Good beef, expires in about a week', '2012-02-28', '2012-02-18', '2012-02-28', 'FALSE', 'SEK', 30, '2012-02-17 18:48:03'),
(2, '', 'demo-producer-1', 2, 'Fruits', 'Apple', '1', 'Quantity', 'Apple are in good state. Giving away for free', '2012-02-24', '2012-02-18', '2012-02-24', 'TRUE', '-', 0, '2012-02-17 19:05:15'),
(3, '', 'demo-producer-2', 3, 'Beverages', 'Fruit juice', '1', 'Quantity', 'Flavor includes apple, citrus etc', '2012-02-22', '2012-02-18', '2012-02-26', 'FALSE', 'SEK', 10, '2012-02-17 21:26:46'),
(4, '', 'demo-producer-2', 3, 'Others', 'Yoghurt', '1', 'Quantity', 'Not fit for sale container is slightly damaged', '2012-04-18', '2012-02-18', '2012-04-18', 'TRUE', '-', 0, '2012-02-17 21:29:34'),
(5, '', 'demo-producer-1', 2, 'Others', 'Onions', '190', 'Kg', 'Old stock needs clearing', '2012-03-18', '2012-02-18', '2012-03-18', 'FALSE', 'SEK', 10, '2012-02-17 22:51:02'),
(6, '', 'demo-producer-1', 2, 'Fruits', 'Oranges', '100', 'Quantity', 'Expires in about a week. Fruits are in good shape', '2012-02-26', '2012-02-18', '2012-02-26', 'TRUE', '-', 0, '2012-02-17 23:08:46'),
(7, '', 'demo-producer-1', 2, 'Animal Products', 'Fish', '40', 'Kg', 'Need to clear old stock.', '2012-02-21', '2012-02-18', '2012-02-21', 'FALSE', 'SEK', 20, '2012-02-17 23:10:41'),
(8, '', 'demo-producer-1', 2, 'Animal Products', 'Beef', '50', 'Kg', 'Expires in soon.', '2012-02-23', '2012-02-18', '2012-02-22', 'FALSE', 'SEK', 20, '2012-02-17 23:53:15'),
(9, '', 'demo-producer-1', 2, 'Fruits', 'Carrot', '600', 'Kg', 'Just cosmetic defects. Product is consumable', '2012-02-28', '2012-02-18', '2012-02-28', 'TRUE', '-', 0, '2012-02-18 00:39:46'),
(10, '', 'demo-producer-3', 2, 'Animal Products', 'Beef', '50', 'Kg', 'Good beef, expires in about a week', '2012-02-28', '2012-02-18', '2012-02-28', 'FALSE', 'SEK', 30, '2012-02-17 17:48:03'),
(11, '', 'demo-producer-5', 2, 'Fruits', 'Apple', '1', 'Quantity', 'Apple are in good state. Giving away for free', '2012-02-24', '2012-02-18', '2012-02-24', 'TRUE', '-', 0, '2012-02-17 18:05:15'),
(12, '', 'demo-producer-4', 3, 'Beverages', 'Fruit juice', '1', 'Quantity', 'Flavor includes apple, citrus etc', '2012-02-22', '2012-02-18', '2012-02-26', 'FALSE', 'SEK', 10, '2012-02-17 20:26:46'),
(13, '', 'demo-producer-3', 3, 'Others', 'Yoghurt', '1', 'Quantity', 'Not fit for sale container is slightly damaged', '2012-04-18', '2012-02-18', '2012-04-18', 'TRUE', '-', 0, '2012-02-17 20:29:34'),
(14, '', 'demo-producer-2', 2, 'Vegetables', 'Onions', '190', 'Kg', 'Old stock needs clearing', '2012-03-18', '2012-02-18', '2012-03-18', 'FALSE', 'SEK', 10, '2012-02-17 21:51:02'),
(15, '', 'demo-producer-3', 2, 'Fruits', 'Oranges', '100', 'Quantity', 'Expires in about a week. Fruits are in good shape', '2012-02-26', '2012-02-18', '2012-02-26', 'TRUE', '-', 0, '2012-02-17 22:08:46'),
(16, '', 'demo-producer-1', 2, 'Animal Products', 'Fish', '40', 'Kg', 'Need to clear old stock.', '2012-02-21', '2012-02-18', '2012-02-21', 'FALSE', 'SEK', 20, '2012-02-17 22:10:41'),
(17, '', 'demo-producer-1', 2, 'Animal Products', 'Beef', '50', 'Kg', 'Expires in soon.', '2012-02-23', '2012-02-18', '2012-02-22', 'FALSE', 'SEK', 20, '2012-02-17 22:53:15'),
(18, '', 'demo-producer-1', 2, 'Fruits', 'Carrot', '600', 'Kg', 'Just cosmetic defects. Product is consumable', '2012-02-28', '2012-02-18', '2012-02-28', 'TRUE', '-', 0, '2012-02-17 23:39:46'),
(19, '', 'demo-producer-1', 2, 'Animal Products', 'Testing phase', '1', 'Quantity', 'Testing phase', '2012-02-26', '2012-02-26', '2012-02-26', 'TRUE', '-', 0, '2012-02-25 23:06:36'),
(20, '', 'demo-producer-1', 2, 'Animal Products', 'Elg steak', '5', 'Kg', 'Fine meat expires in 2 days', '2013-03-09', '2012-03-07', '2013-03-08', 'FALSE', 'SEK', 59, '2012-03-07 16:41:31');

-- --------------------------------------------------------

--
-- Table structure for table `ci_sessions`
--

CREATE TABLE IF NOT EXISTS `ci_sessions` (
  `session_id` varchar(40) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `ip_address` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `user_agent` varchar(150) COLLATE utf8_bin NOT NULL,
  `last_activity` int(10) unsigned NOT NULL DEFAULT '0',
  `user_data` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `ci_sessions`
--

INSERT INTO `ci_sessions` (`session_id`, `ip_address`, `user_agent`, `last_activity`, `user_data`) VALUES
('043f577d4b44603fd3392f3529a36724', '202.247.120.22', 'CareFactor-Android_based', 1335157294, ''),
('41592fbf3a001f9defc8ecaf23d245a7', '202.247.120.22', 'CareFactor-Android_based', 1335157253, ''),
('7950d9109e7b3d9f8db27e820e6719b3', '202.247.120.22', 'CareFactor-Android_based', 1335157275, ''),
('ad27407a72d0d0be0ab5e02c8aa841cf', '202.247.120.22', 'CareFactor-Android_based', 1335157268, ''),
('b6ea23e78bfed35fe68680702111f3db', '202.247.120.22', 'CareFactor-Android_based', 1335157265, ''),
('b874370bc21522286c5cfdfe4218b0eb', '202.247.120.22', 'CareFactor-Android_based', 1335157283, ''),
('eeb615b72531d72adc2a0caaaaee95c5', '202.247.120.22', 'CareFactor-Android_based', 1335157240, ''),
('f51ccfd2f6aa12ed23baca7a1e645fef', '202.247.120.22', 'CareFactor-Android_based', 1335157261, ''),
('f58e3d7b1a506b9a423ec22bffe4cb5c', '202.247.120.22', 'CareFactor-Android_based', 1335157248, ''),
('f652ea6bc685837d132397093cc6faaa', '202.247.120.22', 'CareFactor-Android_based', 1335157283, '');

-- --------------------------------------------------------

--
-- Table structure for table `login_attempts`
--

CREATE TABLE IF NOT EXISTS `login_attempts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(40) COLLATE utf8_bin NOT NULL,
  `login` varchar(50) COLLATE utf8_bin NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `system_notifications`
--

CREATE TABLE IF NOT EXISTS `system_notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `producer_id` varchar(200) NOT NULL,
  `not_type` varchar(11) NOT NULL,
  `not_heading` varchar(100) NOT NULL,
  `not_body` varchar(500) NOT NULL,
  `update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=38 ;

--
-- Dumping data for table `system_notifications`
--

INSERT INTO `system_notifications` (`id`, `producer_id`, `not_type`, `not_heading`, `not_body`, `update_timestamp`) VALUES
(1, '2', 'food_update', 'demo-producer-1: Beef available', 'Good beef, expires in about a week', '2012-02-17 18:48:03'),
(2, '2', 'food_update', 'demo-producer-1: Apple available', 'Apple are in good state. Giving away for free', '2012-02-17 19:05:15'),
(3, '2', 'advert', 'Food product Sales', 'Get up to 25% sales on food product NOW!', '2012-02-17 19:13:23'),
(35, '2', 'food_update', 'demo-producer-1:Testing phase available', 'Testing phase', '2012-02-25 23:06:34'),
(5, '3', 'advert', 'Free offer NOW!', 'Get one item free for every purchase you make', '2012-02-17 21:24:56'),
(6, '3', 'food_update', 'demo-poducer-2: Fruit juice available', 'Flavor includes apple, citrus etc', '2012-02-17 21:26:46'),
(7, '3', 'food_update', 'demo-poducer-2: Yoghurt available', 'Not fit for sale container is slightly damaged', '2012-02-17 21:29:34'),
(37, '2', 'advert', 'OLW Chips hot&spicy;', 'New Product', '2012-03-07 16:43:16'),
(33, '2', 'advert', 'Special offer!', 'Best deal! Get 75% off every sales.', '2012-02-18 00:52:49'),
(32, '2', 'food_update', 'demo-producer-1: Carrot available', 'Just cosmetic defects. Product is consumable', '2012-02-18 00:39:43'),
(34, '2', 'advert', 'New ads', 'Testing phase', '2012-02-25 23:06:05'),
(36, '2', 'food_update', 'demo-producer-1:Elg steak available', 'Fine meat expires in 2 days', '2012-03-07 16:41:30');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_bin NOT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `email` varchar(100) COLLATE utf8_bin NOT NULL,
  `activated` tinyint(1) NOT NULL DEFAULT '1',
  `banned` tinyint(1) NOT NULL DEFAULT '0',
  `ban_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `new_password_key` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `new_password_requested` datetime DEFAULT NULL,
  `new_email` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `new_email_key` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `last_ip` varchar(40) COLLATE utf8_bin NOT NULL,
  `last_login` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=14 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `activated`, `banned`, `ban_reason`, `new_password_key`, `new_password_requested`, `new_email`, `new_email_key`, `last_ip`, `last_login`, `created`, `modified`) VALUES
(1, 'samup4web', '$2a$08$yJusD3Sz79bVDKyonVPpSOPe7h0emKkGxjlLw9l978LsNA4YeVVUS', 'samup4web@gmail.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '180.36.92.225', '2012-02-17 20:38:58', '2012-02-17 01:26:24', '2012-02-17 19:38:58'),
(2, 'demo-producer-1', '$2a$08$mfyC0gq7ezs2ilfm4E.U/OIKvf20OBqaWUrv3zcejqBPgUpaltY6y', 'demo1@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '90.230.128.35', '2012-03-07 17:38:57', '2012-02-17 01:27:18', '2012-03-07 16:38:57'),
(3, 'demo-producer-2', '$2a$08$oWpeTdr.oWGCVLQ/GQHh5uQOCxGzSvybOZJkW5lfUksFrY7OANR.m', 'demo2@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '180.36.92.225', '2012-02-17 22:22:51', '2012-02-17 01:27:36', '2012-02-18 07:17:07'),
(4, 'demo-producer-3', '$2a$08$8Ci2413qAW6pVI3CPKfvvObtgJlC1GON2HVjGsTvx6W0RZl4cTpry', 'demo3@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '0000-00-00 00:00:00', '2012-02-17 01:27:49', '2012-02-18 07:17:07'),
(5, 'demo-producer-4', '$2a$08$3s.A69KI4VMPlbj7iEwHH.x0md3/FVkWP93DLlTMMyy5ISPFQD/3u', 'demo4@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '0000-00-00 00:00:00', '2012-02-17 01:28:10', '2012-02-18 07:17:07'),
(6, 'demo-producer-5', '$2a$08$EHtAW5L.WpHMGARtRwc7x.88IZtgXoo7C1.6bzhJG74Nf8sNpKx92', 'demo5@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '0000-00-00 00:00:00', '2012-02-17 01:28:25', '2012-02-18 07:17:07'),
(7, 'demo-consumer-1', '$2a$08$GIl8BFHAx.WxgqZJdtWziu2ZBZGOmKGdy6vDHnu45/dH2rVwVu9Wu', 'demo-consumer1@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '90.230.128.35', '2012-03-07 17:44:32', '2012-02-17 01:29:23', '2012-03-07 16:44:32'),
(8, 'demo-consumer-2', '$2a$08$E7zlL1cOgop2pGIiQ2PTeuaYddpyC2W5TaWQc.ee7hNTKROMuGHWa', 'demo-consumer2@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '2012-02-23 05:20:33', '2012-02-17 01:30:22', '2012-02-23 04:20:33'),
(9, 'demo-consumer-3', '$2a$08$VTMFvd0WVgrcifdOwkf2r.zKE60D8fdesWsi6NbITm3ECHyZuNWJO', 'demo-consumer3@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '0000-00-00 00:00:00', '2012-02-17 01:30:35', '2012-02-17 00:30:35'),
(10, 'demo-consumer-4', '$2a$08$itajYba6HuIz73Zi06kWy.z4IFbJ7C2FNpE0pOtpZZk0qXgsr0SCa', 'demo-consumer4@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '0000-00-00 00:00:00', '2012-02-17 01:30:58', '2012-02-17 00:30:58'),
(11, 'demo-consumer-5', '$2a$08$zGdi89RVHMyGGPZZCF5PUuzY/3zcLxC.aWXtH6DenYjvBiH4l1UNG', 'demo-consumer5@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '0000-00-00 00:00:00', '2012-02-17 01:31:18', '2012-02-17 00:31:18'),
(12, 'samup4web2', '$2a$08$ZRroAGg0MsyCLFb/gH9qkenBhV3UpYw45yqkOI.1ehO9KTfaXBIVK', 'demo-consumer6@carefactor.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '202.247.120.22', '0000-00-00 00:00:00', '2012-02-17 01:32:03', '2012-02-17 00:32:03'),
(13, 'samido', '$2a$08$ihIJNz6g6TT7J7wq.LULEenulPousbC6mJeUV/GmkyxJbRvHaU0gu', 'd@t.com', 1, 0, NULL, NULL, NULL, NULL, NULL, '180.36.92.225', '2012-02-17 19:02:42', '2012-02-17 18:29:03', '2012-02-17 18:02:42');

-- --------------------------------------------------------

--
-- Table structure for table `user_autologin`
--

CREATE TABLE IF NOT EXISTS `user_autologin` (
  `key_id` char(32) COLLATE utf8_bin NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `user_agent` varchar(150) COLLATE utf8_bin NOT NULL,
  `last_ip` varchar(40) COLLATE utf8_bin NOT NULL,
  `last_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`key_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `user_autologin`
--

INSERT INTO `user_autologin` (`key_id`, `user_id`, `user_agent`, `last_ip`, `last_login`) VALUES
('0610e55a0a8c6339233cca5df6a16795', 2, 'CareFactor-Android_based', '90.230.128.35', '2012-03-07 16:38:57'),
('1c9b67f680b902599e8f743b17ab5139', 7, 'CareFactor-Android_based', '202.247.120.22', '2012-03-05 02:06:52'),
('1ede884735103da52f39a23fc91b8a0d', 13, 'CareFactor-Android_based', '180.36.92.225', '2012-02-17 18:02:42'),
('409384a2c3cad0744a70c920923b013e', 2, 'CareFactor-Android_based', '180.36.92.225', '2012-02-25 23:05:33'),
('50e22908f96883ae1145825a8e56a836', 3, 'CareFactor-Android_based', '180.36.92.225', '2012-02-17 21:22:51'),
('557638e9e4f2bfc4416045e6d4a79346', 7, 'CareFactor-Android_based', '46.162.93.210', '2012-02-25 23:14:02'),
('79a7580a506d6271eb097565585ee685', 7, 'CareFactor-Android_based', '180.36.92.225', '2012-02-25 06:34:17'),
('7e97c6314232f6dbcdfc81e5c2ee81ba', 7, 'CareFactor-Android_based', '90.235.164.225', '2012-03-07 12:35:21'),
('a367b0e1b94227786a20b5b6911248d7', 8, 'CareFactor-Android_based', '202.247.120.22', '2012-02-23 04:20:33'),
('acd0e6a1fcbbcc3f9568e858eb76d2ff', 1, 'CareFactor-Android_based', '180.36.92.225', '2012-02-17 19:38:58'),
('bda85f4c9e8abcf5c50c7e9af1417293', 2, 'CareFactor-Android_based', '202.247.120.22', '2012-02-27 06:06:04'),
('d1b092b8939e8af795041931807b38b7', 7, 'CareFactor-Android_based', '90.230.128.35', '2012-03-07 16:44:32');

-- --------------------------------------------------------

--
-- Table structure for table `user_profiles`
--

CREATE TABLE IF NOT EXISTS `user_profiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_type` enum('consumer','agent','producer') COLLATE utf8_bin NOT NULL,
  `choice_producer` varchar(2000) COLLATE utf8_bin NOT NULL,
  `phone_no` varchar(22) COLLATE utf8_bin NOT NULL,
  `is_notification_active` enum('false','true') COLLATE utf8_bin NOT NULL DEFAULT 'false',
  `country` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `locality` varchar(32) COLLATE utf8_bin NOT NULL,
  `address_line` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `is_override_auto_location` enum('false','true') COLLATE utf8_bin NOT NULL DEFAULT 'false',
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `ratings_value` int(11) NOT NULL,
  `ratings_user_no` int(11) NOT NULL,
  `web_site` varchar(160) COLLATE utf8_bin NOT NULL DEFAULT 'http://',
  `facebook_page_link` varchar(160) COLLATE utf8_bin NOT NULL DEFAULT 'https://',
  `other_contact_details` varchar(160) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=14 ;

--
-- Dumping data for table `user_profiles`
--

INSERT INTO `user_profiles` (`id`, `user_id`, `user_type`, `choice_producer`, `phone_no`, `is_notification_active`, `country`, `locality`, `address_line`, `is_override_auto_location`, `lat`, `lon`, `ratings_value`, `ratings_user_no`, `web_site`, `facebook_page_link`, `other_contact_details`) VALUES
(1, 1, 'producer', '', '', 'false', '', '', '', 'false', 34.704948, 135.6937932, 4, 2, 'https://www.facebook.com/pages/CareFactor/281976968505841', 'https://www.facebook.com/pages/CareFactor/281976968505841', ''),
(2, 2, 'producer', '', '+47000000', 'false', 'Sverige', 'Luleå', 'Luleå tekniska universitet', 'true', 65.6212145, 22.1417342666667, 3, 1, 'https://www.facebook.com/pages/CareFactor/281976968505841', 'https://www.facebook.com/pages/CareFactor/281976968505841', ''),
(3, 3, 'producer', '', '+47000000', 'false', 'Sweden', 'Lulea', '', 'false', 0, 0, 1, 3, 'https://www.facebook.com/pages/CareFactor/281976968505841', 'https://www.facebook.com/pages/CareFactor/281976968505841', ''),
(4, 4, 'producer', '', '+47000000', 'false', 'Sweden', 'Lulea', NULL, 'false', 0, 0, 5, 1, 'https://www.facebook.com/pages/CareFactor/281976968505841', 'https://www.facebook.com/pages/CareFactor/281976968505841', ''),
(5, 5, 'producer', '', '+47000000', 'false', 'Sweden', 'Lulea', NULL, 'false', 0, 0, 3, 2, 'https://www.facebook.com/pages/CareFactor/281976968505841', 'https://www.facebook.com/pages/CareFactor/281976968505841', ''),
(6, 6, 'producer', '', '+47000000', 'false', 'Sweden', 'Lulea', NULL, 'false', 0, 0, 0, 0, 'https://www.facebook.com/pages/CareFactor/281976968505841', 'https://www.facebook.com/pages/CareFactor/281976968505841', ''),
(7, 7, 'consumer', 'demo-producer-1,demo-producer-2,demo-producer-3,demo-producer-4,demo-producer-5,samup4web', '+46730556014', 'true', 'Japan', 'Ikoma', 'Japan', 'false', 34.733725775, 135.734395625, 0, 0, 'http://', 'https://', ''),
(8, 8, 'consumer', '', '', 'false', '', '', '', 'false', 34.7049082666667, 135.693818433333, 0, 0, 'http://', 'https://', ''),
(9, 9, 'consumer', '', '', 'false', NULL, '', NULL, 'false', 0, 0, 0, 0, 'http://', 'https://', ''),
(10, 10, 'consumer', '', '', 'false', NULL, '', NULL, 'false', 0, 0, 0, 0, 'http://', 'https://', ''),
(11, 11, 'consumer', '', '', 'false', NULL, '', NULL, 'false', 0, 0, 0, 0, 'http://', 'https://', ''),
(12, 12, 'consumer', '', '', 'false', NULL, '', NULL, 'false', 0, 0, 0, 0, 'http://', 'https://', ''),
(13, 13, 'producer', '', '', 'false', '', '', '', 'false', 34.704948, 135.6937932, 0, 0, 'https://www.facebook.com/pages/CareFactor/281976968505841', 'https://www.facebook.com/pages/CareFactor/281976968505841', '');

-- --------------------------------------------------------

--
-- Table structure for table `wish_list`
--

CREATE TABLE IF NOT EXISTS `wish_list` (
  `wishlist_id` int(11) NOT NULL AUTO_INCREMENT,
  `wishlist_user_id` int(11) NOT NULL,
  `wishlist_food_id` int(11) NOT NULL,
  `wishlist_quantity` int(11) NOT NULL,
  `wishlist_state` enum('booked','picked') NOT NULL,
  `date_booked` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`wishlist_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `wish_list`
--

INSERT INTO `wish_list` (`wishlist_id`, `wishlist_user_id`, `wishlist_food_id`, `wishlist_quantity`, `wishlist_state`, `date_booked`) VALUES
(5, 7, 5, 1, 'booked', '2012-02-18 09:03:44'),
(7, 7, 4, 1, 'booked', '2012-02-18 17:36:54'),
(9, 7, 4, 1, 'booked', '2012-04-15 16:21:51'),
(6, 7, 1, 1, 'booked', '2012-02-18 17:36:54'),
(8, 7, 3, 1, 'booked', '2012-03-05 02:12:48');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
