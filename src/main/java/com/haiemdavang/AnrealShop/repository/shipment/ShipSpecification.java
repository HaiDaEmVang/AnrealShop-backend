package com.haiemdavang.AnrealShop.repository.shipment;

import com.haiemdavang.AnrealShop.dto.shipping.search.PreparingStatus;
import com.haiemdavang.AnrealShop.dto.shipping.search.SearchTypeShipping;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.ShippingStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShipSpecification {

    public static Specification<Shipping> filter(
            String shopId,
            LocalDateTime fromTime,
            LocalDateTime toTime,
            String search,
            SearchTypeShipping searchTypeShipping,
            PreparingStatus preparingStatus) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            assert query != null;
            query.distinct(true);
            Join<ShopOrder, OrderItem> orderItemJoin = root.join("orderItems", JoinType.INNER);;

            if (StringUtils.hasText(shopId)) {
                predicates.add(cb.like(cb.lower(root.get("addressFrom").get("shop").get("id")), "%" + shopId.toLowerCase() + "%"));
            }
            if (toTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toTime));
            }

            if (fromTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromTime));
            }
            if(StringUtils.hasText(search)) {
                if (searchTypeShipping == SearchTypeShipping.SHIPPING_CODE) {
                    predicates.add(cb.like(cb.lower(root.get("id")), "%" + search.toLowerCase() + "%"));
                } else if (searchTypeShipping == SearchTypeShipping.CUSTOMER_NAME) {
                    predicates.add(cb.like(cb.lower(root.get("addressTo").get("user").get("fullName")), "%" + search.toLowerCase() + "%"));
                } else if (searchTypeShipping == SearchTypeShipping.ORDER_CODE) {
                    if (orderItemJoin != null) {
                        Join<OrderItem, ShopOrder> shopOrderJoin = orderItemJoin.join("shopOrder", JoinType.INNER);
                        predicates.add(cb.like(cb.lower(shopOrderJoin.get("id")), "%" + search.toLowerCase() + "%"));
                    }
                }
            }

            if(preparingStatus.equals(PreparingStatus.PICK_UP)) {
                predicates.add(cb.equal(root.get("status"), ShippingStatus.PICKED_UP));
            } else if(preparingStatus.equals(PreparingStatus.WAITING_FOR_PICKUP)) {
                predicates.add(cb.equal(root.get("status"), ShippingStatus.WAITING_FOR_PICKUP));
            }



            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
