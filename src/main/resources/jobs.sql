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

 Date: 06/06/2022 14:38:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
INSERT INTO `job_goods` VALUES ('1', '荒料石场桥');
INSERT INTO `job_goods` VALUES ('10', '其他');
INSERT INTO `job_goods` VALUES ('2', '荒料石吊车');
INSERT INTO `job_goods` VALUES ('3', '荒料石门机');
INSERT INTO `job_goods` VALUES ('4', '钢铁');
INSERT INTO `job_goods` VALUES ('5', '商品车');
INSERT INTO `job_goods` VALUES ('6', '纸浆板');
INSERT INTO `job_goods` VALUES ('7', '铜精矿');
INSERT INTO `job_goods` VALUES ('8', '中矿');
INSERT INTO `job_goods` VALUES ('9', '锆英砂');

-- ----------------------------
-- Table structure for job_order
-- ----------------------------
DROP TABLE IF EXISTS `job_order`;
CREATE TABLE `job_order`  (
  `ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `driver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `car_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `telephone` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `goods_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_num` int(0) UNSIGNED NULL DEFAULT NULL,
  `time` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '日期+班别',
  `type_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `area_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `goods_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `weight` int(0) UNSIGNED NULL DEFAULT NULL,
  `date` date NOT NULL DEFAULT '0000-00-00' COMMENT '日期',
  `duty` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '班别',
  `vessel_voyage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
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
INSERT INTO `job_order` VALUES ('165e216cfd8a41218440a332b9547077', '安吉物流', NULL, '13333333333', NULL, 3, '2022-06-05白班', '1', '7', '3', 3, '2022-06-05', '白班', NULL);
INSERT INTO `job_order` VALUES ('4d118b29782944729d509b7b7235b6f4', '安吉物流', NULL, '13333333333', NULL, 1, '2022-06-06夜班', '2', '7', '3', NULL, '2022-06-06', '夜班', NULL);
INSERT INTO `job_order` VALUES ('74e7f36fcb6246d3bfb989bfb11f8762', '安吉物流', NULL, '13333333333', NULL, 1, '2022-06-06夜班', '1', NULL, '1', NULL, '2022-06-06', '夜班', '卓越王牌0146A');
INSERT INTO `job_order` VALUES ('81d0415351d7458ea12b17a49a736aa9', '安吉物流', NULL, '0592-2222222', NULL, 1, '2022-06-03夜班', '1', '1', '1', NULL, '2022-06-03', '夜班', NULL);
INSERT INTO `job_order` VALUES ('9ab87d408258416093e52f7efb9ebe38', '安吉物流', NULL, '13333333333', NULL, 2, '2022-06-04白班', '2', '6', '2', 2, '2022-06-04', '白班', NULL);
INSERT INTO `job_order` VALUES ('f0677ed36ae14004adf7b68b5ad95b5f', '安吉物流', NULL, '13333333333', NULL, 1, '2022-06-06夜班', '1', NULL, '1', NULL, '2022-06-06', '夜班', '卓越王牌0146A');

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

SET FOREIGN_KEY_CHECKS = 1;
