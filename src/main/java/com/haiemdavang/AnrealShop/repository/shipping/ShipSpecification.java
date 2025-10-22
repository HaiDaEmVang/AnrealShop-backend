package com.haiemdavang.AnrealShop.repository.shipping;

import com.haiemdavang.AnrealShop.dto.shipping.search.PreparingStatus;
import com.haiemdavang.AnrealShop.dto.shipping.search.SearchTypeShipping;
import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.enums.ShippingStatus;
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

            if (StringUtils.hasText(shopId)) {
                predicates.add(cb.like(cb.lower(root.get("shopOrder").get("shop").get("id")), "%" + shopId.toLowerCase() + "%"));
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
                    predicates.add(cb.like(cb.lower(root.get("shopOrder").get("id")), "%" + search.toLowerCase() + "%"));
                }
            }

            if(preparingStatus.equals(PreparingStatus.PICK_UP)) {
                predicates.add(cb.notEqual(root.get("status"), ShippingStatus.ORDER_CREATED));
            } else if(preparingStatus.equals(PreparingStatus.WAITING_FOR_PICKUP)) {
                predicates.add(cb.equal(root.get("status"), ShippingStatus.ORDER_CREATED));
            }
            predicates.add(cb.notEqual(root.get("status"), ShippingStatus.DELIVERED));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
