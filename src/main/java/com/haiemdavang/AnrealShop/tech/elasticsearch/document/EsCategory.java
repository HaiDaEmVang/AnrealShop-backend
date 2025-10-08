package com.haiemdavang.AnrealShop.tech.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(indexName = "categories", createIndex = false)
public class EsCategory {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    private String name;
    @Field(type = FieldType.Text, name = "url_path", analyzer = "vietnamese_analyzer")
    private String urlPath;
    @Field(name = "url_slug", type = FieldType.Keyword)
    private String urlSlug;
}
