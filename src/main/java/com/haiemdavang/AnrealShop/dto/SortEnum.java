package com.haiemdavang.AnrealShop.dto;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum SortEnum {
    NAME_ASC("name-asc", Sort.by(Sort.Direction.ASC, "name")),
    NAME_DESC("name-desc", Sort.by(Sort.Direction.DESC, "name")),
    PRICE_ASC("price-asc", Sort.by(Sort.Direction.ASC, "price")),
    PRICE_DESC("price-desc", Sort.by(Sort.Direction.DESC, "price")),
    BESTSELLER("bestseller", Sort.by(Sort.Direction.DESC, "sold")),
    STOCK_ASC("stock-asc", Sort.by(Sort.Direction.ASC, "quantity")),
    STOCK_DESC("stock-desc", Sort.by(Sort.Direction.DESC, "quantity")),
    NEWEST("newest", Sort.by(Sort.Direction.DESC, "createdAt")),
    OLDEST("oldest", Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATED_AT_ASC("createdAt-asc", Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATED_AT_DESC("createdAt-desc", Sort.by(Sort.Direction.DESC, "createdAt")),
    UPDATE_AT_DESC("updateAt-desc", Sort.by(Sort.Direction.DESC, "createdAt")),
    UNSORTED("default", Sort.unsorted());

    private final String value;
    private final Sort sort;

    SortEnum(String value, Sort sort) {
        this.value = value;
        this.sort = sort;
    }

    public static SortEnum fromValue(String value) {
        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.value.equals(value)) {
                return sortEnum;
            }
        }
        return UNSORTED;
    }
}