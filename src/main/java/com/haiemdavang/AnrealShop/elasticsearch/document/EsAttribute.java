package com.haiemdavang.AnrealShop.elasticsearch.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EsAttribute {
    @Field(type = FieldType.Keyword)
    private String keyName;

    @Field(type = FieldType.Keyword)
    private String displayName;

    @Field(type = FieldType.Keyword)
    private List<String> value;
}
