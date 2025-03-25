/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.7.43-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
--创建runoob 数据库
CREATE DATABASE IF NOT EXISTS runoob 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;


-- 使用 runoob 数据库
USE runoob;

-- 创建 website 表
CREATE TABLE IF NOT EXISTS `website` (
    `id` INT(11),
    `name` CHAR(60),
    `url` VARCHAR(765),
    `alexa` INT(11),
    `country` CHAR(30)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 向 website 表插入数据
INSERT INTO `website` (`id`, `name`, `url`, `alexa`, `country`) VALUES
('1', 'Google', 'https://www.google.cm/', '1', 'USA'),
('2', '', 'https://www.taobao.com/', '13', 'CN'),
('3', '', 'http://www.runoob.com', '5892', ''),
('4', '', 'http://weibo.com/', '20', 'CN'),
('5', 'Facebook', 'https://www.facebook.com/', '3', 'USA');

-- 创建 websites 表
CREATE TABLE IF NOT EXISTS `websites` (
    `id` INT(11),
    `name` CHAR(60),
    `url` VARCHAR(765),
    `alexa` INT(11),
    `country` CHAR(30)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 向 websites 表插入数据
INSERT INTO `websites` (`id`, `name`, `url`, `alexa`, `country`) VALUES
('1', 'Google', 'https://www.google.cm/', '1', 'USA'),
('2', '淘宝', 'https://www.taobao.com/', '13', 'CN'),
('3', '菜鸟教程', 'http://www.runoob.com', '5892', ''),
('4', '微博', 'http://weibo.com/', '20', 'CN'),
('5', 'Facebook', 'https://www.facebook.com/', '3', 'USA');
