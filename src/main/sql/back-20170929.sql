-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.18 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 withdraw 的数据库结构
CREATE DATABASE IF NOT EXISTS `withdraw` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `withdraw`;

-- 导出  表 withdraw.area_config 结构
CREATE TABLE IF NOT EXISTS `area_config` (
  `id`             BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `area_id`        VARCHAR(50)         DEFAULT NULL
  COMMENT '地区的标识',
  `download_url`   VARCHAR(50)         DEFAULT NULL
  COMMENT '下载链接',
  `address`        VARCHAR(50)         DEFAULT NULL
  COMMENT 'gmt发送请求的地址',
  `game_server_id` INT(11)             DEFAULT NULL
  COMMENT '游戏game服务器id',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- 正在导出表  withdraw.area_config 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `area_config`
  DISABLE KEYS */;
INSERT INTO `area_config` (`id`, `area_id`, `download_url`, `address`, `game_server_id`) VALUES
  (1, 'guizhou', 'http://home.gzgy.gymjnxa.com/m_window/share.html', 'http://127.0.0.1:13101', 16842753);
/*!40000 ALTER TABLE `area_config`
  ENABLE KEYS */;

-- 导出  表 withdraw.balance 结构
CREATE TABLE IF NOT EXISTS `balance` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `income`      DOUBLE     NOT NULL DEFAULT '0'
  COMMENT '总计收入',
  `money`       DOUBLE     NOT NULL DEFAULT '0'
  COMMENT '账户余额',
  `open_id`     VARCHAR(50)         DEFAULT NULL
  COMMENT '微信openid',
  `union_id`    VARCHAR(50)         DEFAULT NULL
  COMMENT '微信unionid',
  `create_time` TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 正在导出表  withdraw.balance 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `balance`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `balance`
  ENABLE KEYS */;

-- 导出  表 withdraw.balance_operate_log 结构
CREATE TABLE IF NOT EXISTS `balance_operate_log` (
  `id`           BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `serial_no`    VARCHAR(50)         DEFAULT NULL
  COMMENT '操作流水号',
  `balance_id`   BIGINT(20)          DEFAULT NULL
  COMMENT '账户id',
  `operate_type` INT(11)    NOT NULL
  COMMENT '1.充值 2.扣款 3.提现 4.补款',
  `begin_money`  DOUBLE     NOT NULL
  COMMENT '操作前余额',
  `end_money`    DOUBLE     NOT NULL
  COMMENT '操作后余额',
  `operate_time` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '操作时间',
  `union_id`     VARCHAR(50)         DEFAULT NULL
  COMMENT 'union_id',
  `open_id`      VARCHAR(50)         DEFAULT NULL
  COMMENT 'open_id',
  `app_id`       VARCHAR(50)         DEFAULT NULL
  COMMENT '游戏唯一标识',
  `account_id`   VARCHAR(50)         DEFAULT NULL
  COMMENT '玩家id',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 正在导出表  withdraw.balance_operate_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `balance_operate_log`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `balance_operate_log`
  ENABLE KEYS */;

-- 导出  表 withdraw.red_packet 结构
CREATE TABLE IF NOT EXISTS `red_packet` (
  `id`               BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `balance_id`       BIGINT(20)          DEFAULT NULL
  COMMENT '账户id',
  `billing_order_id` VARCHAR(50)         DEFAULT NULL
  COMMENT 'billing订单id',
  `channel_order_id` VARCHAR(50)         DEFAULT NULL
  COMMENT '第三方订单id',
  `rmb`              DOUBLE              DEFAULT NULL
  COMMENT '提现金额',
  `status`           INT(11)             DEFAULT NULL
  COMMENT '订单状态，1:正在发放,2:已发待领,3:发放失败,4红包领取,5:正在退款,6:退款成功',
  `note`             VARCHAR(50)         DEFAULT NULL
  COMMENT '说明',
  `create_time`      TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '订单创建时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 正在导出表  withdraw.red_packet 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `red_packet`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `red_packet`
  ENABLE KEYS */;

-- 导出  表 withdraw.red_packet_status_log 结构
CREATE TABLE IF NOT EXISTS `red_packet_status_log` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `red_packet_id` BIGINT(20)                   DEFAULT NULL,
  `init_status`   INT(11)                      DEFAULT NULL,
  `update_status` INT(11)                      DEFAULT NULL,
  `update_time`   TIMESTAMP           NULL     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- 正在导出表  withdraw.red_packet_status_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `red_packet_status_log`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `red_packet_status_log`
  ENABLE KEYS */;

/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
