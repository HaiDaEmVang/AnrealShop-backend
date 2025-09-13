package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.dto.order.search.ModeType;
import com.haiemdavang.AnrealShop.dto.order.search.OrderCountType;
import com.haiemdavang.AnrealShop.dto.order.search.PreparingStatus;
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
import java.util.Set;

public class OrderItemSpecification {

    public static Specification<OrderItem> filter(
            ModeType  mode,
            Set<String> shopOrderIds,
            String productName,
            SearchType searchType,
            String status,
//            LocalDateTime confirmSD,
//            LocalDateTime confirmED,
            OrderCountType orderType,
            PreparingStatus preparingStatus) {
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

            if (mode == ModeType.HOME) {
                if (StringUtils.hasText(status) && !status.equalsIgnoreCase(ShopOrderStatus.CLOSED.name())) {
                    if (!status.equalsIgnoreCase("ALL")) {
                        predicates.add(root.get("cancelReason").isNull());
                        predicates.add(root.get("canceledBy").isNull());

                    }
                }
            }


            if (mode == ModeType.SHIPPING) {
                List<OrderTrackStatus> orderItemStatuses = List.of(
                        OrderTrackStatus.PREPARING,
                        OrderTrackStatus.WAIT_SHIPMENT
                );
                predicates.add(root.get("status").in(orderItemStatuses));

                if (orderType.equals(OrderCountType.ONE)){
                    Subquery<String> subquery = query.subquery(String.class);
                    Root<OrderItem> itemRoot = subquery.from(OrderItem.class);
                    subquery.select(itemRoot.get("id"))
                            .where(cb.equal(itemRoot.get("status"), OrderTrackStatus.PREPARING))
                            .groupBy(itemRoot.get("id"))
                            .having(cb.equal(cb.count(itemRoot.get("id")), 1L));
                    predicates.add(cb.in(root.get("id")).value(subquery));
                } else if (orderType.equals(OrderCountType.MORE)){
                    Subquery<String> subquery = query.subquery(String.class);
                    Root<OrderItem> itemRoot = subquery.from(OrderItem.class);
                    subquery.select(itemRoot.get("id"))
                            .where(cb.equal(itemRoot.get("status"), OrderTrackStatus.PREPARING))
                            .groupBy(itemRoot.get("id"))
                            .having(cb.greaterThan(cb.count(itemRoot.get("id")), 1L));
                    predicates.add(cb.in(root.get("id")).value(subquery));
                }
            }


            if (preparingStatus == PreparingStatus.PREPARING) {
                predicates.add(cb.equal(root.get("status"), OrderTrackStatus.PREPARING));
            } else if (preparingStatus == PreparingStatus.WAIT_SHIPMENT) {
                predicates.add(cb.equal(root.get("status"), OrderTrackStatus.WAIT_SHIPMENT));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
