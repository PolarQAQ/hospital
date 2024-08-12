SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- create database
-- ----------------------------

create database if not exists manager;

-- ----------------------------
-- Table structure for admin
-- ----------------------------

use manager;

-- ----------------------------
-- Table structure for admin
-- ----------------------------

CREATE TABLE if not exists `admin`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色标识',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------

INSERT INTO `admin` VALUES (1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/1697438073596-avatar.png','ADMIN', '13677889922', 'admin@xm.com');

-- ----------------------------
-- Table structure for notice
-- ----------------------------

CREATE TABLE if not exists `notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------

INSERT INTO `notice` VALUES (1, '系统正式运行成功', '今天系统正式运行成功，后续会不断改进', '20234-05-05', 'admin');
INSERT INTO `notice` VALUES (2, '所有功能都已完成，可以正常使用', '所有功能都已完成，可以正常使用', '2024-05-05', 'admin');
INSERT INTO `notice` VALUES (3, '医院领导慰问基层工作人员', '正值流感多发期，医院领导慰问基层工作人员', '2023-05-05', 'admin');

-- ----------------------------
-- FOREIGN_KEY
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for department
-- ----------------------------

CREATE TABLE if not exists `department` (
                              `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                              `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '科室名称',
                              `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '科室描述',
                              PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '科室信息表';

-- ----------------------------
-- Table structure for doctor
-- ----------------------------

CREATE TABLE if not exists `doctor` (
                          `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
                          `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
                          `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
                          `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
                          `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
                          `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
                          `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
                          `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '简介',
                          `price` double(10, 2) DEFAULT NULL COMMENT '挂号费',
                          `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '入职时间',
                          `position` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位',
                          `department_id` int(10) DEFAULT NULL COMMENT '科室ID',
                          PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生信息表';

-- ----------------------------
-- Table structure for user
-- ----------------------------

CREATE TABLE if not exists `user` (
                        `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
                        `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
                        `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
                        `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
                        `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
                        `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
                        `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
                        `account` double(10, 2) DEFAULT '0.00' COMMENT '余额',
                        PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '患者信息表';

-- ----------------------------
-- Table structure for plan
-- ----------------------------


CREATE TABLE if not exists `plan` (
                        `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `doctor_id` int(10) DEFAULT NULL COMMENT '医生ID',
                        `num` int(10) DEFAULT NULL COMMENT '就诊数量',
                        `week` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '周几',
                        PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '排班信息表';

-- ----------------------------
-- Table structure for record
-- ----------------------------

CREATE TABLE if not exists `record` (
                          `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `user_id` int(10) DEFAULT NULL COMMENT '患者ID',
                          `doctor_id` int(10) DEFAULT NULL COMMENT '医生ID',
                          `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '就诊时间',
                          `medical_record` longtext COLLATE utf8mb4_unicode_ci COMMENT '医嘱病历',
                          `inhospital` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否住院',
                          `inhostpital_record` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否住院登记',
                          PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '就诊记录表';

-- ----------------------------
-- Table structure for registration
-- ----------------------------

CREATE TABLE if not exists `registration` (
                                `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                `user_id` int(10) DEFAULT NULL COMMENT '患者ID',
                                `room` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '房号',
                                `price` double(10, 2) DEFAULT NULL COMMENT '费用',
                                `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '缴费状态',
                                `medicine` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否医保',
                                `hos_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '住院状态',
                                PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '住院登记表';

-- ----------------------------
-- Table structure for reserve
-- ----------------------------

CREATE TABLE if not exists `reserve` (
                           `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `user_id` int(10) DEFAULT NULL COMMENT '患者ID',
                           `doctor_id` int(10) DEFAULT NULL COMMENT '医生ID',
                           `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '挂号时间',
                           `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '挂号状态',
                           PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '预约挂号表';
