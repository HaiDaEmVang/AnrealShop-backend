ALTER TABLE `products`
    ADD COLUMN height DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    ADD COLUMN length DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    ADD COLUMN width  DECIMAL(10,2) NOT NULL DEFAULT 0.00;

ALTER TABLE `product_media`
    ADD COLUMN `thumbnail_Url` VARCHAR(255);

CREATE TABLE `product_general_attributes` (
                                              `product_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`product_id`,`attribute_value_id`),
    KEY `fk_product_general_attributes_attribute_value_id` (`attribute_value_id`),
    CONSTRAINT `fk_product_general_attributes_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    CONSTRAINT `fk_product_general_attributes_attribute_value_id` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;