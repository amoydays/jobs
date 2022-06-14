/*
 Navicat Premium Data Transfer

 Source Server         : mysql3307
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3307
 Source Schema         : jobs

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 12/06/2022 13:15:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user`  (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `passwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES ('xdmt', 'yyds');

-- ----------------------------
-- Table structure for job_area
-- ----------------------------
DROP TABLE IF EXISTS `job_area`;
CREATE TABLE `job_area`  (
  `ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of job_area
-- ----------------------------
INSERT INTO `job_area` VALUES ('1', '场桥');
INSERT INTO `job_area` VALUES ('5', '吊车区');
INSERT INTO `job_area` VALUES ('6', '商品车区');
INSERT INTO `job_area` VALUES ('7', '卷钢区');

-- ----------------------------
-- Table structure for job_goods
-- ----------------------------
DROP TABLE IF EXISTS `job_goods`;
CREATE TABLE `job_goods`  (
  `ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of job_goods
-- ----------------------------
INSERT INTO `job_goods` VALUES ('A', '荒料石场桥');
INSERT INTO `job_goods` VALUES ('B', '荒料石吊车');
INSERT INTO `job_goods` VALUES ('C', '荒料石前沿');
INSERT INTO `job_goods` VALUES ('D', '铜精矿');
INSERT INTO `job_goods` VALUES ('E', '铁精矿');
INSERT INTO `job_goods` VALUES ('F', '商品车');
INSERT INTO `job_goods` VALUES ('G', '工程车');
INSERT INTO `job_goods` VALUES ('H', '中矿');
INSERT INTO `job_goods` VALUES ('I', '干浆');
INSERT INTO `job_goods` VALUES ('J', '卷钢');
INSERT INTO `job_goods` VALUES ('K', '石油焦');
INSERT INTO `job_goods` VALUES ('L', '锆英砂');
INSERT INTO `job_goods` VALUES ('M', '线材');
INSERT INTO `job_goods` VALUES ('N', '化肥');
INSERT INTO `job_goods` VALUES ('O', '散小麦');
INSERT INTO `job_goods` VALUES ('P', '设备');
INSERT INTO `job_goods` VALUES ('Q', '木箱');
INSERT INTO `job_goods` VALUES ('R', '其它');

-- ----------------------------
-- Table structure for job_notice
-- ----------------------------
DROP TABLE IF EXISTS `job_notice`;
CREATE TABLE `job_notice`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_notice
-- ----------------------------
INSERT INTO `job_notice` VALUES (24, '15点前预登记今天夜班、明天白班；15点后预登记明天夜班、后天白班');

-- ----------------------------
-- Table structure for job_order
-- ----------------------------
DROP TABLE IF EXISTS `job_order`;
CREATE TABLE `job_order`  (
  `ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `driver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '作业单位，司机或公司',
  `car_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `telephone` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机',
  `goods_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '货物信息',
  `goods_num` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '作业件数',
  `time` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '日期+班别',
  `type_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '作业类型',
  `area_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '作业区域',
  `goods_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '作业货物',
  `weight` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '作业重量',
  `date` date NOT NULL DEFAULT '0000-00-00' COMMENT '日期',
  `duty` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '班别',
  `vessel_voyage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '船名航次',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `FK_jobs_order_type`(`type_id`) USING BTREE,
  INDEX `FK_jobs_order_area`(`area_id`) USING BTREE,
  INDEX `FK_jobs_order_goods`(`goods_id`) USING BTREE,
  CONSTRAINT `FK_jobs_order_area` FOREIGN KEY (`area_id`) REFERENCES `job_area` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_jobs_order_goods` FOREIGN KEY (`goods_id`) REFERENCES `job_goods` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_jobs_order_type` FOREIGN KEY (`type_id`) REFERENCES `job_type` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'InnoDB free: 8192 kB; (`area_id`) REFER `jobs/job_area`(`ID`); (`goods_id`) REFE' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of job_order
-- ----------------------------

-- ----------------------------
-- Table structure for job_type
-- ----------------------------
DROP TABLE IF EXISTS `job_type`;
CREATE TABLE `job_type`  (
  `ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of job_type
-- ----------------------------
INSERT INTO `job_type` VALUES ('1', '提货');
INSERT INTO `job_type` VALUES ('2', '集港');

-- ----------------------------
-- Table structure for job_update_record
-- ----------------------------
DROP TABLE IF EXISTS `job_update_record`;
CREATE TABLE `job_update_record`  (
  `ID` int(0) NOT NULL AUTO_INCREMENT,
  `driver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `time` datetime(0) NOT NULL,
  `column_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ori_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `now_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_update_record
-- ----------------------------

-- ----------------------------
-- Table structure for job_update_string
-- ----------------------------
DROP TABLE IF EXISTS `job_update_string`;
CREATE TABLE `job_update_string`  (
  `ID` int(0) NOT NULL AUTO_INCREMENT,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_read` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_update_string
-- ----------------------------

-- ----------------------------
-- Table structure for job_warn
-- ----------------------------
DROP TABLE IF EXISTS `job_warn`;
CREATE TABLE `job_warn`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `driver` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `time` datetime(0) NOT NULL,
  `is_read` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_warn
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
