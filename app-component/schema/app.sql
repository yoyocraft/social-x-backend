CREATE TABLE `config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` BIGINT(20) NOT NULL COMMENT 'create time',
    `gmt_modified` BIGINT(20) NOT NULL COMMENT 'modified time',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'deleted',
    `extra_data` JSON COMMENT 'extra data',
    `config_key` VARCHAR(255) NOT NULL COMMENT 'config key',
    `config_value` TEXT NOT NULL COMMENT 'config value',
    `env` VARCHAR(16) NOT NULL COMMENT 'env',
    `version` INT(11) NOT NULL DEFAULT 0 COMMENT 'version',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key_env` (`config_key`, `env`)
);