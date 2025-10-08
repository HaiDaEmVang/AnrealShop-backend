package com.haiemdavang.AnrealShop.tech.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EsShop {
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer", fielddata = true)
    private String name;
}
