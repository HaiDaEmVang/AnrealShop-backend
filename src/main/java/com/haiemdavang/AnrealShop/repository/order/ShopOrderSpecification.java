package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShopOrderSpecification {

    public static Specification<ShopOrder> filter(
            String shopId,
            LocalDateTime fromTime,
            LocalDateTime toTime,
            String orderCode,
            String customerName,
            String productName,
            String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            assert query != null;
            query.distinct(true);

            if (StringUtils.hasText(shopId)) {
                predicates.add(cb.like(cb.lower(root.get("shop").get("id")), "%" + shopId.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(orderCode)) {
                predicates.add(cb.like(cb.lower(root.get("id")), "%" + orderCode.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(status) && (!status.equalsIgnoreCase("ALL"))) {
                ShopOrderStatus orderItemStatus = ShopOrderStatus.valueOf(status.toUpperCase());
//                if (!orderItemStatus.equals(ShopOrderStatus.CLOSED)
                predicates.add(cb.equal(root.get("status"), orderItemStatus));
            }
            if (StringUtils.hasText(customerName)) {
                predicates.add(cb.like(cb.lower(root.get("user").get("fullName")), "%" + customerName.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(productName)) {
                Join<ShopOrder, OrderItem> productSkuJoin = root.join("orderItems", JoinType.INNER);
                Join<OrderItem, ProductSku> orderItemProductSkuJoin = productSkuJoin.join("productSku", JoinType.INNER);
                Join<ProductSku, Product> productJoin = orderItemProductSkuJoin.join("product", JoinType.INNER);
                predicates.add(cb.like(cb.lower(productJoin.get("name")), "%" + productName.toLowerCase() + "%"));
            }
            if (toTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toTime));
            }

            if (fromTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromTime));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
