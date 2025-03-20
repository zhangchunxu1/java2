/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.7.43-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `website` (
	`id` int (11),
	`name` char (60),
	`url` varchar (765),
	`alexa` int (11),
	`country` char (30)
); 
insert into `website` (`id`, `name`, `url`, `alexa`, `country`) values('1','Google','https://www.google.cm/','1','USA');
insert into `website` (`id`, `name`, `url`, `alexa`, `country`) values('2','','https://www.taobao.com/','13','CN');
insert into `website` (`id`, `name`, `url`, `alexa`, `country`) values('3','','http://www.runoob.com','5892','');
insert into `website` (`id`, `name`, `url`, `alexa`, `country`) values('4','','http://weibo.com/','20','CN');
insert into `website` (`id`, `name`, `url`, `alexa`, `country`) values('5','Facebook','https://www.facebook.com/','3','USA');


/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.7.43-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `websites` (
	`id` int (11),
	`name` char (60),
	`url` varchar (765),
	`alexa` int (11),
	`country` char (30)
); 
insert into `websites` (`id`, `name`, `url`, `alexa`, `country`) values('1','Google','https://www.google.cm/','1','USA');
insert into `websites` (`id`, `name`, `url`, `alexa`, `country`) values('2','淘宝','https://www.taobao.com/','13','CN');
insert into `websites` (`id`, `name`, `url`, `alexa`, `country`) values('3','菜鸟教程','http://www.runoob.com','5892','');
insert into `websites` (`id`, `name`, `url`, `alexa`, `country`) values('4','微博','http://weibo.com/','20','CN');
insert into `websites` (`id`, `name`, `url`, `alexa`, `country`) values('5','Facebook','https://www.facebook.com/','3','USA');
