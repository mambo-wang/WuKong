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


-- 导出 wukong_consumer 的数据库结构
CREATE DATABASE IF NOT EXISTS `wukong_consumer` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `wukong_consumer`;

-- 导出  表 wukong_consumer.tbl_goods 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 正在导出表  wukong_consumer.tbl_goods 的数据：~0 rows (大约)
DELETE FROM `tbl_goods`;
/*!40000 ALTER TABLE `tbl_goods` DISABLE KEYS */;
INSERT INTO `tbl_goods` (`id`, `name`, `title`, `detail`, `price`, `stock`, `image`, `deleted`, `create_time`, `update_time`) VALUES
	(1, '华为mate30', '华为就是棒棒棒', '啥打法上发大水发大厦范德萨发大水发的', 232.32, 96, NULL, 'n', '2020-03-09 20:10:28', '2020-03-09 20:10:31');
/*!40000 ALTER TABLE `tbl_goods` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
