-- V1__init_db.sql
-- Initial database schema setup

-- Address related tables
CREATE TABLE `provinces` (
                             `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `districts` (
                             `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_district_province` (`province_id`),
    CONSTRAINT `FK_district_province` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `wards` (
                         `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ward_district` (`district_id`),
    CONSTRAINT `FK_ward_district` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Roles table
CREATE TABLE `roles` (
                         `id` varchar(36) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `name` enum('USER','ADMIN') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_name` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Users table
CREATE TABLE `users` (
                         `id` varchar(36) NOT NULL,
    `username` varchar(50) NOT NULL,
    `email` varchar(100) NOT NULL,
    `password` varchar(255) NOT NULL,
    `full_name` varchar(100) DEFAULT NULL,
    `phone_number` varchar(20) DEFAULT NULL,
    `avatar_url` varchar(255) DEFAULT NULL,
    `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `role_id` varchar(36) DEFAULT NULL,
    `from_social` tinyint(1) DEFAULT '0',
    `dob` date DEFAULT NULL,
    `deleted` tinyint(1) DEFAULT '0',
    `delete_reason` text,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`),
    UNIQUE KEY `uk_user_email` (`email`),
    KEY `FK_user_role` (`role_id`),
    CONSTRAINT `FK_user_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- User addresses
CREATE TABLE `user_addresses` (
                                  `id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL, -- << Đã là user_id
    `receiver_name` varchar(100) NOT NULL,
    `phone_number` varchar(20) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    `ward_id` varchar(36) NOT NULL,
    `detail` text NOT NULL,
    `primary_address` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FK_useraddress_user` (`user_id`),
    KEY `FK_useraddress_province` (`province_id`),
    KEY `FK_useraddress_district` (`district_id`),
    KEY `FK_useraddress_ward` (`ward_id`),
    CONSTRAINT `FK_useraddress_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_useraddress_province` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_useraddress_district` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_useraddress_ward` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Shops table
CREATE TABLE `shops` (
                         `id` varchar(36) NOT NULL,
    `name` varchar(100) NOT NULL,
    `description` text,
    `avatar_url` varchar(255) DEFAULT NULL,
    `user_id` varchar(36) NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `product_count` int DEFAULT '0',
    `revenue` bigint DEFAULT '0',
    `average_rating` float DEFAULT '0',
    `total_reviews` int DEFAULT '0',
    `follower_count` int DEFAULT '0',
    `deleted` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FK_shop_user` (`user_id`),
    CONSTRAINT `FK_shop_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Shop addresses
CREATE TABLE `shop_addresses` (
                                  `id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    `sender_name` varchar(100) NOT NULL,
    `phone_number` varchar(20) NOT NULL,
    `province_id` varchar(36) NOT NULL,
    `district_id` varchar(36) NOT NULL,
    `ward_id` varchar(36) NOT NULL,
    `detail` text NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_shopaddress_shop` (`shop_id`),
    KEY `FK_shopaddress_province` (`province_id`),
    KEY `FK_shopaddress_district` (`district_id`),
    KEY `FK_shopaddress_ward` (`ward_id`),
    CONSTRAINT `FK_shopaddress_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_shopaddress_province` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_shopaddress_district` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_shopaddress_ward` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Categories
CREATE TABLE `categories` (
                              `id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `name` varchar(100) NOT NULL,
    `parent_id` varchar(36) DEFAULT NULL, -- << Đã sửa thành VARCHAR(36)
    `description` text,
    `has_children` tinyint(1) DEFAULT '0',
    `product_count` int DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_category_parent` (`parent_id`),
    CONSTRAINT `FK_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Shop categories (junction table header)
CREATE TABLE `shop_categories` (
                                   `id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `shop_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_shopcategory_shop` (`shop_id`),
    CONSTRAINT `FK_shopcategory_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `shop_category_items` (
                                       `shop_categories_id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `category_id` varchar(36) NOT NULL,        -- << Đã sửa thành VARCHAR(36)
    PRIMARY KEY (`shop_categories_id`, `category_id`),
    KEY `FK_shopcategoryitem_category` (`category_id`),
    CONSTRAINT `FK_shopcategoryitem_shopcategory` FOREIGN KEY (`shop_categories_id`) REFERENCES `shop_categories` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_shopcategoryitem_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Attribute System
CREATE TABLE `attribute_keys` (
                                  `id` varchar(36) NOT NULL,
    `key_name` enum('COLOR','SIZE','MATERIAL') NOT NULL, -- Giữ ENUM CSDL theo schema_v3
    `display_name` varchar(100) NOT NULL, -- << Đã thêm display_name
    `allow_custom_values` tinyint(1) DEFAULT '1',
    `shop_id` varchar(36) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_attributekey_keyname_shop` (`key_name`, `shop_id`), -- << UNIQUE KEY tốt hơn
    KEY `FK_attributekey_shop` (`shop_id`),
    CONSTRAINT `FK_attributekey_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `attribute_values` (
                                    `id` varchar(36) NOT NULL,
    `attribute_key_id` varchar(36) NOT NULL,
    `value` varchar(255) NOT NULL,
    `display_order` int DEFAULT '0',
    `metadata` text,
    `shop_id` varchar(36) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_attributevalue_key_value_shop` (`attribute_key_id`, `value`, `shop_id`), -- << Đã thêm UNIQUE
    KEY `FK_attributevalue_key` (`attribute_key_id`),
    KEY `FK_attributevalue_shop` (`shop_id`),
    CONSTRAINT `FK_attributevalue_key` FOREIGN KEY (`attribute_key_id`) REFERENCES `attribute_keys` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_attributevalue_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Products table
CREATE TABLE `products` (
                            `id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `name` varchar(255) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    `description` text,
    `thumbnail_url` varchar(255) DEFAULT NULL,
    `price` bigint NOT NULL, -- << Đã sửa về BIGINT
    `quantity` int DEFAULT '0',
    `category_id` varchar(36) DEFAULT NULL, -- << Đã sửa thành VARCHAR(36)
    `weight` decimal(10,2) NOT NULL DEFAULT '0.00', -- << Đã sửa thành DECIMAL
    `revenue` bigint DEFAULT '0',
    `sold` int DEFAULT '0',
    `average_rating` float DEFAULT '0',
    `total_reviews` int DEFAULT '0',
    `location` varchar(255) DEFAULT NULL,
    `visible` tinyint(1) DEFAULT '1',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `restricted` tinyint(1) DEFAULT '0',
    `restricted_reason` text,
    `restrict_status` enum('PENDING','RESTRICTED','OPENED') DEFAULT 'PENDING',
    `deleted` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FK_product_shop` (`shop_id`),
    KEY `FK_product_category` (`category_id`),
    CONSTRAINT `FK_product_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_product_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Product SKUs
CREATE TABLE `product_skus` (
                                `id` varchar(36) NOT NULL,
    `product_id` varchar(36) NOT NULL,
    `sku` varchar(50) NOT NULL,
    `price` bigint NOT NULL,
    `quantity` int DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_productsku_sku` (`sku`), -- SKU nên là unique toàn cục hoặc ít nhất trong product
    KEY `FK_productsku_product` (`product_id`),
    CONSTRAINT `FK_productsku_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- SKU attributes (junction table)
CREATE TABLE `sku_attributes` (
                                  `sku_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`sku_id`, `attribute_value_id`),
    KEY `FK_skuattribute_attributevalue` (`attribute_value_id`),
    CONSTRAINT `FK_skuattribute_sku` FOREIGN KEY (`sku_id`) REFERENCES `product_skus` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_skuattribute_attributevalue` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Product media
CREATE TABLE `product_media` (
                                 `id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `product_id` varchar(36) NOT NULL,
    `url` varchar(255) NOT NULL,
    `type` enum('IMAGE','VIDEO') DEFAULT 'IMAGE',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_productmedia_product` (`product_id`),
    CONSTRAINT `FK_productmedia_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Carts
CREATE TABLE `carts` (
                         `id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_cart_user` (`user_id`),
    CONSTRAINT `FK_cart_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Cart items
CREATE TABLE `cart_items` (
                              `id` varchar(36) NOT NULL,
    `cart_id` varchar(36) NOT NULL,
    `product_id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `price` bigint NOT NULL,
    `quantity` int DEFAULT '1',
    `selected` tinyint(1) DEFAULT '1',
    PRIMARY KEY (`id`),
    KEY `FK_cartitem_cart` (`cart_id`),
    KEY `FK_cartitem_product` (`product_id`),
    CONSTRAINT `FK_cartitem_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_cartitem_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE -- Hoặc RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Cart item attributes
CREATE TABLE `cart_item_attributes` (
                                        `cart_item_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`cart_item_id`, `attribute_value_id`),
    KEY `FK_cartitemattribute_attributevalue` (`attribute_value_id`),
    CONSTRAINT `FK_cartitemattribute_cartitem` FOREIGN KEY (`cart_item_id`) REFERENCES `cart_items` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_cartitemattribute_attributevalue` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Payments
CREATE TABLE `payments` (
                            `id` varchar(36) NOT NULL,
    `amount` bigint NOT NULL,
    `gateway` enum('VNPAY','CASH_ON_DELIVERY','MOMO','ZALOPAY','STRIPE','PAYPAL','DIRECT_COD','MANUAL_BANK_TRANSFER','NONE') DEFAULT NULL, -- << Mở rộng Enum
    `type` enum('ORDER_PAYMENT','REFUND','WALLET_TOPUP','SUBSCRIPTION_FEE','COD','BANK_TRANSFER') DEFAULT NULL, -- << Mở rộng và điều chỉnh Enum
    `status` enum('PENDING','COD','COMPLETED','EXPIRED','CANCELLED','REFUNDED','FAILED') NOT NULL DEFAULT 'PENDING',
    `expire_at` timestamp NULL DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Orders
CREATE TABLE `orders` (
                          `id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    `address_id` varchar(36) NOT NULL,
    `payment_id` varchar(36) DEFAULT NULL,
    `status` enum('PENDING_CONFIRMATION','PROCESSING','PREPARING','AWAITING_SHIPMENT','IN_TRANSIT','OUT_FOR_DELIVERY','DELIVERED','COMPLETED','CANCELLED','RETURNED') NOT NULL DEFAULT 'PENDING_CONFIRMATION', -- << Thêm default
    `sub_total_amount` bigint DEFAULT NULL,
    `shipping_fee` bigint DEFAULT NULL,
    `grand_total_amount` bigint NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_payment` (`payment_id`), -- Đảm bảo 1 payment chỉ cho 1 order
    KEY `FK_order_user` (`user_id`),
    KEY `FK_order_address` (`address_id`),
    CONSTRAINT `FK_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_order_address` FOREIGN KEY (`address_id`) REFERENCES `user_addresses` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_order_payment` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Order items
CREATE TABLE `order_items` (
                               `id` varchar(36) NOT NULL,
    `order_id` varchar(36) NOT NULL,
    `product_id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `quantity` int NOT NULL,
    `price` bigint NOT NULL,
    `success` tinyint(1) DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_orderitem_order` (`order_id`),
    KEY `FK_orderitem_product` (`product_id`),
    CONSTRAINT `FK_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_orderitem_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Order item attributes
CREATE TABLE `order_item_attributes` (
                                         `order_item_id` varchar(36) NOT NULL,
    `attribute_value_id` varchar(36) NOT NULL,
    PRIMARY KEY (`order_item_id`, `attribute_value_id`),
    KEY `FK_orderitemattribute_attributevalue` (`attribute_value_id`),
    CONSTRAINT `FK_orderitemattribute_orderitem` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_orderitemattribute_attributevalue` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_values` (`id`) ON DELETE RESTRICT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Shop orders
CREATE TABLE `shop_orders` (
                               `id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    `order_id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    `shipping_fee` int DEFAULT '0',
    `status` enum('PENDING_CONFIRMATION','PROCESSING','PREPARING','AWAITING_SHIPMENT','IN_TRANSIT','OUT_FOR_DELIVERY','DELIVERED','COMPLETED','CANCELLED','RETURNED') DEFAULT 'PENDING_CONFIRMATION', -- << Dùng enum của OrderStatus
    `cancel_reason` text,
    `canceled_by` enum('USER','SHOP','ADMIN','SYSTEM') DEFAULT NULL, -- << Dùng enum CancelledParty
    `total` bigint NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_shoporder_user` (`user_id`),
    KEY `FK_shoporder_order` (`order_id`),
    KEY `FK_shoporder_shop` (`shop_id`),
    CONSTRAINT `FK_shoporder_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_shoporder_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_shoporder_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Shop order tracks
CREATE TABLE `shop_order_tracks` (
                                     `shop_order_id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `updated_at` timestamp NOT NULL, -- Nên dùng timestamp thay vì datetime(6) để nhất quán với created_at
    `status` enum('PENDING_CONFIRMATION','PROCESSING','PREPARING','AWAITING_SHIPMENT','IN_TRANSIT','OUT_FOR_DELIVERY','DELIVERED','COMPLETED','CANCELLED','RETURNED') NOT NULL, -- << Dùng enum của OrderStatus
    PRIMARY KEY (`shop_order_id`, `updated_at`),
    CONSTRAINT `FK_shopordertrack_shoporder` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Shop order items (junction table)
CREATE TABLE `shop_order_items` (
                                    `shop_order_id` varchar(36) NOT NULL,
    `order_item_id` varchar(36) NOT NULL,
    PRIMARY KEY (`shop_order_id`, `order_item_id`), -- << ĐÃ THÊM PRIMARY KEY
    KEY `FK_shoporderitem_orderitem` (`order_item_id`), -- Thêm key cho FK nếu cần
    CONSTRAINT `FK_shoporderitem_shoporder` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_shoporderitem_orderitem` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Product reviews
CREATE TABLE `product_reviews` (
                                   `id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    `product_id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `order_item_id` varchar(36) DEFAULT NULL, -- Giữ UNIQUE ở Entity hoặc DB constraint riêng
    `rating` int NOT NULL,
    `comment` text,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_review_order_item` (`order_item_id`),
    KEY `FK_productreview_user` (`user_id`),
    KEY `FK_productreview_product` (`product_id`),
    CONSTRAINT `FK_productreview_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_productreview_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_productreview_orderitem` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Product review media
CREATE TABLE `product_review_media` (
                                        `id` varchar(36) NOT NULL,
    `review_id` varchar(36) NOT NULL,
    `media_url` varchar(255) NOT NULL,
    `media_type` enum('IMAGE','VIDEO') DEFAULT 'IMAGE',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_productreviewmedia_review` (`review_id`),
    CONSTRAINT `FK_productreviewmedia_review` FOREIGN KEY (`review_id`) REFERENCES `product_reviews` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Follows
CREATE TABLE `follows` (
                           `id` varchar(36) NOT NULL,
    `shop_id` varchar(36) NOT NULL,
    `user_id` varchar(36) NOT NULL,
    `followed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_shop_follow` (`user_id`, `shop_id`),
    KEY `FK_follow_shop` (`shop_id`),
    CONSTRAINT `FK_follow_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_follow_shop` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Chat rooms
CREATE TABLE `chat_rooms` (
                              `id` varchar(36) NOT NULL,
    `last_active` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Chat room participants
CREATE TABLE `chat_room_participants` (
                                          `chat_room_id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `user_id` varchar(36) NOT NULL,    -- << Đã sửa thành VARCHAR(36)
    PRIMARY KEY (`chat_room_id`, `user_id`),
    KEY `FK_chatparticipant_user` (`user_id`),
    CONSTRAINT `FK_chatparticipant_room` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_chatparticipant_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Chat messages
CREATE TABLE `chat_messages` (
                                 `id` varchar(36) NOT NULL,
    `room_id` varchar(36) NOT NULL,
    `sender_id` varchar(36) NOT NULL,
    `type` enum('TEXT','MEDIA') NOT NULL,
    `content` text,
    `is_read` tinyint(1) DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_chatmessage_room` (`room_id`),
    KEY `FK_chatmessage_sender` (`sender_id`),
    CONSTRAINT `FK_chatmessage_room` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_chatmessage_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Notifications
CREATE TABLE `notifications` (
                                 `id` varchar(36) NOT NULL,
    `content` text NOT NULL,
    `scope` enum('SHOP','BUYER') NOT NULL,
    `item_count` int DEFAULT '0',
    `thumbnail_url` varchar(255) DEFAULT 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', -- << Giữ lại default URL
    `receiver_id` varchar(36) NOT NULL,
    `redirect_url` varchar(255) DEFAULT NULL,
    `is_read` tinyint(1) DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_notification_receiver` (`receiver_id`),
    CONSTRAINT `FK_notification_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Display categories for homepage
CREATE TABLE `display_categories` (
                                      `id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `category_id` varchar(36) NOT NULL, -- << Đã sửa thành VARCHAR(36)
    `thumbnail_url` varchar(255) DEFAULT 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742879507/Screenshot_2025-03-25_121109-removebg-preview_uizizr.png', -- << Giữ lại default URL
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_displaycategory_category` (`category_id`), -- Mỗi category chỉ nên được display một lần với config này
    CONSTRAINT `FK_displaycategory_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Banners for homepage
CREATE TABLE `banners` (
                           `id` varchar(36) NOT NULL,
    `image_url` varchar(255) NOT NULL,
    `redirect_url` varchar(255) DEFAULT NULL,
    `title` varchar(100) DEFAULT NULL,
    `description` text,
    `is_active` tinyint(1) DEFAULT '1',
    `display_order` int DEFAULT '0',
    `start_date` timestamp NULL DEFAULT NULL,
    `end_date` timestamp NULL DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;