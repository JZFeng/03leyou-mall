/**
 * @Author jzfeng
 * @Date 5/10/21-4:53 AM
 */

package com.leyou.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndexService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private BrandClient brandClient;

    public Goods buildGoods(Spu spu) {

        ObjectMapper objectMapper = new ObjectMapper();

        //准备all
        List<String> categoryNames = categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        String title = spu.getTitle();
        String all = title + " " + StringUtils.join(categoryNames," ") + " " + brand.getName();

        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());

        //准备price列表
        List<Long> price = skus.stream().map( sku -> {return sku.getPrice();}).collect(Collectors.toList());

        //准备skus
        List<Map<String, Object>> objectMaps = new ArrayList<>();
        skus.forEach( sku -> {
            Map<String, Object> objectMap = new HashMap<>();

            objectMap.put("id",sku.getId());
            objectMap.put("price", sku.getPrice());
            objectMap.put("title",sku.getTitle());
            String image = StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split( sku.getImages(),",")[0];
            objectMap.put("image", image);

            objectMaps.add(objectMap);
        });

        //准备specs


        return null;
    }
}
