#
# alter table shop_order_tracks
#      modify column status enum('INIT_PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'SHIPPING', 'DELIVERED', 'CLOSED') not null;



# alter table order_item_tracks
#     drop primary key ,
#     add primary key (order_item_id, updated_at);

# alter table order_items
#     modify column status enum('PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'WAIT_SHIPMENT',
#         'SHIPPING', 'DELIVERED', 'REFUND', 'CANCELED') not null;
#
# alter table order_item_tracks
#     modify column status enum('PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'WAIT_SHIPMENT',
#         'SHIPPING', 'DELIVERED', 'REFUND', 'CANCELED') not null;

#
# CREATE TABLE `shippings` (
#                              `id` varchar(36) NOT NULL,
#                              `address_from_id` varchar(36) NOT NULL,
#                              `address_to_id` varchar(36) NOT NULL,
#                              `shipper_name` varchar(100) NOT NULL,
#                              `shipper_phone` varchar(20) NOT NULL,
#                              `fee` bigint NOT NULL,
#                              `created_at` datetime(6) DEFAULT NULL,
#                              `status` enum('ORDER_CREATED',
#                                  'WAITING_FOR_PICKUP',
#                                  'PICKED_UP',
#                                  'IN_TRANSIT',
#                                  'OUT_FOR_DELIVERY',
#                                  'DELIVERED',
#                                  'DELIVERY_FAILED',
#                                  'RETURNED') NOT NULL,
#                              PRIMARY KEY (`id`),
#                              KEY `fk_shippings_address_from_id` (`address_from_id`),
#                              KEY `fk_shippings_address_to_id` (`address_to_id`),
#                              CONSTRAINT `fk_shippings_address_from_id` FOREIGN KEY (`address_from_id`) REFERENCES `shop_addresses` (`id`),
#                              CONSTRAINT `fk_shippings_address_to_id` FOREIGN KEY (`address_to_id`) REFERENCES `user_addresses` (`id`)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
#
#
#
# CREATE TABLE `shipping_tracks` (
#                                    `shipping_id` varchar(36) NOT NULL,
#                                    `status` enum('ORDER_CREATED',
#                                        'WAITING_FOR_PICKUP',
#                                        'PICKED_UP',
#                                        'IN_TRANSIT',
#                                        'OUT_FOR_DELIVERY',
#                                        'DELIVERED',
#                                        'DELIVERY_FAILED',
#                                        'RETURNED') NOT NULL,
#                                    `updated_at` datetime(6) NOT NULL,
#                                    PRIMARY KEY (`shipping_id`, `updated_at`),
#                                    KEY `fk_shipping_tracks_shipping_id` (`shipping_id`),
#                                    CONSTRAINT `fk_shipping_tracks_shipping_id` FOREIGN KEY (`shipping_id`) REFERENCES `shippings` (`id`)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
#
# ALTER TABLE `order_items`
#     ADD COLUMN `shipping_id` VARCHAR(36) NOT NULL,
#     ADD CONSTRAINT `fk_order_items_shipping_id` FOREIGN KEY (`shipping_id`) REFERENCES `shippings` (`id`);

# CREATE TABLE `shipping_items` (
#                                   `shipping_id` varchar(36) NOT NULL,
#                                   `order_item_id` varchar(36) NOT NULL,
#                                   PRIMARY KEY (`shipping_id`, `order_item_id`),
#                                   CONSTRAINT `fk_shipping_items_shipping_id` FOREIGN KEY (`shipping_id`)
#                                       REFERENCES `shippings` (`id`) ON DELETE CASCADE,
#                                   CONSTRAINT `fk_shipping_items_order_item_id` FOREIGN KEY (`order_item_id`)
#                                       REFERENCES `order_items` (`id`) ON DELETE CASCADE
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
#
# ALTER TABLE `order_items`
#     DROP FOREIGN KEY `fk_order_items_shipping_id`,
#     DROP COLUMN `shipping_id`;


# alter table shippings
# add column total_weight DECIMAL(10,2) default 0.00;

# alter table shippings
#     modify column total_weight BIGINT default 0;


# alter table shippings
#     add  column note text,
#     add column is_printed bit default false not null,
#     add column day_pickup date,
#     add column cancel_reason text;

