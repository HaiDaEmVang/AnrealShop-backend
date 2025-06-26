-- v2__them_bang_product_sku_media.sql

DROP TABLE IF EXISTS `product_sku_media`;
CREATE TABLE `product_sku_media` (
  `id` varchar(36) NOT NULL,
  `type` enum('IMAGE','VIDEO') NOT NULL,
  `url` varchar(255) NOT NULL,
  `product_sku_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_product_sku_media_product_sku_id` (`product_sku_id`),
  CONSTRAINT `FK_product_sku_media_product_sku_id` FOREIGN KEY (`product_sku_id`) REFERENCES `product_skus` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
