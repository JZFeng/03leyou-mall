/**
 * @Author jzfeng
 * @Date 5/10/21-4:53 AM
 */

package com.leyou.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Param;
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
import org.springframework.util.CollectionUtils;

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
        List<String> categoryNames = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        String all = spu.getTitle() + " " + StringUtils.join(categoryNames, " ") + " " + brand.getName();

        //准备skus
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spu.getId()).stream().map(sku -> {
            return Sku.builder().id(sku.getId()).title(sku.getTitle()).price(sku.getPrice()).images(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]).build();
        }).collect(Collectors.toList());

        //准备price列表
        List<Long> price = skus.stream().map(sku -> sku.getPrice()).collect(Collectors.toList());

        //准备specs
        List<Param> params = specificationClient.queryParamsBySpuId(spu.getId());
        if (!CollectionUtils.isEmpty(params)) {
            Map<String, Object> param_map = new HashMap<>(); //最后存到specs字段中
            params.forEach(param -> {
                String k = param.getK();
                List<String> options = param.getOptions();
                if (CollectionUtils.isEmpty(options)) {
                    //v有值，记得加单位
                    String v = param.getV() + (StringUtils.isBlank(param.getUnit()) ? "" : param.getUnit());
                    param_map.put(k, v);
                } else {
                    //Option有值
                    param_map.put(k, options);
                }
            });

            try {
                return Goods.builder()
                        .id(spu.getId())
                        .all(all)
                        .subTitle(spu.getSubTitle())
                        .cid1(spu.getCid1()).cid2(spu.getCid2()).cid3(spu.getCid3())
                        .brandId(spu.getBrandId())
                        .createdTime(spu.getCreateTime())
                        .price(price)
                        //TO-DO: BUG FIX;
                        .skus(skus)
                        .specs(param_map)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
