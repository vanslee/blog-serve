/*
 Navicat Premium Data Transfer

 Source Server         : 101.34.69.172
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : 101.34.69.172:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 17/07/2023 20:59:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for archives
-- ----------------------------
DROP TABLE IF EXISTS `archives`;
CREATE TABLE `archives`  (
                             `id` int(0) NOT NULL AUTO_INCREMENT,
                             `archiveName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
                            `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
                            `original_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                            `article_title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                            `publish_date` bigint(0) NOT NULL,
                            `update_date` bigint(0) UNSIGNED NOT NULL DEFAULT 0,
                            `img_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '',
                            `views` int(0) UNSIGNED NULL DEFAULT 0,
                            `likes` int(0) UNSIGNED NULL DEFAULT 0,
                            `comments` int(0) UNSIGNED NULL DEFAULT 0,
                            `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
                            `user_id` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
                            `collects` int(0) UNSIGNED NULL DEFAULT 0,
                            `md_url` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'markdown地址',
                            `article_abstract` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文章摘要',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category`  (
                                     `article_id` bigint(0) UNSIGNED NOT NULL,
                                     `category_id` int(0) UNSIGNED NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_collects
-- ----------------------------
DROP TABLE IF EXISTS `article_collects`;
CREATE TABLE `article_collects`  (
                                     `id` int(0) NOT NULL AUTO_INCREMENT,
                                     `articleId` int(0) UNSIGNED NULL DEFAULT NULL,
                                     `userId` int(0) UNSIGNED NULL DEFAULT NULL,
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment`  (
                                    `article_id` bigint(0) UNSIGNED NOT NULL COMMENT '评论的文章id',
                                    `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论id',
                                    `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论内容',
                                    `create_time` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '评论时间',
                                    `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
                                    `user_id` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '用户id',
                                    `likes` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '评论点赞数',
                                    `root_comment_id` bigint(0) UNSIGNED NULL DEFAULT 0 COMMENT '根评论ID',
                                    `reply_comment_id` bigint(0) UNSIGNED NULL DEFAULT 0 COMMENT '回复评论id',
                                    `update_time` int(0) NULL DEFAULT NULL COMMENT '评论修改时间',
                                    `is_top` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶',
                                    `user_nick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论人昵称',
                                    `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论人头像',
                                    `location` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论人IP属地',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_details
-- ----------------------------
DROP TABLE IF EXISTS `article_details`;
CREATE TABLE `article_details`  (
                                    `id` bigint(0) NOT NULL,
                                    `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_likes_record
-- ----------------------------
DROP TABLE IF EXISTS `article_likes_record`;
CREATE TABLE `article_likes_record`  (
                                         `id` int(0) NOT NULL AUTO_INCREMENT,
                                         `articleId` bigint(0) NOT NULL,
                                         `likerId` int(0) NOT NULL,
                                         `likeDate` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                                         `isRead` tinyint(1) NOT NULL,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
                                `article_id` bigint(0) UNSIGNED NOT NULL,
                                `tag_id` int(0) UNSIGNED NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
                               `id` int(0) NOT NULL AUTO_INCREMENT,
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
                               `create_time` int(0) UNSIGNED NULL DEFAULT 0,
                               `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
                               `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                               `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                               `user_id` int(0) UNSIGNED NULL DEFAULT 0,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_likes_record
-- ----------------------------
DROP TABLE IF EXISTS `comment_likes_record`;
CREATE TABLE `comment_likes_record`  (
                                         `id` int(0) NOT NULL AUTO_INCREMENT,
                                         `articleId` bigint(0) NOT NULL,
                                         `pId` int(0) NOT NULL,
                                         `likerId` int(0) NOT NULL,
                                         `likeDate` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_record
-- ----------------------------
DROP TABLE IF EXISTS `comment_record`;
CREATE TABLE `comment_record`  (
                                   `id` bigint(0) NOT NULL AUTO_INCREMENT,
                                   `pId` bigint(0) NOT NULL,
                                   `articleId` bigint(0) NOT NULL,
                                   `answererId` int(0) NOT NULL,
                                   `respondentId` int(0) NOT NULL,
                                   `commentDate` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                                   `likes` int(0) NOT NULL,
                                   `commentContent` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                                   `isRead` tinyint(1) NOT NULL,
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
                         `id` int(0) NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags`  (
                         `id` int(0) NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                         `create_time` int(0) UNSIGNED NULL DEFAULT NULL,
                         `is_delete` bit(1) NULL DEFAULT b'0',
                         `category_id` int(0) UNSIGNED NULL DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
                         `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                         `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                         `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                         `gender` bit(1) NOT NULL,
                         `true_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                         `birthday` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                         `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                         `personal_brief` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                         `avatar_img_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         `ip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                         `is_delete` bit(1) NULL DEFAULT b'0',
                         `union_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                         `fans` int(0) UNSIGNED NULL DEFAULT 0,
                         `login_way` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'qq,wechat,github,gitee,normal,phone,email',
                         `create_time` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '创建时间',
                         `recently_time` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '最近登录时间',
                         `update_time` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '更新时间',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1190797317 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_category
-- ----------------------------
DROP TABLE IF EXISTS `user_category`;
CREATE TABLE `user_category`  (
                                  `user_id` int(0) UNSIGNED NOT NULL DEFAULT 0,
                                  `category_id` int(0) UNSIGNED NOT NULL DEFAULT 0,
                                  PRIMARY KEY (`user_id`, `category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
                              `userId` int(0) NOT NULL,
                              `roleId` int(0) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
