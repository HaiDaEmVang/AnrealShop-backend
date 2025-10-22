package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.dto.order.search.ModeType;
import com.haiemdavang.AnrealShop.dto.order.search.OrderCountType;
import com.haiemdavang.AnrealShop.dto.order.search.PreparingStatus;
import com.haiemdavang.AnrealShop.dto.order.search.SearchType;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
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
            if (query.getResultType() != Long.class) {
                root.fetch("user", JoinType.LEFT);
                Fetch<ShopOrder, Order> orderFetch = root.fetch("order", JoinType.LEFT);
                orderFetch.fetch("payment", JoinType.LEFT);
                root.fetch("shop", JoinType.LEFT);
                root.fetch("shipping", JoinType.LEFT);
            }

            if (StringUtils.hasText(shopId)) {
                predicates.add(cb.equal(root.get("shop").get("id"), shopId));
            }

            if (preparingStatus == PreparingStatus.PREPARING) {
                predicates.add(cb.equal(root.get("status"), ShopOrderStatus.PREPARING));
            } else if (preparingStatus == PreparingStatus.CONFIRMED) {
                predicates.add(cb.equal(root.get("status"), ShopOrderStatus.CONFIRMED));
            }

            if (mode == ModeType.HOME) {
                if (StringUtils.hasText(status) && (!status.equalsIgnoreCase("ALL"))) {
                    ShopOrderStatus orderItemStatus = ShopOrderStatus.valueOf(status.toUpperCase());
                    if (orderItemStatus.equals(ShopOrderStatus.PREPARING))
                        predicates.add(root.get("status").in(ShopOrderStatus.PREPARING, ShopOrderStatus.CONFIRMED));
                    else
                        predicates.add(cb.equal(root.get("status"), orderItemStatus));

                    if(orderItemStatus.equals(ShopOrderStatus.CLOSED)) {
                        Subquery<String> subquery = query.subquery(String.class);
                        Root<OrderItem> orderItemRoot = subquery.from(OrderItem.class);
                        subquery.select(orderItemRoot.get("shopOrder").get("id"))
                                .where(cb.and(
                                        cb.isNotNull(orderItemRoot.get("cancelReason")),
                                        cb.isNotNull(orderItemRoot.get("canceledBy")),
                                        cb.equal(orderItemRoot.get("shopOrder").get("id"), root.get("id"))
                                ));
                        predicates.add(cb.exists(subquery));
                    }
                }
                if (toTime != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toTime));
                }

                if (fromTime != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromTime));
                }
            }
            if (mode == ModeType.SHIPPING) {
                List<ShopOrderStatus> shopOrderStatus = List.of(
                        ShopOrderStatus.PREPARING,
                        ShopOrderStatus.CONFIRMED
                );
                predicates.add(root.get("status").in(shopOrderStatus));

                Subquery<String> subquery1 = query.subquery(String.class);
                Root<OrderItem> orderItemRoot = subquery1.from(OrderItem.class);
                subquery1.select(orderItemRoot.get("shopOrder").get("id"))
                        .where(cb.and(
                                cb.isNull(orderItemRoot.get("cancelReason")),
                                cb.isNull(orderItemRoot.get("canceledBy")),
                                cb.equal(orderItemRoot.get("shopOrder").get("id"), root.get("id"))
                        ));
                predicates.add(cb.exists(subquery1));

                if (confirmSD != null && confirmED != null) {
                    Subquery<String> subquery = query.subquery(String.class);
                    Root<ShopOrderTrack> shopOrderTrackRoot = subquery.from(ShopOrderTrack.class);
                    subquery.select(shopOrderTrackRoot.get("shopOrder").get("id"))
                            .where(cb.and(
                                    cb.equal(shopOrderTrackRoot.get("shopOrder"), root),
                                    cb.equal(shopOrderTrackRoot.get("status"), ShopOrderStatus.CONFIRMED),
                                    cb.greaterThanOrEqualTo(shopOrderTrackRoot.get("id").get("updatedAt"), confirmSD),
                                    cb.lessThanOrEqualTo(shopOrderTrackRoot.get("id").get("updatedAt"), confirmED)
                            ));
                    predicates.add(cb.exists(subquery));
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

            }
            ShopOrderSpecification.filterSearch(predicates, query, cb, root, search, searchType);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ShopOrder> filter(String shopId, ModeType modeType, LocalDateTime localDateTime, LocalDateTime now, String search, SearchType searchType) {
        return ShopOrderSpecification.filter(shopId, modeType, localDateTime, now, null, search, searchType, null, null, null, null);
    }

//    public static Specification<ShopOrder> filter(Set<String> shippingIds, String search, SearchTypeShipping searchTypeShipping) {
//        return (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            assert query != null;
//            query.distinct(true);
//
//            Join<ShopOrder, OrderItem> orderItemJoin = root.join("orderItems", JoinType.INNER);
//            Join<OrderItem, Shipping> shippingJoin = orderItemJoin.join("shippings", JoinType.INNER);
//
//            if (shippingIds != null && !shippingIds.isEmpty()) {
//                predicates.add(shippingJoin.get("id").in(shippingIds));
//            }
//
//            if (StringUtils.hasText(search)) {
//                String lowerCaseSearch = "%" + search.toLowerCase() + "%";
//                if (searchTypeShipping == SearchTypeShipping.ORDER_CODE) {
//                    predicates.add(cb.like(cb.lower(root.get("id")), lowerCaseSearch));
//                } else if (searchTypeShipping == SearchTypeShipping.CUSTOMER_NAME) {
//                    predicates.add(cb.like(cb.lower(root.get("user").get("fullName")), lowerCaseSearch));
//                } else if (searchTypeShipping == SearchTypeShipping.SHIPPING_CODE) {
//                    predicates.add(cb.like(cb.lower(shippingJoin.get("id").get("id")), lowerCaseSearch));
//                }
//            }
//
//            if (predicates.isEmpty()) {
//                return cb.conjunction();
//            }
//
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//    }

    public static void filterSearch(List<Predicate> predicates, CriteriaQuery<?> query, CriteriaBuilder cb, Root<ShopOrder> root, String search, SearchType searchType) {
        if(StringUtils.hasText(search)) {
            if (searchType == SearchType.ORDER_CODE) {
                predicates.add(cb.like(cb.lower(root.get("id")), "%" + search.toLowerCase() + "%"));
            } else if (searchType == SearchType.CUSTOMER_NAME) {
                predicates.add(cb.like(cb.lower(root.get("user").get("fullName")), "%" + search.toLowerCase() + "%"));
            } else if (searchType == SearchType.PRODUCT_NAME) {
                Subquery<String> subquery = query.subquery(String.class);
                Root<OrderItem> orderItemRoot = subquery.from(OrderItem.class);
                subquery.select(orderItemRoot.get("shopOrder").get("id"))
                        .where(cb.like(cb.lower(orderItemRoot.get("productSku").get("product").get("name")), "%" + search.toLowerCase() + "%"));
                predicates.add(root.get("id").in(subquery));
            }
        }
    }

    public static Specification<ShopOrder> filter(String userId, String status, String search, SearchType searchType) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            assert query != null;
            query.distinct(true);
            Join<ShopOrder, OrderItem> orderItemJoin = root.join("orderItems", JoinType.INNER);

            ShopOrderStatus statusCompare;
            try {
                if (status.equalsIgnoreCase(OrderTrackStatus.CANCELED.name()) || status.equalsIgnoreCase(OrderTrackStatus.REFUND.name())){
                    statusCompare = ShopOrderStatus.CLOSED;
                }else statusCompare = ShopOrderStatus.valueOf(status);
            } catch (Exception e){
                throw new BadRequestException("INVALID_STATUS");
            }

            predicates.add(cb.equal(root.get("status"), statusCompare));

            if (statusCompare == ShopOrderStatus.CLOSED) {
                if (status.equalsIgnoreCase(OrderTrackStatus.CANCELED.name())) {
                    predicates.add(cb.equal(orderItemJoin.get("status"), OrderTrackStatus.CANCELED));
                } else if ( status.equalsIgnoreCase(OrderTrackStatus.REFUND.name())) {
                    predicates.add(cb.equal(orderItemJoin.get("status"), OrderTrackStatus.REFUND));
                }
            }

            if (StringUtils.hasText(userId)) {
                predicates.add(cb.like(cb.lower(root.get("order").get("user").get("id")), "%" + userId.toLowerCase() + "%"));
            }

            ShopOrderSpecification.filterSearch(predicates, query, cb, root, search, searchType);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
