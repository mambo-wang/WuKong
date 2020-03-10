-- --------------------------------------------------------
-- 主机:                           121.43.191.104
-- 服务器版本:                        8.0.16 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 wukong_provider 的数据库结构
CREATE DATABASE IF NOT EXISTS `wukong_provider` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong_provider`;

-- 导出  表 wukong_provider.tbl_order 结构
CREATE TABLE IF NOT EXISTS `tbl_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `address` varchar(255) DEFAULT NULL COMMENT '收获地址',
  `goods_name` varchar(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
  `goods_count` int(11) DEFAULT '0' COMMENT '商品数量',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `status` int(4) DEFAULT NULL COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime DEFAULT NULL COMMENT '订单的创建时间',
  `pay_date` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1590 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  wukong_provider.tbl_order 的数据：~0 rows (大约)
DELETE FROM `tbl_order`;
/*!40000 ALTER TABLE `tbl_order` DISABLE KEYS */;
INSERT INTO `tbl_order` (`id`, `user_id`, `goods_id`, `address`, `goods_name`, `goods_count`, `goods_price`, `status`, `create_date`, `pay_date`) VALUES
	(1565, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:42:13', NULL),
	(1566, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:42:14', NULL),
	(1567, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:47:01', NULL),
	(1568, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:47:01', NULL),
	(1569, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:47:01', NULL),
	(1570, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:48:48', NULL),
	(1571, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:48:48', NULL),
	(1572, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:48:48', NULL),
	(1573, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:51:43', NULL),
	(1574, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:51:43', NULL),
	(1575, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:51:43', NULL),
	(1576, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:52:07', NULL),
	(1577, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:53:27', NULL),
	(1578, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:53:28', NULL),
	(1579, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:54:44', NULL),
	(1580, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:58:21', NULL),
	(1581, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:58:53', NULL),
	(1582, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 15:59:04', NULL),
	(1583, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 16:03:23', NULL),
	(1584, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 16:03:29', NULL),
	(1585, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 16:03:55', NULL),
	(1586, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 16:15:16', NULL),
	(1587, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 16:17:20', NULL),
	(1588, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 16:39:05', NULL),
	(1589, 8, 1, '111', '华为mate30', 1, 232.32, 0, '2020-03-09 16:40:03', NULL);
/*!40000 ALTER TABLE `tbl_order` ENABLE KEYS */;

-- 导出  表 wukong_provider.tbl_user 结构
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在导出表  wukong_provider.tbl_user 的数据：~5 rows (大约)
DELETE FROM `tbl_user`;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` (`id`, `name`, `username`, `password`, `address`, `phone_number`, `email`, `score`) VALUES
	(1, '喂喂喂', 'hahaha', '123456', NULL, NULL, NULL, 0),
	(5, '哈哈哈', 'houhouhou', '123456', NULL, NULL, NULL, 0),
	(6, '哈哈哈', 'yeyeye', '123456', NULL, NULL, NULL, 0),
	(8, '哈哈哈', 'ououou', '123456', '111', '18668233261', 'mambo1991@163.com', 3016),
	(9, '狗屁', 'booshit', 'mima', NULL, NULL, NULL, 0);
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
