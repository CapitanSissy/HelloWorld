/*
 Navicat Premium Data Transfer

 Source Server         : HelloWorld - Core
 Source Server Type    : MySQL
 Source Server Version : 100509
 Source Host           : localhost:3306
 Source Schema         : helloworld

 Target Server Type    : MySQL
 Target Server Version : 100509
 File Encoding         : 65001

 Date: 14/12/2021 02:34:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_connection_parameters
-- ----------------------------
DROP TABLE IF EXISTS `tbl_connection_parameters`;
CREATE TABLE `tbl_connection_parameters`  (
  `rowId` int(11) NOT NULL COMMENT 'آیدی یکتای جدول',
  `serverName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'آی‌پی سرور پایگاه داده',
  `portNumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'شماره پورت پایگاه داده',
  `databaseName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'نام پایگاه داده',
  `useSSL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'آیا از اس‌اس‌ال استفاده شود',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'نام کاربری اتصال به پایگاه داده',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'گذرواژه عبور اتصال به پایگاه داده',
  PRIMARY KEY (`rowId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_cp
-- ----------------------------
DROP TABLE IF EXISTS `tbl_cp`;
CREATE TABLE `tbl_cp`  (
  `TBL_CP_C` int(11) NOT NULL COMMENT 'آیدی یکتای جدول',
  `TBL_CP_CC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'آی‌پی سرور پایگاه داده',
  `TBL_CP_CCC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'شماره پورت پایگاه داده',
  `TBL_CP_CCCC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'نام پایگاه داده',
  `TBL_CP_CCCCC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'آیا از اس‌اس‌ال استفاده شود',
  `TBL_CP_CCCCCC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'نام کاربری اتصال به پایگاه داده',
  `TBL_CP_CCCCCCC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'گذرواژه عبور اتصال به پایگاه داده',
  PRIMARY KEY (`TBL_CP_C`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
