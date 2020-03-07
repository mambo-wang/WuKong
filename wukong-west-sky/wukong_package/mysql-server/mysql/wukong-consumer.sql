-- --------------------------------------------------------
-- 主机:                           192.168.0.228
-- 服务器版本:                        5.7.20 - MySQL Community Server (GPL)
-- 服务器操作系统:                      linux-glibc2.12
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 homeworksys_demo 的数据库结构
CREATE DATABASE IF NOT EXISTS `homeworksys_demo` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `homeworksys_demo`;


-- 导出  表 homeworksys_demo.tbl_attachments 结构
CREATE TABLE IF NOT EXISTS `tbl_attachments` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `NAME` varchar(512) NOT NULL COMMENT '附件名称',
  `TYPE` int(11) unsigned NOT NULL COMMENT '0：学生作业，1：教师布置作业，2：教师模板',
  `RELATED_ID` bigint(20) NOT NULL COMMENT '关联表的ID，有学生作业表，教师作业表和教师模板表三种可能',
  `URL` varchar(1024) NOT NULL COMMENT '资源位置',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  homeworksys_demo.tbl_attachments 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_attachments` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_comment 结构
CREATE TABLE IF NOT EXISTS `tbl_comment` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `TYPE` int(2) NOT NULL DEFAULT '1' COMMENT '1：学生作业，2：老师作业',
  `RELATION_ID` bigint(20) NOT NULL COMMENT '作业ID，有可能是老师作业，也有可能是学生作业',
  `CONTENT` varchar(1024) NOT NULL COMMENT '评论的内容',
  `CREATE_TIME` bigint(20) NOT NULL COMMENT '提交评论的时间',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户主键ID',
  `FLOOR` int(10) NOT NULL COMMENT '楼层',
  `TARGET` varchar(256) DEFAULT NULL COMMENT '回复的对象，楼层加用户姓名，形如“x楼xxx”',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论';

-- 正在导出表  homeworksys_demo.tbl_comment 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_comment` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_homework 结构
CREATE TABLE IF NOT EXISTS `tbl_homework` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '创建教师的ID',
  `GROUP_ID` bigint(20) NOT NULL COMMENT '所属课程组的ID',
  `NAME` varchar(256) NOT NULL COMMENT '作业名称',
  `CONTENT` text COMMENT '作业内容',
  `CREATE_TIME` date DEFAULT NULL COMMENT '作业下发时间',
  `MODIFIED_TIME` date DEFAULT NULL COMMENT '作业修改时间',
  `DEADLINE` date DEFAULT NULL COMMENT '作业提交截止时间',
  `IS_SAVED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否保存为模板，n：不保存，y：保存',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师下发作业表';

-- 正在导出表  homeworksys_demo.tbl_homework 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_homework` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_homework` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_homework_praise 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_praise` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `HOMEWORK_SUB_ID` bigint(20) NOT NULL COMMENT '提交作业的ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '参与互动的用户的ID',
  `CREATE_TIME` date NOT NULL COMMENT '点赞的时间',
  `MODIFIED_TIME` date DEFAULT NULL COMMENT '点赞修改时间',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='点赞';

-- 正在导出表  homeworksys_demo.tbl_homework_praise 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_homework_praise` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_homework_praise` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_homework_submit 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_submit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '提交作业的用户的ID',
  `HOMEWORK_ID` bigint(20) NOT NULL COMMENT '对应老师发布作业的ID',
  `SUBMIT_TIME` date NOT NULL COMMENT '作业最后一次提交时间',
  `ANSWER` text COMMENT '答题栏内容',
  `SCORE` int(11) DEFAULT NULL COMMENT '分数',
  `COMMENT` text COMMENT '教师点评',
  `STARRED` char(10) NOT NULL DEFAULT 'n' COMMENT '是否被展示，n：不被展示，y：被展示',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提交作业表';

-- 正在导出表  homeworksys_demo.tbl_homework_submit 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_homework_submit` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_homework_submit` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_homework_template 结构
CREATE TABLE IF NOT EXISTS `tbl_homework_template` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '创建教师的ID',
  `NAME` varchar(256) NOT NULL COMMENT '作业名称',
  `CONTENT` text COMMENT '作业内容',
  `CREATE_TIME` date NOT NULL COMMENT '模板创建时间',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='作业模板表';

-- 正在导出表  homeworksys_demo.tbl_homework_template 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_homework_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_homework_template` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_lesson_group 结构
CREATE TABLE IF NOT EXISTS `tbl_lesson_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建课程组的用户ID',
  `NAME` varchar(256) NOT NULL DEFAULT '0' COMMENT '课程组名称',
  `MEMBER_LIMIT` int(11) DEFAULT NULL COMMENT '课程组学生人数限制',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程组表';

-- 正在导出表  homeworksys_demo.tbl_lesson_group 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_lesson_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_lesson_group` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_role 结构
CREATE TABLE IF NOT EXISTS `tbl_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) NOT NULL COMMENT '角色名称',
  `SYS` varchar(255) NOT NULL COMMENT '角色所属系统名称',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT 'n表示未删除，y表示已删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- 正在导出表  homeworksys_demo.tbl_role 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `tbl_role` DISABLE KEYS */;
INSERT INTO `tbl_role` (`ID`, `NAME`, `SYS`, `IS_DELETED`) VALUES
	(1, 'ADMIN', 'athena', 'n'),
	(2, 'TEACHER', 'athena', 'n'),
	(3, 'STUDENT', 'athena', 'n');
/*!40000 ALTER TABLE `tbl_role` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_user 结构
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USERNAME` varchar(128) NOT NULL COMMENT '登录名',
  `PASSWORD` varchar(256) NOT NULL COMMENT '密码',
  `NAME` varchar(128) NOT NULL COMMENT '用户姓名',
  `PHOTO` varchar(256) DEFAULT NULL COMMENT '头像',
  `EMAIL_ADDRESS` varchar(128) DEFAULT NULL COMMENT '邮箱地址',
  `IS_DELETED` char(10) NOT NULL DEFAULT 'n' COMMENT '状态：n：正常数据  y：已删除',
  `login_name` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 正在导出表  homeworksys_demo.tbl_user 的数据：~11 rows (大约)
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` (`ID`, `USERNAME`, `PASSWORD`, `NAME`, `PHOTO`, `EMAIL_ADDRESS`, `IS_DELETED`, `login_name`, `user_name`) VALUES
	(1, 'wbao', 'iW9loS09ItY=', '王老师（密码wangbao）', NULL, 'mambo1992@163.com', 'n', NULL, NULL),
	(2, 'xpt', '123456', 'xpt', NULL, 'test@qq.com', 'n', NULL, NULL),
	(3, 'min', '12334445', 'zlm', NULL, 'teted@qq.com', 'y', NULL, NULL),
	(4, 'admin', 'L48dyUHQrDQ=', '全局管理员（密码admin）', NULL, 'admin@h3c.com', 'n', NULL, NULL),
	(18, '201803sd21232131', 'admimﾵ￟]ﾸ', 'ray12321i', NULL, 'sssa123@qq.com', 'n', NULL, NULL),
	(19, '20180321', 'admin', 'paul', NULL, 'sssa@qq.com', 'n', NULL, NULL),
	(20, 'stu', 'iW9loS09ItY=', '学生账户（密码wangbao）', NULL, 'stu@163.com', 'n', NULL, NULL),
	(21, '20180403', 'ￗm￸￧', '吴许灿', NULL, 'asdf@qq.com', 'n', NULL, NULL),
	(22, '2018040302', 'admin', '灿', NULL, 'asdf@qq.com', 'n', NULL, NULL),
	(23, '2018040401', 'admin', 'Chris', NULL, 'asdf@qq.com', 'n', NULL, NULL),
	(26, '2018013', 'L48dyUHQrDQ=', 'cy', NULL, 'cy@qq.com', 'n', NULL, NULL);
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_user_group 结构
CREATE TABLE IF NOT EXISTS `tbl_user_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `GROUP_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '课程组ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生-课程组中间表';

-- 正在导出表  homeworksys_demo.tbl_user_group 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_user_group` ENABLE KEYS */;


-- 导出  表 homeworksys_demo.tbl_user_role 结构
CREATE TABLE IF NOT EXISTS `tbl_user_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户主键',
  `ROLE_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色主键',
  PRIMARY KEY (`ID`),
  KEY `FK6phlytlf1w3h9vutsu019xor5` (`ROLE_ID`),
  KEY `FKggc6wjqokl2vlw89y22a1j2oh` (`USER_ID`),
  CONSTRAINT `FK6phlytlf1w3h9vutsu019xor5` FOREIGN KEY (`ROLE_ID`) REFERENCES `tbl_role` (`ID`),
  CONSTRAINT `FKggc6wjqokl2vlw89y22a1j2oh` FOREIGN KEY (`USER_ID`) REFERENCES `tbl_user` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='用户角色中间表';

-- 正在导出表  homeworksys_demo.tbl_user_role 的数据：~10 rows (大约)
/*!40000 ALTER TABLE `tbl_user_role` DISABLE KEYS */;
INSERT INTO `tbl_user_role` (`ID`, `USER_ID`, `ROLE_ID`) VALUES
	(1, 1, 1),
	(17, 18, 3),
	(18, 19, 3),
	(19, 20, 3),
	(21, 4, 1),
	(22, 1, 2),
	(23, 21, 3),
	(24, 22, 3),
	(25, 23, 3),
	(28, 26, 3);
/*!40000 ALTER TABLE `tbl_user_role` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
