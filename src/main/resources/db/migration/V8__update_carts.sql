-- drop table cart_item_attribute
drop table cart_item_attributes;

-- delete columns product_id
alter table `cart_items`
    drop constraint fk_cart_items_product_id;
alter table `cart_items`
    drop column product_id;

-- add columns product_sku
alter table `cart_items`
    add column product_sku_id varchar(36) not null;

alter table `cart_items`
    add constraint cart_product_sku_id foreign key (product_sku_id) references product_skus (id);
