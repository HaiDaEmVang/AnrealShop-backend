package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.dto.order.search.ModeType;
import com.haiemdavang.AnrealShop.dto.order.search.OrderCountType;
import com.haiemdavang.AnrealShop.dto.order.search.SearchType;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderItemSpecification {

    public static Specification<OrderItem> filter(
            ModeType  mode,
            List<String> shopOrderIds,
            String productName,
            SearchType searchType,
            String status,
            OrderCountType orderType) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            assert query != null;
            query.distinct(true);

            if (shopOrderIds != null && !shopOrderIds.isEmpty()) {
                predicates.add(root.get("shopOrder").get("id").in(shopOrderIds));
            }
            if (searchType.equals(SearchType.PRODUCT_NAME) && StringUtils.hasText(productName)){
                Join<OrderItem, ProductSku> productSkuJoin = root.join("productSku", JoinType.INNER);
                Join<ProductSku, Product> productJoin = productSkuJoin.join("product", JoinType.INNER);
                predicates.add(cb.like(cb.lower(productJoin.get("name")), "%" + productName.toLowerCase() + "%"));
            }

            if (mode == ModeType.USER && StringUtils.hasText(status)) {
                if (status.equalsIgnoreCase(OrderTrackStatus.CANCELED.name())) {
                    predicates.add(cb.equal(root.get("status"), OrderTrackStatus.CANCELED));
                } else if (status.equalsIgnoreCase(OrderTrackStatus.REFUND.name())) {
                    predicates.add(cb.equal(root.get("status"), OrderTrackStatus.REFUND));
                } else if (!status.equalsIgnoreCase(OrderTrackStatus.CANCELED.name()) && !status.equalsIgnoreCase(OrderTrackStatus.REFUND.name())) {
                    predicates.add(root.get("cancelReason").isNull());
                    predicates.add(root.get("canceledBy").isNull());
                }
            }

            if (mode == ModeType.HOME) {
                if (StringUtils.hasText(status) && !status.equalsIgnoreCase("ALL")) {
                    if (!status.equalsIgnoreCase(ShopOrderStatus.CLOSED.name())){
                        predicates.add(root.get("cancelReason").isNull());
                        predicates.add(root.get("canceledBy").isNull());
                    }else {
                        predicates.add(root.get("cancelReason").isNotNull());
                        predicates.add(root.get("canceledBy").isNotNull());
                    }
                }
            }


            if (mode == ModeType.SHIPPING) {

                predicates.add(root.get("cancelReason").isNull());
                predicates.add(root.get("canceledBy").isNull());

                if (orderType.equals(OrderCountType.ONE) || orderType.equals(OrderCountType.MORE)) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<OrderItem> itemCounter = subquery.from(OrderItem.class);
                    subquery.select(cb.count(itemCounter.get("id")));
                    subquery.where(cb.and(
                            cb.equal(itemCounter.get("shopOrder"), root.get("shopOrder")),
                            cb.equal(itemCounter.get("status"), OrderTrackStatus.PREPARING)
                    ));

                    if (orderType.equals(OrderCountType.ONE)) {
                        predicates.add(cb.equal(subquery, 1L));
                    } else {
                        predicates.add(cb.greaterThan(subquery, 1L));
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<OrderItem> filter(List<String> idShopOrders, String search, SearchType searchType, String status) {
        return OrderItemSpecification.filter(ModeType.USER, idShopOrders, search, searchType, status, null);
    }
}
