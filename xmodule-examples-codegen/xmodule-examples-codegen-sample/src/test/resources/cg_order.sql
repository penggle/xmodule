/*
Navicat MySQL Data Transfer

Source Server         : mysql-127.0.0.1
Source Server Version : 80020
Source Host           : 127.0.0.1:3306
Source Database       : xmodule

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2020-08-04 20:49:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cg_order
-- ----------------------------
DROP TABLE IF EXISTS `cg_order`;
CREATE TABLE `cg_order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总价',
  `freight_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '运费',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `payment_type` tinyint NOT NULL DEFAULT '0' COMMENT '支付方式：0-在线支付，1-货到付款',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '订单状态：0-待付款，1-待发货，2-待收货',
  `remark` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单备注',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ----------------------------
-- Table structure for cg_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `cg_order_detail`;
CREATE TABLE `cg_order_detail` (
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `product_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品详情页URL',
  `unit_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品单价',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '购买数量',
  `freight_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '运费',
  `sub_total_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '小计',
  PRIMARY KEY (`order_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单字表';
