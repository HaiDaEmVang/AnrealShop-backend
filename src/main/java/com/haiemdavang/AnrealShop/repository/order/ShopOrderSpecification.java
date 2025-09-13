package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.dto.order.search.ModeType;
import com.haiemdavang.AnrealShop.dto.order.search.OrderCountType;
import com.haiemdavang.AnrealShop.dto.order.search.PreparingStatus;
import com.haiemdavang.AnrealShop.dto.order.search.SearchType;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrderTrack;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShopOrderSpecification {

    public static Specification<ShopOrder> filter(
            String shopId,
            ModeType mode,
            LocalDateTime fromTime,
            LocalDateTime toTime,
            String status,
            String search,
            SearchType searchType,
            LocalDateTime confirmSD,
            LocalDateTime confirmED,
            OrderCountType orderType,
            PreparingStatus preparingStatus) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            assert query != null;
            query.distinct(true);
            Join<ShopOrder, OrderItem> orderItemJoin = root.join("orderItems", JoinType.INNER);;

            if (StringUtils.hasText(shopId)) {
                predicates.add(cb.like(cb.lower(root.get("shop").get("id")), "%" + shopId.toLowerCase() + "%"));
            }
            if (mode == ModeType.HOME) {
                if (StringUtils.hasText(status) && (!status.equalsIgnoreCase("ALL"))) {
                    ShopOrderStatus orderItemStatus = ShopOrderStatus.valueOf(status.toUpperCase());
                    predicates.add(cb.equal(root.get("status"), orderItemStatus));
                }
                if (toTime != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toTime));
                }

                if (fromTime != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromTime));
                }
            }
            if (mode == ModeType.SHIPPING) {
                List<OrderTrackStatus> orderItemStatuses = List.of(
                        OrderTrackStatus.PREPARING,
                        OrderTrackStatus.WAIT_SHIPMENT
                );
                if (orderItemJoin != null)
                    predicates.add(orderItemJoin.get("status").in(orderItemStatuses));

                if (confirmSD != null && confirmED != null) {
                    Subquery<LocalDateTime> subquery = query.subquery(LocalDateTime.class);
                    Root<ShopOrderTrack> trackRoot = subquery.from(ShopOrderTrack.class);

                    subquery.select(cb.greatest(trackRoot.get("id").<LocalDateTime>get("updatedAt")))
                            .where(cb.equal(trackRoot.get("shopOrder").get("id"), root.get("id")));

                    Join<ShopOrder, ShopOrderTrack> trackJoin = root.join("trackingHistory");

                    predicates.add(cb.equal(trackJoin.get("id").get("updatedAt"), subquery));
                    predicates.add(cb.equal(trackJoin.get("status"), ShopOrderStatus.PREPARING));
                    predicates.add(cb.greaterThanOrEqualTo(trackJoin.get("id").get("updatedAt"), confirmSD));
                    predicates.add(cb.lessThanOrEqualTo(trackJoin.get("id").get("updatedAt"), confirmED));
                }

                if (orderType.equals(OrderCountType.ONE)){
                    Subquery<String> subquery = query.subquery(String.class);
                    Root<OrderItem> itemRoot = subquery.from(OrderItem.class);
                    subquery.select(itemRoot.get("shopOrder").get("id"))
                            .where(cb.and(cb.equal(itemRoot.get("shopOrder"), root), cb.equal(itemRoot.get("status"), OrderTrackStatus.PREPARING)))
                            .groupBy(itemRoot.get("shopOrder").get("id"))
                            .having(cb.equal(cb.count(itemRoot.get("id")), 1L));
                    predicates.add(cb.in(root.get("id")).value(subquery));
                } else if (orderType.equals(OrderCountType.MORE)){
                    Subquery<String> subquery = query.subquery(String.class);
                    Root<OrderItem> itemRoot = subquery.from(OrderItem.class);
                    subquery.select(itemRoot.get("shopOrder").get("id"))
                            .where(cb.and(cb.equal(itemRoot.get("shopOrder"), root), cb.equal(itemRoot.get("status"), OrderTrackStatus.PREPARING)))
                            .groupBy(itemRoot.get("shopOrder").get("id"))
                            .having(cb.greaterThan(cb.count(itemRoot.get("id")), 1L));
                    predicates.add(cb.in(root.get("id")).value(subquery));
                }

                if (orderItemJoin != null) {
                    if (preparingStatus == PreparingStatus.PREPARING) {
                        predicates.add(cb.equal(orderItemJoin.get("status"), OrderTrackStatus.PREPARING));
                    } else if (preparingStatus == PreparingStatus.WAIT_SHIPMENT) {
                        predicates.add(cb.equal(orderItemJoin.get("status"), OrderTrackStatus.WAIT_SHIPMENT));
                    }
                }

            }
            if(StringUtils.hasText(search)) {
                if (searchType == SearchType.ORDER_CODE) {
                    predicates.add(cb.like(cb.lower(root.get("id")), "%" + search.toLowerCase() + "%"));
                } else if (searchType == SearchType.CUSTOMER_NAME) {
                    predicates.add(cb.like(cb.lower(root.get("user").get("fullName")), "%" + search.toLowerCase() + "%"));
                } else if (searchType == SearchType.PRODUCT_NAME) {
                    if (orderItemJoin != null) {
                        Join<OrderItem, ProductSku> orderItemProductSkuJoin = orderItemJoin.join("productSku", JoinType.INNER);
                        Join<ProductSku, Product> productJoin = orderItemProductSkuJoin.join("product", JoinType.INNER);
                        predicates.add(cb.like(cb.lower(productJoin.get("name")), "%" + search.toLowerCase() + "%"));
                    }
                }
            }




            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ShopOrder> filter(String shopId, ModeType modeType, LocalDateTime localDateTime, LocalDateTime now, String search, SearchType searchType) {
        return ShopOrderSpecification.filter(shopId, modeType, localDateTime, now, null, search, searchType, null, null, null, null);
    }
}
