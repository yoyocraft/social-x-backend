# ************************************************************
# Sequel Ace SQL dump
# Version 20089
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# Host: 127.0.0.1 (MySQL 8.3.0)
# Database: social_x
# Generation Time: 2025-04-14 09:12:34 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table config
# ------------------------------------------------------------

CREATE TABLE `config` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `config_key` varchar(255) NOT NULL COMMENT 'config key',
  `config_value` text NOT NULL COMMENT 'config value',
  `config_type` varchar(32) NOT NULL DEFAULT 'STRING' COMMENT 'STRING, JSON, INTEGER, BOOLEAN, etc.',
  `version` int NOT NULL DEFAULT '0' COMMENT 'version',
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'config description',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key_deleted_at` (`config_key`,`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='project config';



# Dump of table media_resource
# ------------------------------------------------------------

CREATE TABLE `media_resource` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `resource_key` varchar(64) NOT NULL COMMENT 'resource key',
  `resource_type` varchar(32) NOT NULL COMMENT 'resource type, e.g. IMAGE, VIDEO, AUDIO, DOCUMENT, OTHER',
  `resource_url` varchar(255) NOT NULL DEFAULT '' COMMENT 'resource url',
  `source` varchar(32) NOT NULL COMMENT 'resource source, e.g. AVATAR, POST, REPLY',
  `creator_id` varchar(64) NOT NULL COMMENT 'creator id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_key` (`resource_key`),
  UNIQUE KEY `uk_resource_url` (`resource_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='media resource';



# Dump of table notification
# ------------------------------------------------------------

CREATE TABLE `notification` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `notification_id` varchar(64) NOT NULL COMMENT 'notification id',
  `notification_type` varchar(64) NOT NULL COMMENT 'notification type',
  `notification_status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'notification status, e.g. UNREAD(0), READ(1), DELETED(Integer.MAX_VALUE)',
  `receiver_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'receiver id',
  `sender_id` varchar(64) NOT NULL COMMENT 'sender id',
  `read_at` bigint NOT NULL DEFAULT '0' COMMENT 'read time',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_notification_id` (`notification_id`),
  KEY `idx_receiver_id_status` (`receiver_id`,`notification_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='notification';



# Dump of table operation_log
# ------------------------------------------------------------

CREATE TABLE `operation_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `operation_type` varchar(64) NOT NULL COMMENT 'operation type',
  `operator_id` bigint NOT NULL COMMENT 'operator id',
  `operator_name` varchar(64) NOT NULL COMMENT 'operator name',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id_type` (`operator_id`,`operation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='operation log';



# Dump of table permission
# ------------------------------------------------------------

CREATE TABLE `permission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `permission_name` varchar(64) NOT NULL COMMENT 'permission name, e.g. PUBLISH_POST, EDIT_USER, DELETE_POST',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_name` (`permission_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='permission';



# Dump of table role_permission
# ------------------------------------------------------------

CREATE TABLE `role_permission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `role` varchar(64) NOT NULL COMMENT 'role, e.g. ADMIN, USER',
  `permissions` json DEFAULT NULL COMMENT 'permissions, e.g. [PUBLISH_POST, EDIT_USER, DELETE_POST]',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='role permission';



# Dump of table sys_task
# ------------------------------------------------------------

CREATE TABLE `sys_task` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `task_id` varchar(64) NOT NULL COMMENT 'task id',
  `task_type` varchar(64) NOT NULL COMMENT 'task type',
  `task_status` varchar(64) NOT NULL COMMENT 'task status',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sys task';



# Dump of table ugc_category
# ------------------------------------------------------------

CREATE TABLE `ugc_category` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `category_id` varchar(64) NOT NULL COMMENT 'category id',
  `category_name` varchar(64) NOT NULL COMMENT 'category name',
  `creator_id` varchar(64) NOT NULL COMMENT 'creator id',
  `priority` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'priority, 0 is highest, 255 is lowest',
  `type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'type, 0 is for article, 1 is for post',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_id` (`category_id`),
  UNIQUE KEY `uk_category_name_priority` (`category_name`,`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ugc category';



# Dump of table ugc_tag
# ------------------------------------------------------------

CREATE TABLE `ugc_tag` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `tag_id` varchar(64) NOT NULL COMMENT 'tag id',
  `tag_name` varchar(64) NOT NULL COMMENT 'tag name',
  `creator_id` varchar(64) NOT NULL COMMENT 'creator id',
  `priority` smallint unsigned NOT NULL DEFAULT '0' COMMENT 'priority, 0 is highest, 65535 is lowest',
  `type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'type, 0 is for article, 1 is for interests',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_id` (`tag_id`),
  UNIQUE KEY `uk_tag_name_type_priority` (`tag_name`,`type`,`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ugc tag';



# Dump of table user_auth
# ------------------------------------------------------------

CREATE TABLE `user_auth` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `user_id` varchar(64) NOT NULL COMMENT 'user id',
  `identity_type` varchar(64) NOT NULL COMMENT 'identity type, e.g. EMAIL_PASSWORD, EMAIL_CAPTCHA, WECHAT',
  `identifier` varchar(64) NOT NULL COMMENT 'identifier, e.g. username, openid, email',
  `credential` varchar(64) NOT NULL COMMENT 'credential, e.g. password, token, code',
  `salt` char(64) NOT NULL DEFAULT '' COMMENT 'salt',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_identity_type_identifier_deleted_at` (`identity_type`,`identifier`,`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='user auth';



# Dump of table user_info
# ------------------------------------------------------------

CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT 'deleted at',
  `extra_data` json DEFAULT NULL COMMENT 'extra data',
  `user_id` varchar(64) NOT NULL COMMENT 'user id',
  `email` varchar(64) NOT NULL COMMENT 'email, AES encrypted',
  `email_iv` char(24) NOT NULL COMMENT 'phone iv, for AES encryption, encoded by base64',
  `phone` varchar(64) NOT NULL DEFAULT '' COMMENT 'phone, AES encrypted',
  `phone_iv` char(24) NOT NULL DEFAULT '' COMMENT 'phone iv, for AES encryption, encoded by base64',
  `nickname` varchar(64) NOT NULL COMMENT 'user nick name',
  `avatar` varchar(255) NOT NULL DEFAULT '/media/avatar/default.png' COMMENT 'avatar',
  `work_start_time` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '2025-07' COMMENT 'Date of joining the workforce in yyyy-MM format',
  `work_direction` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'work direction, e.g. 0: UNKNOWN, 1: BACKEND, 2: FRONTEND, 3: FULL_STACK',
  `bio` varchar(255) NOT NULL DEFAULT '这个人很懒，什么都没有留下...' COMMENT 'brief introduction',
  `personalized_tags` json DEFAULT NULL COMMENT 'personalized tags',
  `job_title` varchar(64) NOT NULL DEFAULT '暂无' COMMENT 'job title',
  `company` varchar(64) NOT NULL DEFAULT '暂无' COMMENT 'company',
  `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'status, 0: NORMAL, 1: LOCKED',
  `role` varchar(16) NOT NULL DEFAULT 'USER' COMMENT 'role, USER, ADMIN',
  `gmt_create` bigint unsigned NOT NULL COMMENT 'gmt_create',
  `gmt_modified` bigint unsigned NOT NULL COMMENT 'gmt_modified',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_email_deleted_at` (`email`,`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='user';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
