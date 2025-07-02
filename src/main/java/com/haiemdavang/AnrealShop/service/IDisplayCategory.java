package com.haiemdavang.AnrealShop.service;


import com.haiemdavang.AnrealShop.modal.entity.category.DisplayCategory;
import com.nimbusds.openid.connect.sdk.Display;

public interface IDisplayCategory {
    DisplayCategory save(DisplayCategory displayCategory);
    DisplayCategory findDisplayCategoryByIdCategory(String categoryId);
}
