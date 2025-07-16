alter table product_skus
    modify column sold int default 0;
alter table categories
    modify column url_path text;