-- V2__Update_attribute_keys_table.sql

ALTER TABLE `attribute_keys`
    CHANGE COLUMN `slug_name` `key_name` VARCHAR(50) NOT NULL;

ALTER TABLE `attribute_keys`
    ADD COLUMN `display_order` INT DEFAULT 0;

ALTER TABLE `attribute_keys`
    ADD COLUMN `is_multi_selected` TINYINT(1) DEFAULT 0;

ALTER TABLE `attribute_keys`
    ADD COLUMN `is_for_sku` TINYINT(1) DEFAULT 0;

ALTER TABLE `attribute_keys`
    ADD UNIQUE INDEX `idx_attribute_key_name` (`key_name`);