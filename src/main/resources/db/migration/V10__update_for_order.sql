-- Chú ý: Hãy sao lưu cơ sở dữ liệu hiện tại trước khi chạy các lệnh này.
-- Mở transaction để đảm bảo tính toàn vẹn dữ liệu
START TRANSACTION;

-- -----------------------------------------------------------------------------
-- 1. Cập nhật bảng `payments`
-- -----------------------------------------------------------------------------

-- Thêm 'MOMO' vào kiểu ENUM của cột `getway`
ALTER TABLE `payments` MODIFY COLUMN `gateway` ENUM('VNPAY','CASH_ON_DELIVERY','MOMO') DEFAULT NULL;

-- Cập nhật kiểu ENUM của cột `status` để nhất quán hơn.
-- Các trạng thái được đổi tên và sắp xếp lại để rõ ràng hơn.
ALTER TABLE `payments` MODIFY COLUMN `status` ENUM('PENDING', 'COMPLETED', 'CANCELED', 'EXPIRED', 'REFUNDED', 'FAILED') NOT NULL DEFAULT 'PENDING';

-- -----------------------------------------------------------------------------
-- 2. Cập nhật bảng `orders`
-- -----------------------------------------------------------------------------

-- Thay đổi kiểu ENUM của cột `status` để phản ánh trạng thái tổng quát hơn của đơn hàng.
ALTER TABLE `orders` MODIFY COLUMN `status` ENUM('PROCESSING','COMPLETED','CANCELED') NOT NULL;

-- Đổi tên cột `shipping_fee` thành `total_shipping_fee` để rõ nghĩa hơn.
ALTER TABLE `orders` CHANGE `shipping_fee` `total_shipping_fee` BIGINT DEFAULT NULL;


-- -----------------------------------------------------------------------------
-- 3. Cập nhật bảng `shop_orders`
-- -----------------------------------------------------------------------------

-- Thay đổi kiểu ENUM của cột `status` để bao gồm các trạng thái chi tiết của shop.
ALTER TABLE `shop_orders` MODIFY COLUMN `status` ENUM('PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'AWAITING_SHIPMENT', 'IN_TRANSIT', 'OUT_FOR_DELIVERY', 'DELIVERED', 'REFUND', 'CANCELED') NOT NULL;

-- Đổi tên cột `total` thành `total_amount`.
ALTER TABLE `shop_orders` CHANGE `total` `total_amount` BIGINT NOT NULL;


-- -----------------------------------------------------------------------------
-- 4. Cập nhật bảng `order_items`
-- -----------------------------------------------------------------------------

-- Thêm cột `shop_order_id` để liên kết trực tiếp với `shop_orders` và thiết lập khóa ngoại.
ALTER TABLE `order_items` ADD COLUMN `shop_order_id` VARCHAR(36) NULL;
ALTER TABLE `order_items` ADD CONSTRAINT `fk_order_items_shop_order_id` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`);

-- Cập nhật kiểu ENUM của cột `status` để theo dõi trạng thái từng sản phẩm.
ALTER TABLE `order_items` ADD COLUMN `status` ENUM('PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'AWAITING_SHIPMENT', 'IN_TRANSIT', 'OUT_FOR_DELIVERY', 'DELIVERED', 'REFUND', 'CANCELED') NOT NULL DEFAULT 'PROCESSING';

-- Cập nhật dữ liệu cho cột `shop_order_id` mới dựa trên mối quan hệ cũ.
-- Lệnh này cần được tùy chỉnh dựa trên logic ứng dụng của bạn để xác định `shop_order_id` chính xác.
-- Ví dụ:
-- UPDATE `order_items` oi
-- JOIN `shop_order_items` soi ON oi.id = soi.order_item_id
-- SET oi.shop_order_id = soi.shop_order_id;



-- -----------------------------------------------------------------------------
-- 5. Xóa bảng cũ không còn cần thiết
-- -----------------------------------------------------------------------------

-- Xóa bảng liên kết trung gian `shop_order_items`.
DROP TABLE `shop_order_items`;

-- -----------------------------------------------------------------------------
-- 6. Tạo các bảng mới
-- -----------------------------------------------------------------------------

-- Bảng để theo dõi lịch sử trạng thái của từng mặt hàng trong đơn hàng.
CREATE TABLE `order_item_tracks` (
                                     `status` ENUM('PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'AWAITING_SHIPMENT', 'IN_TRANSIT', 'OUT_FOR_DELIVERY', 'DELIVERED', 'REFUND', 'CANCELED') NOT NULL,
                                     `updated_at` DATETIME(6) NOT NULL,
                                     `order_item_id` VARCHAR(36) NOT NULL,
                                     PRIMARY KEY (`order_item_id`),
                                     CONSTRAINT `fk_order_item_tracks_order_item_id` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`)
);



-- Bảng để lưu phí vận chuyển cho từng đơn hàng của shop.
CREATE TABLE `shop_order_shipping_fees` (
                                            `shop_order_id` VARCHAR(36) NOT NULL,
                                            `amount` BIGINT NOT NULL,
                                            PRIMARY KEY (`shop_order_id`),
                                            CONSTRAINT `fk_shop_order_shipping_fees_shop_order_id` FOREIGN KEY (`shop_order_id`) REFERENCES `shop_orders` (`id`)
);

-- Kết thúc transaction.
COMMIT;