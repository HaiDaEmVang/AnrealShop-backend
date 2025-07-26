CREATE TABLE outbox_messages (
                                 id varchar(36) PRIMARY KEY,
                                 aggregate_type VARCHAR(255),     -- e.g., "Product"
                                 aggregate_id VARCHAR(255),       -- e.g., productId
                                 type VARCHAR(255),               -- e.g., "PRODUCT_CREATED"
                                 payload JSON,                    -- Thông tin DTO
                                 created_at TIMESTAMP,
                                 processed BOOLEAN DEFAULT FALSE
);