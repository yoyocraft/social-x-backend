CREATE TABLE `config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `gmt_create` BIGINT(20) NOT NULL COMMENT 'create time',
    `gmt_modified` BIGINT(20) NOT NULL COMMENT 'modified time',
    `deleted_at` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'deleted at',
    `extra_data` JSON COMMENT 'extra data',
    `config_key` VARCHAR(255) NOT NULL COMMENT 'config key',
    `config_value` TEXT NOT NULL COMMENT 'config value',
    `env` VARCHAR(16) NOT NULL COMMENT 'env',
    `version` INT(11) NOT NULL DEFAULT 0 COMMENT 'version',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key_env_deleted_at` (`config_key`, `env`, `deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='project config';