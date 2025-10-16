# DROP TABLE IF EXISTS `shipping_items`;
#
# ALTER TABLE `shippings`
#     ADD COLUMN `shop_order_id` VARCHAR(36) NOT NULL AFTER `id`,
# ADD UNIQUE KEY `uk_shippings_shop_order_id` (`shop_order_id`),
# ADD CONSTRAINT `fk_shippings_shop_order_id` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`);

#  alter table shop_orders
#      add column total_weight DECIMAL(10,2) default 0.00;

