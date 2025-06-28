package com.haiemdavang.AnrealShop.utils;

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
}
