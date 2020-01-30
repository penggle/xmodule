/*
Navicat MySQL Data Transfer

Source Server         : mysql-127.0.0.1
Source Server Version : 50617
Source Host           : 127.0.0.1:3306
Source Database       : xmodule

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2020-01-29 16:34:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ss_simple_user
-- ----------------------------
DROP TABLE IF EXISTS `ss_simple_user`;
CREATE TABLE `ss_simple_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nick_name` varchar(100) NOT NULL COMMENT '昵称',
  `role_codes` varchar(512) DEFAULT NULL COMMENT '角色代码列表',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of ss_simple_user
-- ----------------------------
INSERT INTO `ss_simple_user` VALUES ('1', 'pengsan', '$2a$10$PD6MLD6C72CjVz54zN/i8OJZufcfdyQCR3p1QoLI.J3CKh/mNFS4a', '彭三', 'ROLE_USER', '2018-10-19 11:02:20');
