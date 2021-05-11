/**
 * @Author jzfeng
 * @Date 5/9/21-8:19 AM
 */

package com.leyou.search.pojo;

import com.leyou.item.pojo.Sku;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Document(indexName = "goods")
public class Goods {
    @Id
    private Long id; //spuId

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; //标题， 分类，品牌

    @Field(type = FieldType.Keyword,index = false)
    private String subTitle;

    private Long cid1;
    private Long cid2;
    private Long cid3;

    private Long brandId;

    private Date createdTime;

    private List<Long> price;


    private List<Sku> skus;

    private Map<String, Object> specs;

}
