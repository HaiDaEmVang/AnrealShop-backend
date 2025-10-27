-- drop table cart_item_attribute
drop table order_item_attributes;

-- delete columns product_id
alter table `order_items`
        drop constraint fk_order_items_product_id;
alter table `order_items`
        drop column product_id;

-- add columns product_sku
alter table `order_items`
        add column product_sku_id varchar(36) not null;

alter table `order_items`
        add constraint order_items_product_sku_id foreign key (product_sku_id) references product_skus (id);



