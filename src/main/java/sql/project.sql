#项目的数据库
CREATE DATABASE `boot_shop`;
USE boot_shop;
#创建表
CREATE TABLE `product_shop`(
                               `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               `name` VARCHAR(32) NOT NULL UNIQUE,
                               `product_Id` CHAR(60)  NOT NULL,
                               `Inventory` INT NOT NULL DEFAULT 0,
                               `sales` INT NOT NULL DEFAULT 0,
                               `parts` VARCHAR(32),
                               `production_Time` DATETIME,
                               `init_Time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARSET=utf8;

#建立通用索引
ALTER TABLE `product_shop` ADD INDEX allIndex (`id`,`name`,product_id,Inventory,sales,parts,production_Time,init_Time);

# 添加唯一索引
ALTER TABLE `product_shop` ADD UNIQUE (`name`);

#添加测试数据
INSERT INTO  `product_shop` VALUES(NULL,'test_', CONCAT(UUID(),CURDATE()),212,276,'test_part',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW41', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW42', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW43', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW40', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW45', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW06', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW47', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
INSERT INTO  `product_shop` VALUES(NULL,'R9Q9EW48', CONCAT(UUID(),CURDATE()),212,276,'测试部门',NOW(),NULL);
# 查询缓冲池大小
 -- SELECT @@innodb_buffer_pool_size;
 -- show status like 'Innodb_buffer_pool_read_%'
#创建用户表
USE boot_shop;
CREATE TABLE `user` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `username` VARCHAR(32) NOT NULL UNIQUE,
                        `password` CHAR(32) NOT NULL,
                        `email` VARCHAR(32) NOT NULL UNIQUE,
                        `registerTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                        `limit` TINYINT(1) NOT NULL DEFAULT 1,
                        `context` VARCHAR(32) NOT NULL DEFAULT 'simple',
                        PRIMARY KEY (`id`)
) CHARSET=utf8;

#测试查询表
#SELECT * FROM `user`;
#SELECT `id` FROM `user` WHERE `username` = '152435' OR `email` = '2471831066@qq.com';

#注册的sql语句,添加测试
INSERT INTO `user`(`id`,`username`,`password`,`email`)
 VALUES(
 NULL,
 'kivy0000',
 MD5('123456789'),
 '66661qweq16@qq.com'
 );

-- 注册修正
#修改用户名和邮箱为唯一值,
#修改密码为md5加密,设置字段为char32
#添加权限识别,类型为tinyint


-- 重置自增id ，注意，重置的id要大于最大id，否则仍会从最大id向下排序
#delete from `user` where id >1
#alter table `user` auto_increment = 4;
#select max(id) from `user`;
#update `user` set id = 3 where `id` = 41;
#delete from `user` where id > 3


-- 登录
#登录验证sql语句
#SELECT `id` FROM `user` WHERE `username` = 'lingua' -- 先检查用户是否注册过
#SELECT `id` FROM `user` WHERE `username` = 'lingua' or `password` = MD5('666666');  -- 再检查账号密码是否不匹配
#SELECT `id` FROM `user` WHERE `username` = 'lingua' AND `password` = MD5('666666');  -- 再检查账号密码是否正确

-- 注册验证sql语句
#SELECT `id` FROM `user` WHERE `username` = 'lingua' -- 先检查用户是否注册过
#INSERT INTO `user` (`username`,`password`,`email`) VALUES (#{username}, MD5(#{password}), #{email} )  -- 实际注册

-- 重置密码验证sql语句
#SELECT `id` FROM `user` WHERE `username` = 'lingua' -- 先检查用户是否注册过
#UPDATE `user` SET `password` = MD5('777') WHERE `username` = '1' AND `email` = '2471831066@qqq.com' -- 再重置密码





