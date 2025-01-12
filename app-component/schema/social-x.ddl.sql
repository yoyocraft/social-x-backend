-- database social_x
DROP TABLE IF EXISTS `config`;

CREATE TABLE `config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    `deleted_at` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'deleted at',
    `extra_data` JSON COMMENT 'extra data',
    `config_key` VARCHAR(255) NOT NULL COMMENT 'config key',
    `config_value` TEXT NOT NULL COMMENT 'config value',
    `version` INT(11) NOT NULL DEFAULT 0 COMMENT 'version',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key_deleted_at` (`config_key`, `deleted_at`)
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT 'project config';

DROP TABLE IF EXISTS `operation_log`;

CREATE TABLE `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    `extra_data` JSON COMMENT 'extra data',
    `operation_type` VARCHAR(64) NOT NULL COMMENT 'operation type',
    `operator_id` BIGINT NOT NULL COMMENT 'operator id',
    `operator_name` VARCHAR(64) NOT NULL COMMENT 'operator name',
    PRIMARY KEY (`id`),
    KEY `idx_operator_id_type` (`operator_id`, `operation_type`)
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT 'operation log';

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    `deleted_at` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'deleted at',
    `extra_data` JSON COMMENT 'extra data',
    `user_id` VARCHAR(64) NOT NULL COMMENT 'user id',
    `email` VARCHAR(64) NOT NULL COMMENT 'email, AES encrypted',
    `email_iv` CHAR(24) NOT NULL COMMENT 'phone iv, for AES encryption, encoded by base64',
    `phone` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'phone, AES encrypted',
    `phone_iv` CHAR(24) NOT NULL DEFAULT '' COMMENT 'phone iv, for AES encryption, encoded by base64',
    `nick_name` VARCHAR(64) NOT NULL COMMENT 'user nick name',
    `avatar` VARCHAR(255) NOT NULL DEFAULT '/media/avatar/default.png' COMMENT 'avatar',
    `gender` TINYINT NOT NULL DEFAULT 0 COMMENT 'gender, 0: UNKNOWN, 1: MALE, 2: FEMALE',
    `date_of_birth` DATE NOT NULL DEFAULT '1970-01-01' COMMENT 'date of birth',
    `bio` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'brief introduction',
    `personalized_tags` JSON COMMENT 'personalized tags',
    `location` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'location',
    `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'status, 0: NORMAL, 1: LOCKED',
    `role` VARCHAR(16) NOT NULL DEFAULT 'USER' COMMENT 'role, USER, ADMIN',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_nick_name` (`nick_name`),
    UNIQUE KEY `uk_email_deleted_at` (`email`, `deleted_at`)
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT 'user';

DROP TABLE IF EXISTS `user_auth`;

CREATE TABLE `user_auth` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    `deleted_at` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'deleted at',
    `extra_data` JSON COMMENT 'extra data',
    `user_id` VARCHAR(64) NOT NULL COMMENT 'user id',
    `identity_type` VARCHAR(64) NOT NULL COMMENT 'identity type, e.g. EMAIL_PASSWORD, EMAIL_CAPTCHA, WECHAT',
    `identifier` VARCHAR(64) NOT NULL COMMENT 'identifier, e.g. username, openid, email',
    `credential` VARCHAR(64) NOT NULL COMMENT 'credential, e.g. password, token, code',
    `salt` CHAR(64) NOT NULL DEFAULT '' COMMENT 'salt',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_identity_type_identifier_deleted_at` (`identity_type`, `identifier`, `deleted_at`)
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT 'user auth';

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    `deleted_at` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'deleted at',
    `extra_data` JSON COMMENT 'extra data',
    `permission_name` VARCHAR(64) NOT NULL COMMENT 'permission name, e.g. PUBLISH_POST, EDIT_USER, DELETE_POST',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_name` (`permission_name`)
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT 'permission';

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    `deleted_at` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'deleted at',
    `extra_data` JSON COMMENT 'extra data',
    `role` VARCHAR(64) NOT NULL COMMENT 'role, e.g. ADMIN, USER',
    `permissions` JSON COMMENT 'permissions, e.g. [PUBLISH_POST, EDIT_USER, DELETE_POST]',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role` (`role`)
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT 'role permission';