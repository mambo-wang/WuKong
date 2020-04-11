-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.19 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 wukong-consumer 的数据库结构
CREATE DATABASE IF NOT EXISTS `wukong-consumer` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong-consumer`;

-- 导出  表 wukong-consumer_master.tbl_goods 结构
CREATE TABLE IF NOT EXISTS `tbl_goods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标题',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int unsigned DEFAULT '0' COMMENT '-1表示无限',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'n',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在导出表  wukong-consumer.tbl_goods 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `tbl_goods` DISABLE KEYS */;
INSERT INTO `tbl_goods` (`id`, `name`, `title`, `detail`, `price`, `stock`, `image`, `deleted`, `create_time`, `update_time`) VALUES
	(1, '华为mate30', '华为就是棒棒棒', '啥打法上发大水发大厦范德萨发大水发的', 232.32, 83, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-09 20:10:28', '2020-03-09 20:10:31'),
	(2, '华为mate30 pro', '华为就是棒棒棒', '5G手机', 3500.52, 79, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(3, 'OPPO Reno3', 'oppo手机', '5G手机-拍照手机', 3200.58, 949, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(4, '荣耀V30', '华为荣耀', '5G手机-拍照手机-夜景', 3900.00, 996, 'https://img14.360buyimg.com/n0/jfs/t1/107725/8/8164/116514/5e65ae02E0769b9ed/d0823c588572251e.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(5, '小米9', '小米9全面屏手机', '小米手机降价了', 1600.00, 999, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-13 02:38:42', NULL);
/*!40000 ALTER TABLE `tbl_goods` ENABLE KEYS */;

-- 导出  表 wukong-consumer.undo_log 结构
CREATE TABLE IF NOT EXISTS `undo_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  wukong-consumer.undo_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;


-- 导出 wukong-provider 的数据库结构
CREATE DATABASE IF NOT EXISTS `wukong-provider` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong-provider`;

-- 导出  表 wukong-provider_0.tbl_order_0 结构
CREATE TABLE IF NOT EXISTS `tbl_order` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `goods_id` bigint DEFAULT NULL COMMENT '商品ID',
  `address` varchar(255) DEFAULT NULL COMMENT '收获地址',
  `goods_name` varchar(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
  `goods_count` int DEFAULT '0' COMMENT '商品数量',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `status` int DEFAULT NULL COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime DEFAULT NULL COMMENT '订单的创建时间',
  `pay_date` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- 导出  表 wukong-provider_0.tbl_user 结构
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `id` bigint NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int NOT NULL DEFAULT '0',
  `balance` decimal(10,2) unsigned DEFAULT '0.00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在导出表  wukong-provider.tbl_user 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` (`id`, `name`, `username`, `password`, `address`, `phone_number`, `email`, `score`, `balance`) VALUES
	(445013752736120832, '范冰冰', 'ououou', '123456', '杭州', '18668233261', 'mambo1991@163.com', 3200, NULL),
	(445013753377849344, 'lisa', 'lisa', '123456', '天津', '18668233261', 'mambo1991@163.com', 0, NULL);
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;

-- 导出  表 wukong-provider_0.undo_log 结构
CREATE TABLE IF NOT EXISTS `undo_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  wukong-provider_0.undo_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
