package com.haiemdavang.AnrealShop.utils;

import com.haiemdavang.AnrealShop.dto.SortEnum;
import org.springframework.data.domain.Sort;

import java.text.Normalizer;

public class ApplicationInitHelper {

    public static String IMAGE_USER_DEFAULT = "https://res.cloudinary.com/dqogp38jb/image/upload/v1750060824/7309681_msx5j1.jpg";

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
        return SortEnum.fromValue(sortBy).getSort();
    }


}
