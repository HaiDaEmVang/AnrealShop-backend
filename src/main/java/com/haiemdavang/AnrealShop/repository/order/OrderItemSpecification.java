package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderItemSpecification {

    public static Specification<OrderItem> filter(
            Set<String> shopOrderIds,
            String productName,
            String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            assert query != null;
            query.distinct(true);

            if (shopOrderIds != null && !shopOrderIds.isEmpty()) {
                predicates.add(root.get("shopOrder").get("id").in(shopOrderIds));
            }

            if (StringUtils.hasText(productName)) {
                Join<OrderItem, ProductSku> productSkuJoin = root.join("productSku", JoinType.INNER);
                Join<ProductSku, Product> productJoin = productSkuJoin.join("product", JoinType.INNER);
                predicates.add(cb.like(cb.lower(productJoin.get("name")), "%" + productName.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(status) && !status.equalsIgnoreCase(ShopOrderStatus.CLOSED.name())) {
                if (!status.equalsIgnoreCase("ALL")) {
                    predicates.add(root.get("cancelReason").isNull());
                    predicates.add(root.get("canceledBy").isNull());
                }
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
