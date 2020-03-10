######################################################
################wukong updates######################
######################################################
delimiter //
use wukong //
drop procedure if exists upgradeWuKong //
create procedure upgradeWuKong()
begin
CREATE TABLE IF NOT EXISTS `tbl_cart` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `CLASS_NO` varchar(50) NOT NULL COMMENT '班级代码',
  `CLASS_NAME` varchar(50) NOT NULL COMMENT '班级名称',
  `DATA_SOURCE` INT(11) NOT NULL DEFAULT '0' COMMENT '数据来源',
  `DELETED` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0：正常数据  1：已删除',
  PRIMARY KEY (`ID`)
) COMMENT='班级';

end;
//
call upgradeWuKong() //
drop procedure if exists upgradeWuKong //
DELIMITER ;

