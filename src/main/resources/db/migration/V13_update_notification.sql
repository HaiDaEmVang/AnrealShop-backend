#
# CREATE TABLE shop_notifications (
#                                     id VARCHAR(36) NOT NULL,
#                                     content TEXT NOT NULL,
#                                     created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
#                                     is_read TINYINT(1) DEFAULT '0',
#                                     redirect_url VARCHAR(255) DEFAULT NULL,
#                                     shop_id VARCHAR(36) NOT NULL,
#                                     PRIMARY KEY (id),
#                                     KEY fk_shop_notifications_shop_id (shop_id),
#                                     CONSTRAINT fk_shop_notifications_shop_id FOREIGN KEY (shop_id) REFERENCES shops (id)
# );
#
# ALTER TABLE notifications
#     DROP COLUMN scope;
#
# ALTER TABLE notifications
#     CHANGE COLUMN receiver_id user_id VARCHAR(36) NOT NULL;
#
#
# ALTER TABLE notifications
#     RENAME TO user_notifications;

# ALTER TABLE user_notifications
#     DROP COLUMN item_count;

# ALTER TABLE user_notifications
#     DROP COLUMN updated_at;

# alter table user_notifications
#     add column updated_at TIMESTAMP NULL;
#
# alter table shop_notifications
#     add column updated_at TIMESTAMP NULL,
#     add column thumbnail_url VARCHAR(255) DEFAULT 'https://res.cloudinary.com/dlcjc36ow/image/upload/v1747916255/ImagError_jsv7hr.png';

#  alter table shop_orders
#      add column updated_at TIMESTAMP NULL;


