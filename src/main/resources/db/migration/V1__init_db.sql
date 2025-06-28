-- Create `roles` table first as it's referenced by `users`
CREATE TABLE `roles` (
                         `id` varchar(36) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `name` enum('USER','ADMIN') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_role_name` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `users` table
CREATE TABLE `users` (
                         `id` varchar(36) NOT NULL,
    `avatar_url` varchar(255) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `delete_reason` text,
    `deleted` tinyint(1) DEFAULT '0',
    `dob` date DEFAULT NULL,
    `email` varchar(100) NOT NULL,
    `from_social` tinyint(1) DEFAULT '0',
    `full_name` varchar(100) DEFAULT NULL,
    `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
    `password` varchar(255) NOT NULL,
    `phone_number` varchar(20) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `username` varchar(50) NOT NULL,
    `role_id` varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_username` (`username`),
    UNIQUE KEY `idx_user_email` (`email`),
    KEY `fk_users_role_id` (`role_id`),
    CONSTRAINT `fk_users_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `provinces` table
CREATE TABLE `provinces` (
                             `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `districts` table, referencing `provinces`
CREATE TABLE `districts` (
                             `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_districts_province_id` (`province_id`),
    CONSTRAINT `fk_districts_province_id` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `wards` table, referencing `districts`
CREATE TABLE `wards` (
                         `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_wards_district_id` (`district_id`),
    CONSTRAINT `fk_wards_district_id` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `user_addresses` table, referencing `users`, `provinces`, `districts`, and `wards`
CREATE TABLE `user_addresses` (
                                  `id` varchar(36) NOT NULL,
    `detail` text NOT NULL,
    `phone_number` varchar(20) NOT NULL,
    `primary_address` tinyint(1) DEFAULT '0',
    `receiver_name` varchar(100) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    `ward_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_user_addresses_district_id` (`district_id`),
    KEY `fk_user_addresses_province_id` (`province_id`),
    KEY `fk_user_addresses_user_id` (`user_id`),
    KEY `fk_user_addresses_ward_id` (`ward_id`),
    CONSTRAINT `fk_user_addresses_district_id` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`),
    CONSTRAINT `fk_user_addresses_province_id` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`),
    CONSTRAINT `fk_user_addresses_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_user_addresses_ward_id` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shops` table, referencing `users`
CREATE TABLE `shops` (
                         `id` varchar(36) NOT NULL,
    `avatar_url` varchar(255) DEFAULT NULL,
    `average_rating` float DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` tinyint(1) DEFAULT '0',
    `description` text,
    `follower_count` int DEFAULT '0',
    `name` varchar(100) NOT NULL,
    `product_count` int DEFAULT '0',
    `revenue` bigint DEFAULT '0',
    `total_reviews` int DEFAULT '0',
    `updated_at` datetime(6) DEFAULT NULL,
    `url_slug` text,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_shops_user_id` (`user_id`),
    CONSTRAINT `fk_shops_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shop_addresses` table, referencing `shops`, `provinces`, `districts`, and `wards`
CREATE TABLE `shop_addresses` (
                                  `id` varchar(36) NOT NULL,
    `detail` text NOT NULL,
    `phone_number` varchar(20) NOT NULL,
    `primary_address` tinyint(1) DEFAULT '0',
    `sender_name` varchar(100) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    `ward_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_shop_addresses_district_id` (`district_id`),
    KEY `fk_shop_addresses_province_id` (`province_id`),
    KEY `fk_shop_addresses_shop_id` (`shop_id`),
    KEY `fk_shop_addresses_ward_id` (`ward_id`),
    CONSTRAINT `fk_shop_addresses_district_id` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`),
    CONSTRAINT `fk_shop_addresses_province_id` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`),
    CONSTRAINT `fk_shop_addresses_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `fk_shop_addresses_ward_id` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `follows` table, referencing `shops` and `users`
CREATE TABLE `follows` (
                           `id` varchar(36) NOT NULL,
    `followed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `shop_id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_shop_follow` (`user_id`,`shop_id`),
    KEY `fk_follows_shop_id` (`shop_id`),
    CONSTRAINT `fk_follows_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `fk_follows_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `notifications` table, referencing `users`
CREATE TABLE `notifications` (
                                 `id` varchar(36) NOT NULL,
    `content` text NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `is_read` tinyint(1) DEFAULT '0',
    `item_count` int DEFAULT '0',
    `redirect_url` varchar(255) DEFAULT NULL,
    `scope` enum('SHOP','BUYER') NOT NULL,
    `thumbnail_url` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `receiver_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_notifications_receiver_id` (`receiver_id`),
    CONSTRAINT `fk_notifications_receiver_id` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `categories` table
CREATE TABLE `categories` (
                              `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `description` text,
    `has_children` tinyint(1) NOT NULL DEFAULT '0',
    `name` varchar(100) NOT NULL,
    `product_count` int NOT NULL DEFAULT '0',
    `url_path` varchar(100) DEFAULT NULL,
    `url_slug` varchar(100) DEFAULT NULL,
    `parent_id` varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_categories_parent_id` (`parent_id`),
    CONSTRAINT `fk_categories_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `display_categories` table, referencing `categories`
CREATE TABLE `display_categories` (
                                      `id` varchar(36) NOT NULL,
    `thumbnail_url` varchar(255) DEFAULT NULL,
    `category_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_display_categories_category_id` (`category_id`),
    CONSTRAINT `fk_display_categories_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `products` table, referencing `shops` and `categories`
CREATE TABLE `products` (
                            `id` varchar(36) NOT NULL,
    `average_rating` float NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `deleted` bit(1) NOT NULL,
    `description` text,
    `discount_price` bigint NOT NULL,
    `name` varchar(255) NOT NULL,
    `price` bigint NOT NULL,
    `quantity` int NOT NULL,
    `restrict_status` enum('OPENED','PENDING','RESTRICTED') NOT NULL,
    `restricted` bit(1) NOT NULL,
    `restricted_reason` text,
    `revenue` bigint NOT NULL,
    `sold` int NOT NULL,
    `sort_description` text,
    `thumbnail_url` varchar(255) DEFAULT NULL,
    `total_reviews` int NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `url_slug` text,
    `visible` bit(1) NOT NULL,
    `weight` decimal(10,2) NOT NULL DEFAULT '0.00',
    `category_id` varchar(36) DEFAULT NULL,
    `shop_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_products_category_id` (`category_id`),
    KEY `fk_products_shop_id` (`shop_id`),
    CONSTRAINT `fk_products_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    CONSTRAINT `fk_products_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `product_media` table, referencing `products`
CREATE TABLE `product_media` (
                                 `id` varchar(36) NOT NULL,
    `type` enum('IMAGE','VIDEO') NOT NULL,
    `url` varchar(255) NOT NULL,
    `product_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_product_media_product_id` (`product_id`),
    CONSTRAINT `fk_product_media_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `attribute_keys` table
CREATE TABLE `attribute_keys` (
                                  `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `display_name` varchar(100) NOT NULL,
    `is_default` tinyint(1) DEFAULT '0',
    `slug_name` varchar(50) NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `attribute_values` table, referencing `attribute_keys`
CREATE TABLE `attribute_values` (
                                    `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `display_order` int DEFAULT '0',
    `is_default` tinyint(1) DEFAULT '0',
    `metadata` text,
    `updated_at` datetime(6) DEFAULT NULL,
    `value` varchar(255) NOT NULL,
    `attribute_key_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_attribute_values_attribute_key_id` (`attribute_key_id`),
    CONSTRAINT `fk_attribute_values_attribute_key_id` FOREIGN KEY (`attribute_key_id`) REFERENCES `attribute_keys` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `product_skus` table, referencing `products`
CREATE TABLE `product_skus` (
                                `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `price` bigint NOT NULL,
    `quantity` int DEFAULT '0',
    `sku` varchar(50) NOT NULL,
    `image_urls` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `product_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_productsku_sku_unique` (`sku`),
    KEY `fk_product_skus_product_id` (`product_id`),
    CONSTRAINT `fk_product_skus_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `sku_attributes` table, referencing `product_skus` and `attribute_values`
CREATE TABLE `sku_attributes` (
                                  `sku_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`sku_id`,`attribute_value_id`),
    KEY `fk_sku_attributes_attribute_value_id` (`attribute_value_id`),
    CONSTRAINT `fk_sku_attributes_sku_id` FOREIGN KEY (`sku_id`) REFERENCES `product_skus` (`id`),
    CONSTRAINT `fk_sku_attributes_attribute_value_id` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shop_attribute_keys` table, referencing `attribute_keys` and `shops`
CREATE TABLE `shop_attribute_keys` (
                                       `attribute_key_id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    PRIMARY KEY (`attribute_key_id`,`shop_id`),
    KEY `fk_shop_attribute_keys_shop_id` (`shop_id`),
    CONSTRAINT `fk_shop_attribute_keys_attribute_key_id` FOREIGN KEY (`attribute_key_id`) REFERENCES `attribute_keys` (`id`),
    CONSTRAINT `fk_shop_attribute_keys_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shop_categories` table, referencing `shops`
CREATE TABLE `shop_categories` (
                                   `id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_shop_categories_shop_id` (`shop_id`),
    CONSTRAINT `fk_shop_categories_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shop_category_items` table, referencing `categories` and `shop_categories`
CREATE TABLE `shop_category_items` (
                                       `category_id` varchar(255) NOT NULL,
    `shop_categories_id` varchar(255) NOT NULL,
    PRIMARY KEY (`category_id`,`shop_categories_id`),
    KEY `fk_shop_category_items_shop_categories_id` (`shop_categories_id`),
    CONSTRAINT `fk_shop_category_items_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    CONSTRAINT `fk_shop_category_items_shop_categories_id` FOREIGN KEY (`shop_categories_id`) REFERENCES `shop_categories` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `carts` table, referencing `users`
CREATE TABLE `carts` (
                         `id` varchar(36) NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_carts_user_id` (`user_id`),
    CONSTRAINT `fk_carts_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `cart_items` table, referencing `carts` and `products`
CREATE TABLE `cart_items` (
                              `id` varchar(36) NOT NULL,
    `price` bigint NOT NULL,
    `quantity` int DEFAULT '1',
    `selected` tinyint(1) DEFAULT '1',
    `cart_id` varchar(36) NOT NULL,
    `product_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_cart_items_cart_id` (`cart_id`),
    KEY `fk_cart_items_product_id` (`product_id`),
    CONSTRAINT `fk_cart_items_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    CONSTRAINT `fk_cart_items_cart_id` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `cart_item_attributes` table, referencing `cart_items` and `attribute_values`
CREATE TABLE `cart_item_attributes` (
                                        `cart_item_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`cart_item_id`,`attribute_value_id`),
    KEY `fk_cart_item_attributes_attribute_value_id` (`attribute_value_id`),
    CONSTRAINT `fk_cart_item_attributes_cart_item_id` FOREIGN KEY (`cart_item_id`) REFERENCES `cart_items` (`id`),
    CONSTRAINT `fk_cart_item_attributes_attribute_value_id` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `payments` table
CREATE TABLE `payments` (
                            `id` varchar(36) NOT NULL,
    `amount` bigint NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `expire_at` datetime(6) DEFAULT NULL,
    `gateway` enum('VNPAY','CASH_ON_DELIVERY') DEFAULT NULL,
    `status` enum('PENDING','COD','COMPLETED','EXPIRED','CANCELLED','REFUNDED','FAILED') NOT NULL DEFAULT 'PENDING',
    `type` enum('BANK_TRANSFER','COD') DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `orders` table, referencing `payments`, `user_addresses`, and `users`
CREATE TABLE `orders` (
                          `id` varchar(36) NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `grand_total_amount` bigint NOT NULL,
    `shipping_fee` bigint DEFAULT NULL,
    `status` enum('AWAITING_SHIPMENT','CANCELLED','DELIVERED','IN_TRANSIT','OUT_FOR_DELIVERY','PREPARING','PROCESSING') NOT NULL,
    `sub_total_amount` bigint DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `payment_id` varchar(36) DEFAULT NULL,
    `address_id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orders_payment_id` (`payment_id`),
    KEY `fk_orders_address_id` (`address_id`),
    KEY `fk_orders_user_id` (`user_id`),
    CONSTRAINT `fk_orders_payment_id` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`),
    CONSTRAINT `fk_orders_address_id` FOREIGN KEY (`address_id`) REFERENCES `user_addresses` (`id`),
    CONSTRAINT `fk_orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `order_items` table, referencing `orders` and `products`
CREATE TABLE `order_items` (
                               `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `price` bigint NOT NULL,
    `quantity` int NOT NULL,
    `success` tinyint(1) DEFAULT '0',
    `updated_at` datetime(6) DEFAULT NULL,
    `order_id` varchar(36) NOT NULL,
    `product_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_order_items_order_id` (`order_id`),
    KEY `fk_order_items_product_id` (`product_id`),
    CONSTRAINT `fk_order_items_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `fk_order_items_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `order_item_attributes` table, referencing `order_items` and `attribute_values`
CREATE TABLE `order_item_attributes` (
                                         `order_item_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`order_item_id`,`attribute_value_id`),
    KEY `fk_order_item_attributes_attribute_value_id` (`attribute_value_id`),
    CONSTRAINT `fk_order_item_attributes_order_item_id` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
    CONSTRAINT `fk_order_item_attributes_attribute_value_id` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `product_reviews` table, referencing `order_items`, `products`, and `users`
CREATE TABLE `product_reviews` (
                                   `id` varchar(36) NOT NULL,
    `comment` text,
    `created_at` datetime(6) DEFAULT NULL,
    `rating` int NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `order_item_id` varchar(36) DEFAULT NULL,
    `product_id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_reviews_order_item_id` (`order_item_id`),
    KEY `idx_productreview_user_product` (`user_id`,`product_id`),
    KEY `fk_product_reviews_product_id` (`product_id`),
    CONSTRAINT `fk_product_reviews_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    CONSTRAINT `fk_product_reviews_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_product_reviews_order_item_id` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `product_review_media` table, referencing `product_reviews`
CREATE TABLE `product_review_media` (
                                        `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `media_type` enum('IMAGE','VIDEO') DEFAULT 'IMAGE',
    `media_url` varchar(255) NOT NULL,
    `review_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_product_review_media_review_id` (`review_id`),
    CONSTRAINT `fk_product_review_media_review_id` FOREIGN KEY (`review_id`) REFERENCES `product_reviews` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shop_orders` table, referencing `orders`, `shop_addresses`, `shops`, and `users`
CREATE TABLE `shop_orders` (
                               `id` varchar(36) NOT NULL,
    `cancel_reason` text,
    `canceled_by` enum('CUSTOMER','SHOP') DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `shipping_fee` int DEFAULT '0',
    `status` enum('AWAITING_SHIPMENT','CANCELLED','DELIVERED','IN_TRANSIT','OUT_FOR_DELIVERY','PREPARING','PROCESSING') DEFAULT NULL,
    `total` bigint NOT NULL,
    `order_id` varchar(36) NOT NULL,
    `address_id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_shop_orders_order_id` (`order_id`),
    KEY `fk_shop_orders_address_id` (`address_id`),
    KEY `fk_shop_orders_shop_id` (`shop_id`),
    KEY `fk_shop_orders_user_id` (`user_id`),
    CONSTRAINT `fk_shop_orders_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `fk_shop_orders_address_id` FOREIGN KEY (`address_id`) REFERENCES `shop_addresses` (`id`),
    CONSTRAINT `fk_shop_orders_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `fk_shop_orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shop_order_items` table, referencing `shop_orders` and `order_items`
CREATE TABLE `shop_order_items` (
                                    `shop_order_id` varchar(36) NOT NULL,
    `order_item_id` varchar(36) NOT NULL,
    KEY `fk_shop_order_items_order_item_id` (`order_item_id`),
    KEY `fk_shop_order_items_shop_order_id` (`shop_order_id`),
    CONSTRAINT `fk_shop_order_items_order_item_id` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
    CONSTRAINT `fk_shop_order_items_shop_order_id` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `shop_order_tracks` table, referencing `shop_orders`
CREATE TABLE `shop_order_tracks` (
                                     `updated_at` datetime(6) NOT NULL,
    `status` enum('AWAITING_SHIPMENT','CANCELLED','DELIVERED','IN_TRANSIT','OUT_FOR_DELIVERY','PREPARING','PROCESSING') NOT NULL,
    `shop_order_id` varchar(255) NOT NULL,
    PRIMARY KEY (`shop_order_id`,`updated_at`),
    CONSTRAINT `fk_shop_order_tracks_shop_order_id` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `chat_rooms` table
CREATE TABLE `chat_rooms` (
                              `id` varchar(36) NOT NULL,
    `last_active` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `chat_messages` table, referencing `chat_rooms` and `users`
CREATE TABLE `chat_messages` (
                                 `id` varchar(36) NOT NULL,
    `content` text,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `is_read` tinyint(1) DEFAULT '0',
    `type` enum('TEXT','MEDIA') NOT NULL,
    `room_id` varchar(36) NOT NULL,
    `sender_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_chat_messages_room_id` (`room_id`),
    KEY `fk_chat_messages_sender_id` (`sender_id`),
    CONSTRAINT `fk_chat_messages_sender_id` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_chat_messages_room_id` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `chat_room_participants` table, referencing `chat_rooms` and `users`
CREATE TABLE `chat_room_participants` (
                                          `chat_room_id` varchar(255) NOT NULL,
    `user_id` varchar(255) NOT NULL,
    PRIMARY KEY (`chat_room_id`,`user_id`),
    KEY `fk_chat_room_participants_user_id` (`user_id`),
    CONSTRAINT `fk_chat_room_participants_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_chat_room_participants_chat_room_id` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_rooms` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `banners` table
CREATE TABLE `banners` (
                           `id` varchar(36) NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `description` text,
    `display_order` int DEFAULT '0',
    `end_date` datetime(6) DEFAULT NULL,
    `image_url` varchar(255) NOT NULL,
    `is_active` tinyint(1) DEFAULT '1',
    `redirect_url` varchar(255) DEFAULT NULL,
    `start_date` datetime(6) DEFAULT NULL,
    `title` varchar(100) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;