alter table shop_orders
    drop column cancel_reason,
    drop column canceled_by;

alter table order_items
    add column cancel_reason nvarchar(255),
    add column canceled_by enum('CUSTOMER','SHOP') DEFAULT NULL;

alter table orders
    modify column status enum('PROCESSING', 'SUCCESS', 'CANCELED') not null;
alter table shop_orders
    modify column status enum('INIT_PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'SHIPPING', 'DELIVERED', 'CLOSED') not null;
