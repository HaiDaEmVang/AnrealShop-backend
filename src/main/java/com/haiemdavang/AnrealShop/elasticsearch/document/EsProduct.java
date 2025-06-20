package com.haiemdavang.AnrealShop.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "products", createIndex = false)
@Setting(shards = 1)
public class EsProduct {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    private String name;

    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    private String description;

    @Field(type = FieldType.Search_As_You_Type, analyzer = "vietnamese_analyzer")
    private String suggest;

    @Field(type = FieldType.Long)
    private Long price;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Integer)
    private Integer soldCount;

    @Field(type = FieldType.Half_Float)
    private Float averageRating;

    @Field(type = FieldType.Keyword)
    private String thumbnailUrl;

    @Field(type = FieldType.Object)
    private EsShop shop;

    @Field(type = FieldType.Object)
    private EsCategory category;

    @Field(type = FieldType.Nested)
    private List<EsAttribute> attributes;

}
