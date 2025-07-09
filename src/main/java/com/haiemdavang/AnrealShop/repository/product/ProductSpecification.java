package com.haiemdavang.AnrealShop.repository.product;

import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.enums.RestrictStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> myShopFilter(
            String search,
            String categoryId,
            String shopId,
            RestrictStatus restrictStatus
    ){
        return filter(search, categoryId, shopId, restrictStatus, null, null, null, null, null);
    }

    public static Specification<Product> filter(
            String search,
            String categoryId,
            String shopId,
            RestrictStatus restrictStatus,
            Boolean visible,
            Long priceFrom,
            Long priceTo,
            LocalDateTime createdFrom,
            LocalDateTime createdTo
    ) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(categoryId)) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (StringUtils.hasText(shopId)) {
                predicates.add(cb.equal(root.get("shop").get("id"), shopId));
            }

            if (restrictStatus != null && restrictStatus != RestrictStatus.ALL) {
                predicates.add(cb.equal(root.get("restrictStatus"), restrictStatus));
            }

            if (visible != null) {
                predicates.add(cb.equal(root.get("visible"), visible));
            }

            if (priceFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceFrom));
            }

            if (priceTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceTo));
            }

            if (createdFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
            }

            if (createdTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
