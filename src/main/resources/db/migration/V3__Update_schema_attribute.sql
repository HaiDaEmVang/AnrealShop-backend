
SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE attribute_keys
    DROP FOREIGN KEY FKsik07cug4lf4y8v8c51lpd11q;

ALTER TABLE sku_attributes
    DROP FOREIGN KEY FKqbgvbbnbx9jgwpkf0rk4xn7kg;

ALTER TABLE order_item_attributes
    DROP FOREIGN KEY FKpd3snx3p0tn43cfmu48if945v;

ALTER TABLE cart_item_attributes
    DROP FOREIGN KEY FKt76yq7lrg7l8jkx4vj7o80fia;


ALTER TABLE attribute_keys
    DROP COLUMN  key_name,
    DROP COLUMN  allow_custom_values,
    DROP COLUMN  shop_id;

ALTER TABLE attribute_keys
    ADD COLUMN slug_name VARCHAR(50) NOT NULL UNIQUE,
    ADD COLUMN is_default BOOLEAN DEFAULT FALSE;

CREATE TABLE shop_attribute_keys
(
    shop_id            VARCHAR(36) NOT NULL,
    attribute_key_id   VARCHAR(36) NOT NULL,
    PRIMARY KEY (shop_id, attribute_key_id),
    FOREIGN KEY (shop_id) REFERENCES shops (id) ON DELETE CASCADE,
    FOREIGN KEY (attribute_key_id) REFERENCES attribute_keys (id) ON DELETE CASCADE
);

ALTER TABLE attribute_values
    DROP FOREIGN KEY FKhgo27y94xcykg49kk7xfa0of8;

ALTER TABLE attribute_values
    DROP FOREIGN KEY FK20mh4e6em7g8r0ahsr9kvxgc1;

ALTER TABLE attribute_values
    DROP COLUMN  shop_id;

ALTER TABLE attribute_values
    ADD COLUMN is_default BOOLEAN DEFAULT FALSE;

ALTER TABLE attribute_values
    ADD CONSTRAINT uk_attrvalue_key_value UNIQUE (attribute_key_id, value);

SET FOREIGN_KEY_CHECKS = 1;
