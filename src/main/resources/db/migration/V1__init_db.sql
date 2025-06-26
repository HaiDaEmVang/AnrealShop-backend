
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
                         `id` varchar(36) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `name` enum('USER','ADMIN') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_role_name` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users`;
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
    KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
    CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `shops`;
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
    KEY `FK34po7mmli7wotimo70r6640ap` (`user_id`),
    CONSTRAINT `FK34po7mmli7wotimo70r6640ap` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `attribute_keys`;
CREATE TABLE `attribute_keys` (
                                  `id` varchar(36) NOT NULL,
    `allow_custom_values` tinyint(1) DEFAULT '1',
    `created_at` datetime(6) DEFAULT NULL,
    `display_name` varchar(100) NOT NULL,
    `key_name` enum('BRAND','COLOR','MATERIAL','ORIGIN','PATTERN','SIZE','STYLE','TARGET_AUDIENCE') NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `shop_id` varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_attribute_key_name` (`key_name`),
    KEY `FKsik07cug4lf4y8v8c51lpd11q` (`shop_id`),
    CONSTRAINT `FKsik07cug4lf4y8v8c51lpd11q` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `attribute_values`;
CREATE TABLE `attribute_values` (
                                    `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `display_order` int DEFAULT '0',
    `metadata` text,
    `updated_at` datetime(6) DEFAULT NULL,
    `value` varchar(255) NOT NULL,
    `attribute_key_id` varchar(36) NOT NULL,
    `shop_id` varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKhgo27y94xcykg49kk7xfa0of8` (`attribute_key_id`),
    KEY `FK20mh4e6em7g8r0ahsr9kvxgc1` (`shop_id`),
    CONSTRAINT `FK20mh4e6em7g8r0ahsr9kvxgc1` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `FKhgo27y94xcykg49kk7xfa0of8` FOREIGN KEY (`attribute_key_id`) REFERENCES `attribute_keys` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `banners`;
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

DROP TABLE IF EXISTS `carts`;
CREATE TABLE `carts` (
                         `id` varchar(36) NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKb5o626f86h46m4s7ms6ginnop` (`user_id`),
    CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `categories`;
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
    KEY `FKsaok720gsu4u2wrgbk10b5n8d` (`parent_id`),
    CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `chat_rooms`;
CREATE TABLE `chat_rooms` (
                              `id` varchar(36) NOT NULL,
    `last_active` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `chat_messages`;
CREATE TABLE `chat_messages` (
                                 `id` varchar(36) NOT NULL,
    `content` text,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `is_read` tinyint(1) DEFAULT '0',
    `type` enum('TEXT','MEDIA') NOT NULL,
    `room_id` varchar(36) NOT NULL,
    `sender_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKhalwepod3944695ji0suwoqb9` (`room_id`),
    KEY `FKgiqeap8ays4lf684x7m0r2729` (`sender_id`),
    CONSTRAINT `FKgiqeap8ays4lf684x7m0r2729` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKhalwepod3944695ji0suwoqb9` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `chat_room_participants`;
CREATE TABLE `chat_room_participants` (
                                          `chat_room_id` varchar(255) NOT NULL,
    `user_id` varchar(255) NOT NULL,
    PRIMARY KEY (`chat_room_id`,`user_id`),
    KEY `FKiqfwuqd8c8i7hrkukc0rii5pg` (`user_id`),
    CONSTRAINT `FKiqfwuqd8c8i7hrkukc0rii5pg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKkrenof4in9x16he7adf6g058f` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_rooms` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `display_categories`;
CREATE TABLE `display_categories` (
                                      `id` varchar(36) NOT NULL,
    `thumbnail_url` varchar(255) DEFAULT NULL,
    `category_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKo5i70njlkbmgagvm4hwcdnvsy` (`category_id`),
    CONSTRAINT `FKo5i70njlkbmgagvm4hwcdnvsy` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `provinces`;
CREATE TABLE `provinces` (
                             `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `districts`;
CREATE TABLE `districts` (
                             `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK82doq1t64jhly7a546lpvnu2c` (`province_id`),
    CONSTRAINT `FK82doq1t64jhly7a546lpvnu2c` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `follows`;
CREATE TABLE `follows` (
                           `id` varchar(36) NOT NULL,
    `followed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `shop_id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_shop_follow` (`user_id`,`shop_id`),
    KEY `FKj0chrx7ruh4dk7l2cphamofv9` (`shop_id`),
    CONSTRAINT `FKj0chrx7ruh4dk7l2cphamofv9` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `FKn4am7c82j2uo8pkw4x7qibf12` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `notifications`;
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
    KEY `FK9kxl0whvhifo6gw4tjq36v53k` (`receiver_id`),
    CONSTRAINT `FK9kxl0whvhifo6gw4tjq36v53k` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `payments`;
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

DROP TABLE IF EXISTS `wards`;
CREATE TABLE `wards` (
                         `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKfjqt744bo800mb5uax74lav8k` (`district_id`),
    CONSTRAINT `FKfjqt744bo800mb5uax74lav8k` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user_addresses`;
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
    KEY `FK46jbhcddgi2ig4tv9wvr5282o` (`district_id`),
    KEY `FK6vo2j9xym99dvopdbeb9glixc` (`province_id`),
    KEY `FKn2fisxyyu3l9wlch3ve2nocgp` (`user_id`),
    KEY `FKp0h5l8sk3usxw00hqlkybpb5r` (`ward_id`),
    CONSTRAINT `FK46jbhcddgi2ig4tv9wvr5282o` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`),
    CONSTRAINT `FK6vo2j9xym99dvopdbeb9glixc` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`),
    CONSTRAINT `FKn2fisxyyu3l9wlch3ve2nocgp` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKp0h5l8sk3usxw00hqlkybpb5r` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `orders`;
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
    UNIQUE KEY `UKhaujdjk1ohmeixjhnhslchrp1` (`payment_id`),
    KEY `FKebxbj09m4a87s8na3lr86xnf4` (`address_id`),
    KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
    CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK8aol9f99s97mtyhij0tvfj41f` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`),
    CONSTRAINT `FKebxbj09m4a87s8na3lr86xnf4` FOREIGN KEY (`address_id`) REFERENCES `user_addresses` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
                            `id` varchar(36) NOT NULL,
    `average_rating` float NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `deleted` bit(1) NOT NULL,
    `description` text,
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
    `url_path` text,
    `url_slug` text,
    `visible` bit(1) NOT NULL,
    `weight` decimal(10,2) NOT NULL DEFAULT '0.00',
    `category_id` varchar(36) DEFAULT NULL,
    `shop_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
    KEY `FK7kp8sbhxboponhx3lxqtmkcoj` (`shop_id`),
    CONSTRAINT `FK7kp8sbhxboponhx3lxqtmkcoj` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items` (
                              `id` varchar(36) NOT NULL,
    `price` bigint NOT NULL,
    `quantity` int DEFAULT '1',
    `selected` tinyint(1) DEFAULT '1',
    `cart_id` varchar(36) NOT NULL,
    `product_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
    KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
    CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `cart_item_attributes`;
CREATE TABLE `cart_item_attributes` (
                                        `cart_item_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`cart_item_id`,`attribute_value_id`),
    KEY `FKt76yq7lrg7l8jkx4vj7o80fia` (`attribute_value_id`),
    CONSTRAINT `FKh3gmrmbtay8kh6x1r76a36v35` FOREIGN KEY (`cart_item_id`) REFERENCES `cart_items` (`id`),
    CONSTRAINT `FKt76yq7lrg7l8jkx4vj7o80fia` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `order_items`;
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
    KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
    KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
    CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `order_item_attributes`;
CREATE TABLE `order_item_attributes` (
                                         `order_item_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`order_item_id`,`attribute_value_id`),
    KEY `FKpd3snx3p0tn43cfmu48if945v` (`attribute_value_id`),
    CONSTRAINT `FK5y8lj9wjm12t3rwdw2t5kmvra` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
    CONSTRAINT `FKpd3snx3p0tn43cfmu48if945v` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `product_media`;
CREATE TABLE `product_media` (
                                 `id` varchar(36) NOT NULL,
    `type` enum('IMAGE','VIDEO') NOT NULL,
    `url` varchar(255) NOT NULL,
    `product_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK6d0dleyyjhq9qg62vto0kn01f` (`product_id`),
    CONSTRAINT `FK6d0dleyyjhq9qg62vto0kn01f` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `product_reviews`;
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
    UNIQUE KEY `UKi19palx1qyrw7n6jqn26mn3xb` (`order_item_id`),
    KEY `idx_productreview_user_product` (`user_id`,`product_id`),
    KEY `FK35kxxqe2g9r4mww80w9e3tnw9` (`product_id`),
    CONSTRAINT `FK35kxxqe2g9r4mww80w9e3tnw9` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    CONSTRAINT `FK58i39bhws2hss3tbcvdmrm60f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKau5g3dylb9eh7ua5xjjw6uopw` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `product_review_media`;
CREATE TABLE `product_review_media` (
                                        `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `media_type` enum('IMAGE','VIDEO') DEFAULT 'IMAGE',
    `media_url` varchar(255) NOT NULL,
    `review_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKbrlnmogqtxbek3763sqm37t2n` (`review_id`),
    CONSTRAINT `FKbrlnmogqtxbek3763sqm37t2n` FOREIGN KEY (`review_id`) REFERENCES `product_reviews` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `product_skus`;
CREATE TABLE `product_skus` (
                                `id` varchar(36) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `price` bigint NOT NULL,
    `quantity` int DEFAULT '0',
    `sku` varchar(50) NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `product_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_productsku_sku_unique` (`sku`),
    KEY `FKgfjst7dvihycy15ceiruv9roo` (`product_id`),
    CONSTRAINT `FKgfjst7dvihycy15ceiruv9roo` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `shop_addresses`;
CREATE TABLE `shop_addresses` (
                                  `id` varchar(36) NOT NULL,
    `detail` text NOT NULL,
    `phone_number` varchar(20) NOT NULL,
    `sender_name` varchar(100) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    `ward_id` varchar(36) NOT NULL,
    `primary_address` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FKjihc33pfcfolj5kdm8r5e2rjc` (`district_id`),
    KEY `FKcjvsy6qy8vmfbvah9sdj3lhpo` (`province_id`),
    KEY `FKdqm2dvywp1h3d1q0hjlp86224` (`shop_id`),
    KEY `FKij8gkwnyu7mxv0r1un5u0j1x8` (`ward_id`),
    CONSTRAINT `FKcjvsy6qy8vmfbvah9sdj3lhpo` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`),
    CONSTRAINT `FKdqm2dvywp1h3d1q0hjlp86224` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `FKij8gkwnyu7mxv0r1un5u0j1x8` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`),
    CONSTRAINT `FKjihc33pfcfolj5kdm8r5e2rjc` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `shop_categories`;
CREATE TABLE `shop_categories` (
                                   `id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKidgwn2wkyt6ebkm9a1u1n7usl` (`shop_id`),
    CONSTRAINT `FKidgwn2wkyt6ebkm9a1u1n7usl` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `shop_category_items`;
CREATE TABLE `shop_category_items` (
                                       `category_id` varchar(255) NOT NULL,
    `shop_categories_id` varchar(255) NOT NULL,
    PRIMARY KEY (`category_id`,`shop_categories_id`),
    KEY `FKs536flpp26ree8bhoewq8s9vp` (`shop_categories_id`),
    CONSTRAINT `FK6kcmaid96qb4aq2qvmud4du4v` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    CONSTRAINT `FKs536flpp26ree8bhoewq8s9vp` FOREIGN KEY (`shop_categories_id`) REFERENCES `shop_categories` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `shop_orders`;
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
    KEY `FKsgnvljpq2781kepnibt2mhmmg` (`order_id`),
    KEY `FKqmij9swq6yk74wwef6rmiufnx` (`address_id`),
    KEY `FK25a2nj9yypomcxwstjglds195` (`shop_id`),
    KEY `FKj4qixku05sstqepfps9jrq132` (`user_id`),
    CONSTRAINT `FK25a2nj9yypomcxwstjglds195` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
    CONSTRAINT `FKj4qixku05sstqepfps9jrq132` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKqmij9swq6yk74wwef6rmiufnx` FOREIGN KEY (`address_id`) REFERENCES `shop_addresses` (`id`),
    CONSTRAINT `FKsgnvljpq2781kepnibt2mhmmg` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `shop_order_items`;
CREATE TABLE `shop_order_items` (
                                    `shop_order_id` varchar(36) NOT NULL,
    `order_item_id` varchar(36) NOT NULL,
    KEY `FKhy4fpmvty4grhm6tp7xw4v5gp` (`order_item_id`),
    KEY `FKmmrop9hwor1kut8433j0vqtp2` (`shop_order_id`),
    CONSTRAINT `FKhy4fpmvty4grhm6tp7xw4v5gp` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
    CONSTRAINT `FKmmrop9hwor1kut8433j0vqtp2` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `shop_order_tracks`;
CREATE TABLE `shop_order_tracks` (
                                     `updated_at` datetime(6) NOT NULL,
    `status` enum('AWAITING_SHIPMENT','CANCELLED','DELIVERED','IN_TRANSIT','OUT_FOR_DELIVERY','PREPARING','PROCESSING') NOT NULL,
    `shop_order_id` varchar(255) NOT NULL,
    PRIMARY KEY (`shop_order_id`,`updated_at`),
    CONSTRAINT `FKcfqegwwclhbx5k6rs7oikaj50` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `sku_attributes`;
CREATE TABLE `sku_attributes` (
                                  `sku_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`sku_id`,`attribute_value_id`),
    KEY `FKqbgvbbnbx9jgwpkf0rk4xn7kg` (`attribute_value_id`),
    CONSTRAINT `FKffiuqvdic86actxb7corxq4m0` FOREIGN KEY (`sku_id`) REFERENCES `product_skus` (`id`),
    CONSTRAINT `FKqbgvbbnbx9jgwpkf0rk4xn7kg` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;