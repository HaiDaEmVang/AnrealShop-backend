package com.haiemdavang.AnrealShop.utils;

import org.springframework.data.domain.Sort;

import java.text.Normalizer;

public class ApplicationInitHelper {

    public static String toSlug(String combined) {
        String noDiacritics = Normalizer.normalize(combined, Normalizer.Form.NFD);
        noDiacritics = noDiacritics.replaceAll("\\p{M}", "");
        String slug = noDiacritics
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        slug += "-" + System.currentTimeMillis();
        return slug.substring(0, Math.min(slug.length(), 50));
    }

    public static Sort getSortBy(String sortBy) {
        return switch (sortBy) {
            case "name-asc" -> Sort.by(Sort.Direction.ASC, "name");
            case "name-desc" -> Sort.by(Sort.Direction.DESC, "name");
            case "price-asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price-desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "bestseller" -> Sort.by(Sort.Direction.DESC, "sold");
            case "stock-asc" -> Sort.by(Sort.Direction.ASC, "quantity");
            case "stock-desc" -> Sort.by(Sort.Direction.DESC, "quantity");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "createdAt-asc" -> Sort.by(Sort.Direction.ASC, "createdAt");
            case "createdAt-desc" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.unsorted();
        };
    }
}
