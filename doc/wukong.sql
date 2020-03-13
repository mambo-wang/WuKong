-- --------------------------------------------------------
-- 主机:                           121.43.191.104
-- 服务器版本:                        8.0.16 - MySQL Community Server - GPL
-- 服务器OS:                        Linux
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for wukong-consumer_master
CREATE DATABASE IF NOT EXISTS `wukong-consumer_master` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong-consumer_master`;

-- Dumping structure for table wukong-consumer_master.tbl_goods
CREATE TABLE IF NOT EXISTS `tbl_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标题',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int(10) unsigned DEFAULT '0' COMMENT '-1表示无限',
  `image` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'n',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-consumer_master.tbl_goods: ~5 rows (大约)
/*!40000 ALTER TABLE `tbl_goods` DISABLE KEYS */;
INSERT INTO `tbl_goods` (`id`, `name`, `title`, `detail`, `price`, `stock`, `image`, `deleted`, `create_time`, `update_time`) VALUES
	(1, '华为mate30', '华为就是棒棒棒', '啥打法上发大水发大厦范德萨发大水发的', 232.32, 83, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-09 20:10:28', '2020-03-09 20:10:31'),
	(2, '华为mate30 pro', '华为就是棒棒棒', '5G手机', 3500.52, 84, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(3, 'OPPO Reno3', 'oppo手机', '5G手机-拍照手机', 3200.58, 987, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(4, '荣耀V30', '华为荣耀', '5G手机-拍照手机-夜景', 3900.00, 996, 'https://img14.360buyimg.com/n0/jfs/t1/107725/8/8164/116514/5e65ae02E0769b9ed/d0823c588572251e.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(5, '小米9', '小米9全面屏手机', '小米手机降价了', 1600.00, 999, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-13 02:38:42', NULL);
/*!40000 ALTER TABLE `tbl_goods` ENABLE KEYS */;


-- Dumping database structure for wukong-consumer_slave
CREATE DATABASE IF NOT EXISTS `wukong-consumer_slave` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong-consumer_slave`;

-- Dumping structure for table wukong-consumer_slave.tbl_goods
CREATE TABLE IF NOT EXISTS `tbl_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标题',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int(10) unsigned DEFAULT '0' COMMENT '-1表示无限',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'n',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-consumer_slave.tbl_goods: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_goods` DISABLE KEYS */;
INSERT INTO `tbl_goods` (`id`, `name`, `title`, `detail`, `price`, `stock`, `image`, `deleted`, `create_time`, `update_time`) VALUES
	(1, '华为mate30', '华为就是棒棒棒', '啥打法上发大水发大厦范德萨发大水发的', 232.32, 83, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-09 20:10:28', '2020-03-09 20:10:31'),
	(2, '华为mate30 pro', '华为就是棒棒棒', '5G手机', 3500.52, 84, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(3, 'OPPO Reno3', 'oppo手机', '5G手机-拍照手机', 3200.58, 992, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(4, '荣耀V30', '华为荣耀', '5G手机-拍照手机-夜景', 3900.00, 996, 'https://img14.360buyimg.com/n0/jfs/t1/107725/8/8164/116514/5e65ae02E0769b9ed/d0823c588572251e.jpg', 'n', '2020-03-11 20:10:28', '2020-11-09 20:10:31'),
	(5, '小米9', '小米9全面屏手机', '小米手机降价了', 1600.00, 1000, 'https://img14.360buyimg.com/n0/jfs/t1/98106/1/14526/361939/5e6740faE112df285/c9d74594b87462ec.jpg', 'n', '2020-03-13 02:38:42', NULL);
/*!40000 ALTER TABLE `tbl_goods` ENABLE KEYS */;


-- Dumping database structure for wukong-provider_0
CREATE DATABASE IF NOT EXISTS `wukong-provider_0` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong-provider_0`;

-- Dumping structure for table wukong-provider_0.tbl_order_0
CREATE TABLE IF NOT EXISTS `tbl_order_0` (
  `id` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table wukong-provider_0.tbl_order_0: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_order_0` DISABLE KEYS */;
INSERT INTO `tbl_order_0` (`id`, `user_id`, `goods_id`, `address`, `goods_name`, `goods_count`, `goods_price`, `status`, `create_date`, `pay_date`) VALUES
	(445022502440140800, 445013753029722113, 1, '北京', '华为mate30', 1, 232.32, 0, '2020-03-12 16:40:33', '2020-03-12 16:40:33'),
	(445022719898025984, 445013752165695489, 3, '德州', 'OPPO Reno3', 1, 3200.58, 0, '2020-03-12 16:41:25', '2020-03-12 16:41:25'),
	(445177740505645056, 445013753029722113, 5, '北京', '小米9', 1, 1600.00, 0, '2020-03-13 02:57:24', '2020-03-13 02:57:24');
/*!40000 ALTER TABLE `tbl_order_0` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_0.tbl_order_1
CREATE TABLE IF NOT EXISTS `tbl_order_1` (
  `id` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table wukong-provider_0.tbl_order_1: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_order_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_order_1` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_0.tbl_order_2
CREATE TABLE IF NOT EXISTS `tbl_order_2` (
  `id` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table wukong-provider_0.tbl_order_2: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_order_2` DISABLE KEYS */;
INSERT INTO `tbl_order_2` (`id`, `user_id`, `goods_id`, `address`, `goods_name`, `goods_count`, `goods_price`, `status`, `create_date`, `pay_date`) VALUES
	(445022676445036544, 445013752165695489, 1, '德州', '华为mate30', 1, 232.32, 0, '2020-03-12 16:41:14', '2020-03-12 16:41:14'),
	(445177689389662208, 445013753029722113, 3, '北京', 'OPPO Reno3', 1, 3200.58, 0, '2020-03-13 02:57:12', '2020-03-13 02:57:12');
/*!40000 ALTER TABLE `tbl_order_2` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_0.tbl_user_0
CREATE TABLE IF NOT EXISTS `tbl_user_0` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-provider_0.tbl_user_0: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user_0` DISABLE KEYS */;
INSERT INTO `tbl_user_0` (`id`, `name`, `username`, `password`, `address`, `phone_number`, `email`, `score`) VALUES
	(445013752736120832, '范冰冰', 'ououou', '123456', '杭州', '18668233261', 'mambo1991@163.com', 0),
	(445013753377849344, 'lisa', 'lisa', '123456', '天津', '18668233261', 'mambo1991@163.com', 0);
/*!40000 ALTER TABLE `tbl_user_0` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_0.tbl_user_1
CREATE TABLE IF NOT EXISTS `tbl_user_1` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-provider_0.tbl_user_1: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user_1` DISABLE KEYS */;
INSERT INTO `tbl_user_1` (`id`, `name`, `username`, `password`, `address`, `phone_number`, `email`, `score`) VALUES
	(445013754011189248, '小妮', 'xiaoni', '123456', '宁波', '18668233261', 'mambo1991@163.com', 0);
/*!40000 ALTER TABLE `tbl_user_1` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_0.tbl_user_2
CREATE TABLE IF NOT EXISTS `tbl_user_2` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-provider_0.tbl_user_2: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user_2` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_user_2` ENABLE KEYS */;


-- Dumping database structure for wukong-provider_1
CREATE DATABASE IF NOT EXISTS `wukong-provider_1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong-provider_1`;

-- Dumping structure for table wukong-provider_1.tbl_order_0
CREATE TABLE IF NOT EXISTS `tbl_order_0` (
  `id` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table wukong-provider_1.tbl_order_0: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_order_0` DISABLE KEYS */;
INSERT INTO `tbl_order_0` (`id`, `user_id`, `goods_id`, `address`, `goods_name`, `goods_count`, `goods_price`, `status`, `create_date`, `pay_date`) VALUES
	(445175948443123713, 445013753029722113, 3, '北京', 'OPPO Reno3', 1, 3200.58, 0, '2020-03-13 02:50:17', '2020-03-13 02:50:17');
/*!40000 ALTER TABLE `tbl_order_0` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_1.tbl_order_1
CREATE TABLE IF NOT EXISTS `tbl_order_1` (
  `id` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table wukong-provider_1.tbl_order_1: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_order_1` DISABLE KEYS */;
INSERT INTO `tbl_order_1` (`id`, `user_id`, `goods_id`, `address`, `goods_name`, `goods_count`, `goods_price`, `status`, `create_date`, `pay_date`) VALUES
	(445177713980866561, 445013753029722113, 3, '北京', 'OPPO Reno3', 1, 3200.58, 0, '2020-03-13 02:57:18', '2020-03-13 02:57:18');
/*!40000 ALTER TABLE `tbl_order_1` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_1.tbl_order_2
CREATE TABLE IF NOT EXISTS `tbl_order_2` (
  `id` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table wukong-provider_1.tbl_order_2: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_order_2` DISABLE KEYS */;
INSERT INTO `tbl_order_2` (`id`, `user_id`, `goods_id`, `address`, `goods_name`, `goods_count`, `goods_price`, `status`, `create_date`, `pay_date`) VALUES
	(445022423725637633, 445013753029722113, 1, '北京', '华为mate30', 1, 232.32, 0, '2020-03-12 16:40:14', '2020-03-12 16:40:14'),
	(445022576054370305, 445013753029722113, 1, '北京', '华为mate30', 1, 232.32, 0, '2020-03-12 16:40:50', '2020-03-12 16:40:50'),
	(445022696699330561, 445013752165695489, 2, '德州', '华为mate30 pro', 1, 3500.52, 0, '2020-03-12 16:41:19', '2020-03-12 16:41:19');
/*!40000 ALTER TABLE `tbl_order_2` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_1.tbl_user_0
CREATE TABLE IF NOT EXISTS `tbl_user_0` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-provider_1.tbl_user_0: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user_0` DISABLE KEYS */;
INSERT INTO `tbl_user_0` (`id`, `name`, `username`, `password`, `address`, `phone_number`, `email`, `score`) VALUES
	(445013752165695489, '李师师', 'lishishi', '123456', '德州', '18668233261', 'mambo1991@163.com', 6932);
/*!40000 ALTER TABLE `tbl_user_0` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_1.tbl_user_1
CREATE TABLE IF NOT EXISTS `tbl_user_1` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-provider_1.tbl_user_1: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user_1` DISABLE KEYS */;
INSERT INTO `tbl_user_1` (`id`, `name`, `username`, `password`, `address`, `phone_number`, `email`, `score`) VALUES
	(445013753667256321, '刘亦菲', 'liuyifei', '123456', '哈尔滨', '18668233261', 'mambo1991@163.com', 0),
	(445013754346733569, '哈哈', 'hahaha', '123456', '济南', '18668233261', 'mambo1991@163.com', 0);
/*!40000 ALTER TABLE `tbl_user_1` ENABLE KEYS */;

-- Dumping structure for table wukong-provider_1.tbl_user_2
CREATE TABLE IF NOT EXISTS `tbl_user_2` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `score` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Dumping data for table wukong-provider_1.tbl_user_2: ~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user_2` DISABLE KEYS */;
INSERT INTO `tbl_user_2` (`id`, `name`, `username`, `password`, `address`, `phone_number`, `email`, `score`) VALUES
	(445013753029722113, '杨幂', 'yeyeye', '123456', '北京', '18668233261', 'mambo1991@163.com', 11896);
/*!40000 ALTER TABLE `tbl_user_2` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
