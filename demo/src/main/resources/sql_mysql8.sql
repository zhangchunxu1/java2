/*
MySQL - 8.0.43
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

CREATE DATABASE IF NOT EXISTS `runoob`
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE `runoob`;

CREATE TABLE IF NOT EXISTS `website` (
    `id` INT,
    `name` CHAR(60),
    `url` VARCHAR(765),
    `alexa` INT,
    `country` CHAR(30)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `website` (`id`, `name`, `url`, `alexa`, `country`) VALUES
('1', 'Google', 'https://www.google.cm/', '1', 'USA'),
('2', '', 'https://www.taobao.com/', '13', 'CN'),
('3', '', 'http://www.runoob.com', '5892', ''),
('4', '', 'http://weibo.com/', '20', 'CN'),
('5', 'Facebook', 'https://www.facebook.com/', '3', 'USA');

CREATE TABLE IF NOT EXISTS `websites` (
    `id` INT,
    `name` CHAR(60),
    `url` VARCHAR(765),
    `alexa` INT,
    `country` CHAR(30)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `websites` (`id`, `name`, `url`, `alexa`, `country`) VALUES
('1', 'Google', 'https://www.google.cm/', '1', 'USA'),
('2', '淘宝', 'https://www.taobao.com/', '13', 'CN'),
('3', '菜鸟教程', 'http://www.runoob.com', '5892', ''),
('4', '微博', 'http://weibo.com/', '20', 'CN'),
('5', 'Facebook', 'https://www.facebook.com/', '3', 'USA');

CREATE TABLE USER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    PASSWORD VARCHAR(255) NOT NULL
);
