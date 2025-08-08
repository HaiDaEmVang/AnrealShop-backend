package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GHNResponse<T> {
    public int code;
    public String message;
    public T data;
}
