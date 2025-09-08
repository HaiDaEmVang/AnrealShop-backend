#
# alter table shop_order_tracks
#      modify column status enum('INIT_PROCESSING', 'PENDING_CONFIRMATION', 'PREPARING', 'SHIPPING', 'DELIVERED', 'CLOSED') not null;



# alter table order_item_tracks
#     drop primary key ,
#     add primary key (order_item_id, updated_at);